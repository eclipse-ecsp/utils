package com.harman.ignite.healthcheck;

public class TestHealthMonitor implements HealthMonitor, HealthServiceCallBack {

    private boolean healthy;
    private boolean needsRetartOnFailure;
    private boolean enabled;
    private String monitorName = "TestHealthMonitor";
    private String metricName = "TestHealthMonitorGuage";

    @Override
    public boolean isHealthy(boolean forceHealthCheck) {
        return healthy;
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
    
    @Override
    public boolean performRestart() {
    	return false;
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
