/*
 * *******************************************************************************
 *
 *  Copyright (c) 2023-24 Harman International
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *       
 *
 *  Unless required by applicable law or agreed to in writing, software
 *
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *
 *  limitations under the License.
 *
 *
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  *******************************************************************************
 */

package com.harman.ignite.healthcheck;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteHealthGuage;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Test class for HealthService.
 *
 * @see HealthService
 */
public class HealthServiceUnitTest {
    static final String SERVICE_HEALTH = "SERVICE_HEALTH";
    static final double HEALTHY = 0;
    static final double UNHEALTHY = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final int SEVEN = 7;
    static final int HUNDRED = 100;
    static final long THOUSAND = 1000L;
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(HealthServiceUnitTest.class);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @InjectMocks
    private HealthService healthService = new HealthService();

    @Mock
    private IgniteHealthGuage serviceHealthGuage;
    @Mock
    private ThreadUtils threadUtils;

    /**
     * Setup the test.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        healthService.setServiceHealthGuage(serviceHealthGuage);
        healthService.setFailureRetryInterval(HUNDRED);
        healthService.setFailureRetryThreshold(TWO);
        healthService.setRetryInterval(HUNDRED);
        healthService.setNodeName("localhost");
    }

    /**
     * HealthMonitor used simple TestHealthMonitor;
     * Count = 2; Both healthMonitors are enabled; Both healthMonitors are set healthy ; Force
     * is set to false;
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalse() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * HealthMonitor used simple TestHealthMonitor;
     * Count = 2; Both healthMonitors are enabled; Both healthMonitors are set healthy ; Force
     * is set to false;
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalseWithPromethus() {
        Enumeration<MetricFamilySamples> allSamples = CollectorRegistry.defaultRegistry.metricFamilySamples();
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * HealthMonitor used simple TestHealthMonitor;
     * Count = 2; Both healthMonitors are enabled; HealthMonitor 1 is set healthy ;
     * HealthMonitor 2 is set to unhealthy ; Force is set to false;
     * As one health monitor is not healthy we are expecting one failed health monitor<br>
     * Expecting failure.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceFalseWhenOneMonitorIsUnHealthy() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());
        boolean force = false;

        // Validate Failed hms should be 1
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(1, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * HealthMonitor used simple TestHealthMonitor;
     * Count = 2; Both healthMonitors are enabled; Both HealthMonitors are set healthy ; Force
     * is set to true;
     * As both health monitors are healthy setting force to true or false should not have any impact
     * Expecting success.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceTrueWhenHealthMonitorsAreHealthy() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());
        boolean force = true;

        // Validate Failed hms should be 0
        List<HealthMonitor> failedHms = healthService.checkHealthAndGetFailedMonitors(force, hms);
        Assert.assertEquals(0, failedHms.size());
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * Here we are using two types of health monitors TestHealthMonitor and TestHealthMonitorWithForce.
     * Both healthMonitors are enabled;
     * Both HealthMonitors are set healthy ; Force is set to true;
     * As one is unhealthy and force is true we will be retrying.
     * Expecting success after retrying.
     */
    @Test
    public void testCheckHealthAndGetFailedMonitorsWithForceTrueWhenHealthMonitorsAreUnHealthy() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());
        boolean force = true;
        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(TWO))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(TWO))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(TWO))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(THREE))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(THREE))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(THREE))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(FOUR))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(FOUR))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(FOUR))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);

        healthService.checkHealthAndGetFailedMonitors(force, hms);
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(FIVE))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * HealthMonitor used simple TestHealthMonitor;
     * Count = 2; Both healthMonitors are enabled; HealthMonitor 1 is set healthy ;
     * HealthMonitor 2 is set to unhealthy ;
     * <br>
     * As one health monitor is unhealthy we are expecting failure not have any impact
     * <br>
     * Expecting failure.
     */
    @Test
    public void testTriggerInitialCheck() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Assert.assertEquals(TWO, enabledHms.size());

        List<HealthMonitor> failedList = healthService.triggerInitialCheck();
        Assert.assertEquals(1, failedList.size());

        Assert.assertEquals("Metric2", failedList.get(0).metricName());
        Assert.assertEquals("Monitor2", failedList.get(0).monitorName());

        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);

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
        final List<HealthMonitor> hms = new ArrayList<>();
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

        Assert.assertFalse(healthService.isStartedExecutor());
    }

    @Test
    public void testRegisterCallback() {
        TestHealthMonitor hm = new TestHealthMonitor();
        healthService.registerCallBack(hm);
        
        Assert.assertNotNull(healthService.getCallback());
    }

    /**
     * Here we are using two types of health monitors TestHealthMonitor and TestHealthMonitorWithForce.
     * Both healthMonitors are enabled;
     * Both HealthMonitors are set healthy ;
     * <br>
     * As both are healthy it is a success scenario
     * <br>
     * Expecting success.
     */
    @Test
    public void testNeedsRestartFalseScenario() throws InterruptedException {
        healthService.setFailureRetryThreshold(SEVEN);
        final List<HealthMonitor> hms = new ArrayList<HealthMonitor>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setHealthy(true);
        hm.setNeedsRestartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRestartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(TWO, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * Objective is to test the retry threshold of health service.
     * The method needsRestart will repeat in a loop until the retry threshold is exceeded;
     *  or before that the service becomes healthy. Testing with retry threshold 5.
     * <br>
     * The health monitor TestHealthMonitorWithForce will give true after 3 attempts with force true
     * As both are healthy it is a success scenario.
     * Hence, for retry threshold > 4 we should get healthy
     * <br>
     * <br>
     * Expecting success after retry.
     */
    @Test
    public void testNeedsRestartFalseScenarioWithRetry() throws InterruptedException {
        healthService.setFailureRetryThreshold(FIVE);
        final List<HealthMonitor> hms = new ArrayList<>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRestartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRestartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(TWO, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(FIVE))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(FIVE))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", SERVICE_HEALTH);

    }

    /**
     * Objective is to test the retry threshold of health service.
     * The method needsRestart will repeat in a loop until the retry threshold is exceeded;
     * or before that the service becomes healthy. Testing with retry threshold 5.
     * <br>
     * The health monitor TestHealthMonitorWithForce will give true after 3 attempts with force true
     * As both are healthy it is a success scenario.
     * Hence, for retry threshold > 4 we should get healthy
     * <br>
     * <br>
     * Expecting failure as retry threshold == 4
     */

    @Test
    public void testNeedsRestartTrueScenario() throws InterruptedException {
        healthService.setFailureRetryThreshold(FOUR);
        final List<HealthMonitor> hms = new ArrayList<>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRestartOnFailure(true);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRestartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(TWO, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertTrue(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(FIVE))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(FIVE))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * If a health monitor needs restart is false.
     * Even if it is unhealthy the service should return restart false.
     */
    @Test
    public void testNeedsRestartFalseScenarioWithMonitorRestartFalse() throws InterruptedException {
        healthService.setFailureRetryThreshold(FOUR);
        final List<HealthMonitor> hms = new ArrayList<>();
        TestHealthMonitorWithForce hm = new TestHealthMonitorWithForce();
        hm.setEnabled(true);
        hm.setMetricName("Metric1");
        hm.setMonitorName("Monitor1");
        hm.setNeedsRestartOnFailure(false);
        hms.add(hm);

        TestHealthMonitor hm2 = new TestHealthMonitor();
        hm2.setEnabled(true);
        hm2.setMetricName("Metric2");
        hm2.setMonitorName("Monitor2");
        hm2.setHealthy(true);
        hm2.setNeedsRestartOnFailure(true);
        hms.add(hm2);

        healthService.setHealthMonitors(hms);
        healthService.init();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(TWO, enabledHms.size());

        boolean restart = healthService.needsRestart();
        Assert.assertFalse(restart);

        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", "Metric1");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(HEALTHY, "localhost", "Metric2");
        Mockito.verify(serviceHealthGuage, Mockito.times(1))
                .set(UNHEALTHY, "localhost", SERVICE_HEALTH);
    }

    /**
     * If two health monitors have same name it should throw runtime exception.
     */
    @Test(expected = RuntimeException.class)
    public void testInitTwoMonitorsWithSameName() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
     * If a health monitor is disabled, it should not be added in the list of health monitors.
     */
    @Test
    public void testInitWithDisabledMonitor() {
        final List<HealthMonitor> hms = new ArrayList<>();
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
        final List<HealthMonitor> hms = new ArrayList<>();
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
        Thread.sleep(THOUSAND);
        healthService.startHealthServiceExecutor();
        List<HealthMonitor> enabledHms = healthService.getHealthMonitors();
        Assert.assertEquals(TWO, enabledHms.size());
        LOGGER.info("Started  again health service");
    }
}
