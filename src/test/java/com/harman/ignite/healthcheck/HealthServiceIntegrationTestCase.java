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
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Enumeration;
import java.util.List;

/**
 * Integration test for HealthService.
 *
 * @see HealthService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestHealthServiceConfig.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HealthServiceIntegrationTestCase {

    static final double HEALTHY = 0.0;
    static final double UNHEALTHY = 1.0;
    static final int TWO = 2;
    static final String SERVICE_HEALTH = "SERVICE_HEALTH";
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(HealthServiceIntegrationTestCase.class);
    @Autowired
    private HealthService healthService;

    @Before
    public void setup() {
        clearMetrics();
    }

    /**
     * Post constructor should have autowired the health monitors and not started the executor.
     */
    @Test
    public void testInit() {
        List<HealthMonitor> monitors = healthService.getHealthMonitors();
        // Although 3 monitors are present in Configuration class one is
        // disabled hence only 2 monitors should be present
        Assert.assertEquals(TWO, monitors.size());
    }

    @Test
    public void testNeedsRestartWithHealthyMonitor() throws InterruptedException {
        TestHealthMonitor overrideMonitor = new TestHealthMonitor();
        overrideMonitor.setEnabled(true);
        overrideMonitor.setHealthy(true);
        overrideMonitor.setNeedsRestartOnFailure(true);

        List<HealthMonitor> monitors = healthService.getHealthMonitors();

        monitors.clear();
        monitors.add(overrideMonitor);

        healthService.setHealthMonitors(monitors);

        Assert.assertFalse(healthService.needsRestart());

        Enumeration<MetricFamilySamples> allSamples = CollectorRegistry.defaultRegistry.metricFamilySamples();

        while (allSamples.hasMoreElements()) {
            MetricFamilySamples samples = allSamples.nextElement();
            LOGGER.info("Metric name {}", samples.name);

            for (Sample sample : samples.samples) {
                Assert.assertEquals(HEALTHY, sample.value, 0.0D);
            }
        }

    }

    @Test
    public void testNeedsRestartWithUnHealthyMonitor() throws InterruptedException {
        TestHealthMonitor overrideMonitor = new TestHealthMonitor();
        overrideMonitor.setEnabled(true);
        overrideMonitor.setHealthy(false);
        overrideMonitor.setNeedsRestartOnFailure(true);

        List<HealthMonitor> monitors = healthService.getHealthMonitors();

        monitors.clear();
        monitors.add(overrideMonitor);

        healthService.setHealthMonitors(monitors);

        Assert.assertTrue(healthService.needsRestart());

        Enumeration<MetricFamilySamples> allSamples = CollectorRegistry.defaultRegistry.metricFamilySamples();

        while (allSamples.hasMoreElements()) {
            MetricFamilySamples samples = allSamples.nextElement();
            LOGGER.info("Metric name {}", samples.name);

            for (Sample sample : samples.samples) {
                Assert.assertEquals(UNHEALTHY, sample.value, 1.0D);
            }
        }
    }

    /**
     * Clear all the metrics.
     */
    public void clearMetrics() {
        Enumeration<MetricFamilySamples> mfsEnumerator = CollectorRegistry.defaultRegistry.metricFamilySamples();
        while (mfsEnumerator.hasMoreElements()) {
            MetricFamilySamples mfs = mfsEnumerator.nextElement();
            if (mfs.samples != null) {
                mfs.samples.clear();
            }
        }
    }

}
