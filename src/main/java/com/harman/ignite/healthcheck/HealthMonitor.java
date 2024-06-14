package com.harman.ignite.healthcheck;

/**
 * Contract for health monitors that intend to publish health to the
 * HealthService
 * 
 * @author avadakkootko
 *
 */
public interface HealthMonitor {

    /**
     * 
     * @param forceHealthCheck
     * @return true if healthmonitor is healthy. If the argument
     *         forceHealthCheck is true. Then it needs to trigger a forced
     *         health check
     * 
     */
    public boolean isHealthy(boolean forceHealthCheck);

    /**
     * 
     * @return name of the health monitor
     */
    public String monitorName();

    /**
     * 
     * @return true if the health monitor is unhealthy and hence service should
     *         be restarted.
     */
    public boolean needsRestartOnFailure();

    /**
     * 
     * @return name of the ignite guage
     */
    public String metricName();

    /**
     * 
     * @return if the service is enabled or not
     */
    public boolean isEnabled();

}
