package com.harman.ignite.diagnostic;

public class TestDiagnosticReporter implements DiagnosticReporter {

    private boolean enabled;
    private String reporterName = "TestDiagnosticReporter";
    private String metricName = "TestDiagnosticReporterGuage";
    private DiagnosticData data = new DiagnosticData();

    @Override
    public DiagnosticData getDiagnosticData() {
        return data;
    }

    @Override
    public String getDiagnosticReporterName() {
        return reporterName;
    }

    @Override
    public String getDiagnosticMetricName() {
        return metricName;
    }

    @Override
    public boolean isDiagnosticReporterEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setData(DiagnosticData data) {
        this.data = data;
    }

}
