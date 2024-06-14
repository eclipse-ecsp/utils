package com.harman.ignite.healthcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteHealthGuage;

/**
 * The objective of IgniteHealthMonitor is publish health of stream processor to
 * prometheus. This can also be used for readiness and liveliness probe.
 * 
 * @author avadakkootko
 *
 */
@Component
public class HealthService {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(HealthService.class);

    @Autowired(required = false)
    private List<HealthMonitor> healthMonitors;

    @Value("${NODE_NAME:localhost}")
    private String nodeName;

    @Value("${health.service.failure.retry.thrshold:10}")
    private int failureRetryThreshold;

    @Value("${health.service.failure.retry.interval.millis:50}")
    private int failureRetryInterval;

    @Value("${health.service.retry.interval.millis:100}")
    private int retryInterval;

    @Value("${health.service.executor.shutdown.millis:2000}")
    private int shutdownBuffer;

    @Value("${health.service.executor.initial.delay:300000}")
    private long initialDelay;

    static final String SERVICE_HEALTH = "SERVICE_HEALTH";
    static final String ISHEALTHY = " is healthy; ";
    static final String ISUNHEALTHY = " is unhealthy;";
    static final double HEALTHY = 0;
    static final double UNHEALTHY = 1;
    private HealthServiceCallBack callback;

    // Here we are using a single guage with multiple labels for each monitor.
    @Autowired
    private IgniteHealthGuage serviceHealthGuage;

    private ScheduledExecutorService healthServiceExecutor = null;
    private final AtomicBoolean startedExecutor = new AtomicBoolean(false);

    private HealthServiceState previousState = new HealthServiceState();
    private HealthServiceState currentState = new HealthServiceState();

    /**
     * It accepts two parameters boolean value force and list of healthmonitor
     * to be checked for health status. If force is true it triggers a forced
     * health check for all health monitors. It returns a list of failed
     * monitors which can be retried in case of failures.
     * 
     * @param force
     * @param hms
     * @return
     */
    protected synchronized List<HealthMonitor> checkHealthAndGetFailedMonitors(boolean force, List<HealthMonitor> hms) {
        List<HealthMonitor> failedHealthMonitors = new ArrayList<>();
        boolean spHealthy = true;
        StringBuilder status = new StringBuilder();
        for (HealthMonitor healthMonitor : hms) {
            String monitorName = healthMonitor.monitorName();
            String metricName = healthMonitor.metricName();
            if (healthMonitor.isHealthy(force)) {
                status.append(healthMonitor.monitorName()).append(ISHEALTHY);
                serviceHealthGuage.set(HEALTHY, nodeName, metricName);
            }
            else {
                status.append(monitorName).append(ISUNHEALTHY);
                serviceHealthGuage.set(UNHEALTHY, nodeName, metricName);
                failedHealthMonitors.add(healthMonitor);
                spHealthy = false;
            }
        }

        String statusMsg = status.toString();
        double state = 0;
        if (spHealthy) {
            state = HEALTHY;
            serviceHealthGuage.set(HEALTHY, nodeName, SERVICE_HEALTH);
        }
        else {
            state = UNHEALTHY;
            serviceHealthGuage.set(UNHEALTHY, nodeName, SERVICE_HEALTH);
        }
        currentState.setState(state);
        currentState.setMessage(statusMsg);
        if (!currentState.equals(previousState)) {
            printStatus(spHealthy, statusMsg);
            previousState.setState(currentState.getState());
            previousState.setMessage(currentState.getMessage());
        }
        return failedHealthMonitors;
    }

    private void printStatus(boolean spHealthy, String statusMsg) {
        if (spHealthy) {
            LOGGER.info("Health status :: healthy; desc: {}", statusMsg);
        }
        else {
            LOGGER.error("Health status :: unhealthy; desc: {}", statusMsg);
        }
    }

    /**
     * This method can be used for the initial forced health check by Stream
     * base Launcher or API without starting the scheduled executor. Why this
     * approach because there may be certain monitors which is bound to return
     * unhealthy unless the process starts. For example kafka state listener
     * health monitor will return unhealthy unless stream processor starts hence
     * this may have to be ignored for initial health check.
     * 
     * Another approach is to come up with a new contract that says
     * initialCheckDisabled. This can be achieved in the next step.
     * 
     * @param opt
     * @return
     */
    public List<HealthMonitor> triggerInitialCheck() {
        List<HealthMonitor> failedHealthMonitors = null;
        boolean force = true;
        failedHealthMonitors = checkHealthAndGetFailedMonitors(force, healthMonitors);
        int failedMonitorsSize = failedHealthMonitors.size();
        if (failedMonitorsSize > 0) {
            LOGGER.error("Initial health check failed with {} healthmonitors", failedMonitorsSize);
        }
        else {
            LOGGER.info("Initial health check has passed");
        }
        return failedHealthMonitors;
    }

