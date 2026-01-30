import json
import os
import sys
import xml.etree.ElementTree as ET
from collections import Counter
from datetime import datetime
import urllib.request
import base64

TRIVY_PATH = os.getenv("TRIVY_REPORT", "trivy-fs-report.json")
JACOCO_XML = os.getenv("JACOCO_XML", "target/site/jacoco/jacoco.xml")

SONAR_TOKEN = os.getenv("SONAR_TOKEN")
SONAR_PROJECT_KEY = os.getenv("SONAR_PROJECT_KEY")
SONAR_ORGANIZATION = os.getenv("SONAR_ORGANIZATION")

GITHUB_RUN_NUMBER = os.getenv("GITHUB_RUN_NUMBER", "")
GITHUB_SHA = os.getenv("GITHUB_SHA", "")
GITHUB_REF_NAME = os.getenv("GITHUB_REF_NAME", "")

def trivy_counts(path: str):
    if not os.path.exists(path):
        return {"CRITICAL": 0, "HIGH": 0, "MEDIUM": 0, "LOW": 0, "UNKNOWN": 0}
    data = json.load(open(path, encoding="utf-8"))
    c = Counter()
    for r in data.get("Results", []):
        for v in (r.get("Vulnerabilities") or []):
            c[v.get("Severity", "UNKNOWN")] += 1
    return {k: int(c.get(k, 0)) for k in ["CRITICAL", "HIGH", "MEDIUM", "LOW", "UNKNOWN"]}

def jacoco_line_coverage(xml_path: str):
    # returns coverage percent (line covered / line missed+covered)
    if not os.path.exists(xml_path):
        return None
    root = ET.parse(xml_path).getroot()
    # Find the <counter type="LINE" missed=".." covered=".."/>
    for counter in root.findall("counter"):
        if counter.attrib.get("type") == "LINE":
            missed = int(counter.attrib.get("missed", 0))
            covered = int(counter.attrib.get("covered", 0))
            total = missed + covered
            return round((covered / total) * 100.0, 2) if total > 0 else None
    return None

def sonar_measures():
    # Fetches key measures from SonarCloud API
    if not (SONAR_TOKEN and SONAR_PROJECT_KEY):
        return {}
    metric_keys = ",".join([
        "bugs",
        "vulnerabilities",
        "code_smells",
        "security_hotspots",
        "coverage",
        "duplicated_lines_density",
    ])
    url = f"https://sonarcloud.io/api/measures/component?component={SONAR_PROJECT_KEY}&metricKeys={metric_keys}"
    auth = base64.b64encode((SONAR_TOKEN + ":").encode("utf-8")).decode("utf-8")
    req = urllib.request.Request(url, headers={"Authorization": f"Basic {auth}"})
    with urllib.request.urlopen(req) as resp:
        payload = json.loads(resp.read().decode("utf-8"))
    measures = payload.get("component", {}).get("measures", [])
    out = {}
    for m in measures:
        out[m["metric"]] = m.get("value")
    return out

def main():
    trivy = trivy_counts(TRIVY_PATH)
    jacoco_cov = jacoco_line_coverage(JACOCO_XML)
    sonar = sonar_measures()

    metrics = {
        "timestamp": datetime.utcnow().isoformat() + "Z",
        "github_run_number": GITHUB_RUN_NUMBER,
        "commit_sha": GITHUB_SHA,
        "branch": GITHUB_REF_NAME,
        "trivy": trivy,
        "jacoco_line_coverage_pct": jacoco_cov,
        "sonar": sonar,
    }

    os.makedirs("reports", exist_ok=True)
    with open("reports/run-metrics.json", "w", encoding="utf-8") as f:
        json.dump(metrics, f, indent=2)

    # also write a 1-line CSV for easy aggregation
    def val(x): return "" if x is None else str(x)
    csv_line = ",".join([
        val(GITHUB_RUN_NUMBER),
        val(GITHUB_SHA),
        val(GITHUB_REF_NAME),
        val(sonar.get("bugs")),
        val(sonar.get("vulnerabilities")),
        val(sonar.get("code_smells")),
        val(sonar.get("security_hotspots")),
        val(sonar.get("coverage")),
        val(sonar.get("duplicated_lines_density")),
        val(jacoco_cov),
        str(trivy["CRITICAL"]),
        str(trivy["HIGH"]),
        str(trivy["MEDIUM"]),
    ])
    header = "github_run_number,commit_sha,branch,sonar_bugs,sonar_vulnerabilities,sonar_code_smells,sonar_security_hotspots,sonar_coverage_pct,sonar_duplications_pct,jacoco_line_coverage_pct,trivy_critical,trivy_high,trivy_medium"
    with open("reports/run-metrics.csv", "w", encoding="utf-8") as f:
        f.write(header + "\n")
        f.write(csv_line + "\n")

    print("Wrote reports/run-metrics.json and reports/run-metrics.csv")

if __name__ == "__main__":
    sys.exit(main())
