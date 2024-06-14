package com.harman.ignite.healthcheck;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteHealthGuage;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;

public class HealthServiceUnitTest {
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(HealthServiceUnitTest.class);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private HealthService healthService = new HealthService();

    static final String SERVICE_HEALTH = "SERVICE_HEALTH";



    @Mock
    private IgniteHealthGuage serviceHealthGuage;
    @Mock
    private ThreadUtils threadUtils;
    static final double HEALTHY = 0;
    static final double UNHEALTHY = 1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        healthService.setServiceHealthGuage(serviceHealthGuage);
        healthService.setFailureRetryInterval(100);
        healthService.setFailureRetryThreshold(2);
        healthService.setRetryInterval(100);
        healthService.setNodeName("localhost");
    }

    /**
     * HealthMonitor used simple TestHealthMonitor; Count = 2; Both
     * healthMonitors are enabled; Both healthMonitors are set healthy ; Force
     * is set to false;
     * 
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalse() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * HealthMonitor used simple TestHealthMonitor; Count = 2; Both
     * healthMonitors are enabled; Both healthMonitors are set healthy ; Force
     * is set to false;
     * 
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalseWithPromethus() {
        Enumeration<MetricFamilySamples> allSamples = CollectorRegistry.defaultRegistry.metricFamilySamples();
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * HealthMonitor used simple TestHealthMonitor; Count = 2; Both
     * healthMonitors are enabled; HealthMonitor 1 is set healthy ;
     * HealthMonitor 2 is set to unhealthy ; Force is set to false;
     * 
     * As one health monitor is not healthy we are expecting one failed health
     * monitor
     * 
     * Expecting failure.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalseWhenOneMonitorIsUnHealthy() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(false);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 1
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(1, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * HealthMonitor used simple TestHealthMonitor; Count = 2; Both
     * healthMonitors are enabled; Both HealthMonitors are set healthy ; Force
     * is set to true;
     * 
     * As both health monitors are healthy setting force to true or false should
     * not have any impact
     * 
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceTrueWhenHealthMonitorsAreHealthy() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        boolean force = true;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * Here we are using two types of health monitors TestHealthMonitor and
     * TestHealthMonitorWithForce. Both healthMonitors are enabled; Both
     * HealthMonitors are set healthy ; Force is set to true;
     * 
     * As one is unhealthy and force is true we will be retyring.
     * 
     * Expecting success after retrying.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceTrueWhenHealthMonitorsAreUnHealthy() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        boolean force = true;
        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(2)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(2)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(2)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(3)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(3)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(3)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(4)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(4)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(4)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(5)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * HealthMonitor used simple TestHealthMonitor; Count = 2; Both
     * healthMonitors are enabled; HealthMonitor 1 is set healthy ;
     * HealthMonitor 2 is set to unhealthy ;
     * 
     * As one health monitor is unhealthy we are expecting failure not have any
     * impact
     * 
     * Expecting failure.
     */
    @Test
    public void testTriggerInitialCheck() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(false);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());

        List<HealthMonitor> failedList = healthService.triggerInitialCheck();
        Assert.assertEquals(1, failedList.size());

        Assert.assertEquals("Metric2", failedList.get(0).metricName());
        Assert.assertEquals("Monitor2", failedList.get(0).monitorName());

        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);
        
        hms.clear();
        hm2.setHealthy(true);
        hms.add(hm2);
        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> list = healthService.triggerInitialCheck();
        Assert.assertEquals(0, list.size());
    }
    
    @Test
    public void testClose() {
    	List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(false);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        healthService.close();
        
        Assert.assertTrue(!healthService.isStartedExecutor());
    }
    
    @Test
    public void testRegisterCallback() {
    	TestHealthMonitor hm = new TestHealthMonitor();
    	healthService.registerCallBack(hm);

    	Assert.assertNotNull(healthService.getCallback());
    }

    /**
     * Here we are using two types of health monitors TestHealthMonitor and
     * TestHealthMonitorWithForce. Both healthMonitors are enabled; Both
     * HealthMonitors are set healthy ;
     * 
     * As both are healthy it is a success scenario
     * 
     * Expecting success .
     */
    @Test
    public void testNeedsRestartFalseScenario() throws InterruptedException {
        healthService.setFailureRetryThreshold(7);
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hm.setNeedsRetartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRetartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * Objective is to test the retry threshold of health service. The method
     * needsRestart will repeat in a loop until the retry threshold is exceeded
     * or before that the service becomes healthy. Testing with retry threshold
     * 5.
     * 
     * The healthmonitor TestHealthMonitorWithForce will give true after 3
     * attempts with force true As both are healthy it is a success scenario.
     * Hence for retry threshold > 4 we should get healthy
     * 
     * 
     * Expecting success after retry.
     */
    @Test
    public void testNeedsRestartFalseScenarioWithRetry() throws InterruptedException {
        healthService.setFailureRetryThreshold(5);
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRetartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRetartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(5)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(5)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * Objective is to test the retry threshold of health service. The method
     * needsRestart will repeat in a loop until the retry threshold is exceeded
     * or before that the service becomes healthy. Testing with retry threshold
     * 5.
     * 
     * The healthmonitor TestHealthMonitorWithForce will give true after 3
     * attempts with force true As both are healthy it is a success scenario.
     * Hence for retry threshold > 4 we should get healthy
     * 
     * 
     * Expecting failure as retry threshold == 4
     */

    @Test
    public void testNeedsRestartTrueScenario() throws InterruptedException {
        healthService.setFailureRetryThreshold(4);
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRetartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRetartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertTrue(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(5)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(5)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * If a health monitor needs restart is false. Even if it is unhealthy the
     * service should return restart false.
     * 
     */
    @Test
    public void testNeedsRestartFalseScenarioWithMonitorRestartFalse() throws InterruptedException {
        healthService.setFailureRetryThreshold(4);
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRetartOnFailure(false);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRetartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1)).set(UNHEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * If two healthmonitors have same name it should throw runtime exception
     */
    @Test(expected = RuntimeException.class)
    public void testInitTwoMonitorsWithSameName() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric1");
        hm2.setMonitorName("Monitor2");
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
    }

    /**
     * If a healthmonitor is disabled it should not be added in the list of
     * healthmonitors
     */
    @Test
    public void testInitWithDisabledMonitor() {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(false);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();

        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(1, enabledHms.size());
        Assert.assertEquals("Metric1", enabledHms.get(0).metricName());
        Assert.assertEquals("Monitor1", enabledHms.get(0).monitorName());
    }

    @Test
    public void testStartHealthServiceExecutor() throws InterruptedException {
        List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitor hm = new TestHealthMonitor();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        healthService.startHealthServiceExecutor();
        LOGGER.info("Started health service");
        Thread.sleep(1000L);
        healthService.startHealthServiceExecutor();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(2, enabledHms.size());
        LOGGER.info("Started  again health service");
    }
}
