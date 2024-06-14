package com.harman.ignite.healthcheck;

public class TestHealthMonitorWithForce implements HealthMonitor {

    private boolean healthy;
    private boolean needsRetartOnFailure;
    private boolean enabled;
    private String monitorName = "TestHealthMonitorWithForce";
    private String metricName = "TestHealthMonitorWithForceGuage";
    private int i = 0;

    @Override
    public boolean isHealthy(boolean forceHealthCheck) {
        if (i > 3)
            return true;
        if (forceHealthCheck) {
            i++;
        }
        return false;
    }

    @Override
    public String monitorName() {
        return monitorName;
    }

    @Override
    public boolean needsRestartOnFailure() {
        return needsRetartOnFailure;
    }

    @Override
    public String metricName() {
        return metricName;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setNeedsRetartOnFailure(boolean needsRetartOnFailure) {
        this.needsRetartOnFailure = needsRetartOnFailure;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

}
