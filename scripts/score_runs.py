import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest
import matplotlib.pyplot as plt

INPUT = "reports/runs.csv"
OUTPUT = "reports/runs_scored.csv"

df = pd.read_csv(INPUT)

# -------- Classic baseline rules (adjustable) --------
def classic_level(row):
    critical = int(row["trivy_critical"])
    high = int(row["trivy_high"])
    cov = float(row["sonar_coverage_pct"])
    if critical >= 2:
        return "HIGH"
    if high >= 16 or critical == 1:
        return "MEDIUM"
    if critical == 0 and high < 16:
        return "LOW"
    return "LOW"

df["classic_risk_level"] = df.apply(classic_level, axis=1)

# -------- ML features (per run) --------
feature_cols = [
    "trivy_critical",
    "trivy_high",
    "trivy_medium",
    "sonar_coverage_pct",
    "sonar_duplications_pct",
    "sonar_code_smells",
]

X = df[feature_cols].astype(float).values

# Train an Isolation Forest on your runs (unsupervised)
model = IsolationForest(
    n_estimators=300,
    contamination="auto",
    random_state=42
)
model.fit(X)

# decision_function: higher = more normal
normality = model.decision_function(X)

# scale to 0..100 (higher = riskier)
nmin, nmax = normality.min(), normality.max()
risk_score = (nmax - normality) / (nmax - nmin + 1e-9) * 100

df["ml_risk_score"] = np.round(risk_score, 2)

def ml_level(score):
    if score >= 70: return "HIGH"
    if score >= 40: return "MEDIUM"
    return "LOW"

df["ml_risk_level"] = df["ml_risk_score"].apply(ml_level)

# Save scored runs
df.to_csv(OUTPUT, index=False)
print(f"Saved: {OUTPUT}")

# -------- Plots (saved as PNG) --------
# 1) Risk score over runs
plt.figure()
plt.plot(df["run_id"], df["ml_risk_score"], marker="o")
plt.xlabel("run_id")
plt.ylabel("ml_risk_score")
plt.title("ML risk score over runs")
plt.savefig("plot_ml_risk_score.png", dpi=200)

# 2) Trivy Critical/High over runs
plt.figure()
plt.plot(df["run_id"], df["trivy_critical"], marker="o", label="critical")
plt.plot(df["run_id"], df["trivy_high"], marker="o", label="high")
plt.xlabel("run_id")
plt.ylabel("count")
plt.title("Trivy findings over runs")
plt.legend()
plt.savefig("plot_trivy_counts.png", dpi=200)

# 3) Sonar coverage over runs
plt.figure()
plt.plot(df["run_id"], df["sonar_coverage_pct"], marker="o")
plt.xlabel("run_id")
plt.ylabel("sonar_coverage_pct")
plt.title("Sonar coverage over runs")
plt.savefig("plot_sonar_coverage.png", dpi=200)

print("Saved plots: plot_ml_risk_score.png, plot_trivy_counts.png, plot_sonar_coverage.png")