    /**
     * Invoked by the scheduled executor. It wraps the retry strategy in case of
     * failure scenario.
     * 
     * @return
     * @throws InterruptedException
     */
    protected boolean needsRestart() throws InterruptedException {
        // For the first health check default states of variable unHealthy will
        // be true and force will be force.
        startedExecutor.set(true);
        boolean restart = true;
        boolean force = false;
        List<HealthMonitor> hms = new ArrayList<>(healthMonitors);
        int counter = 0;
        do {
            List<HealthMonitor> failedHms = checkHealthAndGetFailedMonitors(force, hms);
            hms.clear();
            for (HealthMonitor hm : failedHms) {
                if (hm.needsRestartOnFailure()) {
                    hms.add(hm);
                }
            }
            if (!hms.isEmpty()) {
                restart = true;
                force = true;
            }
            else {
                restart = false;
                force = false;
            }
            counter++;
            Thread.sleep(failureRetryInterval);
        } while (counter <= (failureRetryThreshold) && restart);
        return restart;
    }

    /**
     * Create a list of health monitors that are enabled and if two monitors
     * have same name throw exception
     */
    @PostConstruct
    public void init() {
        Map<String, String> metricToMonitorMapping = new HashMap<>();
        List<HealthMonitor> enabledHealthMonitors = new ArrayList<>();
        if (healthMonitors != null) {
            for (HealthMonitor healthMonitor : healthMonitors) {
                if (healthMonitor.isEnabled()) {
                    String metricName = healthMonitor.metricName();
                    String monitorName = healthMonitor.monitorName();
                    // If two monitors have metrics with same name throw
                    // exception
                    if (metricToMonitorMapping.containsKey(metricName)) {
                        LOGGER.error("Two health monitors {} and {} cannot have same MetricName :", monitorName,
                                metricToMonitorMapping.get(metricName), metricName);
                        throw new InvalidMetricNamingException(
                                "Two health monitors " + monitorName + "and " + metricToMonitorMapping.get(metricName)
                                        + "cannot have same MetricName " + metricName);
                    }
                    LOGGER.info("Health monitor {}, is enabled.", healthMonitor.monitorName());
                    metricToMonitorMapping.put(metricName, healthMonitor.monitorName());
                    enabledHealthMonitors.add(healthMonitor);
                }
                else {
                    LOGGER.info("Health monitor {}, is disabled.", healthMonitor.monitorName());
                }
            }
        }
        healthMonitors = new ArrayList<>(enabledHealthMonitors);
        createhHealthServiceExecutor();

    }

    private void createhHealthServiceExecutor() {
        healthServiceExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread t = Executors.defaultThreadFactory().newThread(runnable);
            t.setDaemon(true);
            t.setUncaughtExceptionHandler(new HealthServiceUncaughtExceptionHandler());
            t.setName(Thread.currentThread().getName() + ":" + "HealthService");
            return t;
        });
    }

    /**
     * Start the scheduled executor which will periodically check health of
     * HealthMonitors.
     */
    public synchronized void startHealthServiceExecutor() {
        if (!startedExecutor.get()) {
            healthServiceExecutor.scheduleWithFixedDelay(() -> {
                try {
                    checkCallback(callback);
                }catch (InterruptedException e){
                    LOGGER.error("Error occurred while executing health service scheduled thread {}", e);
                    Thread.currentThread().interrupt();
                }catch (Exception e) {
                    LOGGER.error("Error occurred while executing health service scheduled thread {}", e);
                }

            }, initialDelay, retryInterval, TimeUnit.MILLISECONDS);
        }
    }

    private void checkCallback(HealthServiceCallBack callback) throws InterruptedException {
        boolean restart = needsRestart();
        if (restart) {
            if (callback != null && callback.performRestart()) {
                close();
            } else {
                LOGGER.trace("Service is unhealthy. Continuing health check without restart");
            }
        }
    }

    private class HealthServiceUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable t) {
            LOGGER.error("Uncaught exception in thread {} ", thread.getName(), t);
        }
    }

    protected void close() {
        if (healthServiceExecutor != null && !healthServiceExecutor.isShutdown()) {
            LOGGER.info("Shutting the SingleThreadScheduledExecutor for health service!");
            ThreadUtils.shutdownExecutor(healthServiceExecutor, shutdownBuffer, false);
            startedExecutor.set(false);
        }
    }

    public void registerCallBack(HealthServiceCallBack callback) {
        this.callback = callback;
        LOGGER.info("Registered HealthService callback");
    }

    // Below are for test case support
    void setHealthMonitors(List<HealthMonitor> healthMonitors) {
        this.healthMonitors = healthMonitors;
    }

    List<HealthMonitor> getHealthMonitors() {
        return this.healthMonitors;
    }

    void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    void setFailureRetryThreshold(int failureRetryThreshold) {
        this.failureRetryThreshold = failureRetryThreshold;
    }

    void setFailureRetryInterval(int failureRetryInterval) {
        this.failureRetryInterval = failureRetryInterval;
    }

    void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    void setServiceHealthGuage(IgniteHealthGuage serviceHealthGuage) {
        this.serviceHealthGuage = serviceHealthGuage;
    }

    boolean isStartedExecutor() {
        return startedExecutor.get();
    }
    
    HealthServiceCallBack getCallback() {
    	return this.callback;
    }

}
