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

package com.harman.ignite.utils.metrics;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Test class for IgniteGuageTest.
 *
 * @see TestGuage
 */
public class IgniteGuageTest {

    private static final double TEN_DOT_FIVE = 10.5;
    private final String name = "test_guage";
    private final String[] labels = { "test_metric", "node", "monitorname" };

    @InjectMocks
    private TestGuage testGuage = new TestGuage();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectorRegistry.defaultRegistry.clear();
    }

    @Test
    public void testCreateGuage() {
        testGuage.createGuage(name, labels);
        Gauge guage = testGuage.getIgniteGuageMetric();

        Assert.assertTrue(guage != null && guage instanceof Gauge);

        List<MetricFamilySamples> samples = guage.collect();
        for (MetricFamilySamples sample : samples) {
            Assert.assertEquals("test_guage", sample.name);
        }

        //trying to create a guage that already exists, it won't create and publish a WARN logger
        testGuage.createGuage(name, labels);
    }

    @Test
    public void testSetValueOnGuage() {
        testGuage.createGuage(name, labels);
        Gauge guage = testGuage.getIgniteGuageMetric();
        testGuage.setIgniteGuageMetric(guage);
        testGuage.set(TEN_DOT_FIVE, labels);
        double value = testGuage.get(labels);

        Assert.assertEquals(TEN_DOT_FIVE, value, 0);
    }

    /** Test class for IgniteGuage.
     *
     * @see IgniteGuage
     */
    public static class TestGuage extends IgniteGuage {

    }
}
