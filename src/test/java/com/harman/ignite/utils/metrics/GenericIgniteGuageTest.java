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
 * @see IgniteGuageTest
 */
public class GenericIgniteGuageTest {

    @InjectMocks
    IgniteGuageTest guage = new IgniteGuageTest();
    private static final double THREE = 3.0;
    private static final double THREE_DOT_FIVE = 3.5;
    private static final double FIVE = 5.0;
    private static final double SEVEN = 7.0;
    private static final double NINE = 9.0;
    private static final double TEN = 10.0;
    private final String name = null;
    private final String[] labels = { "node", "taskId" };

    /**
     * Setup method to initialize the mocks.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectorRegistry.defaultRegistry.clear();
        System.out.println("In before");
    }

    @Test
    public void testCreateGuage() {
        guage.createGauge("test_guage", "test_guage", labels);
        Gauge newGuage = guage.getGuage();
        List<MetricFamilySamples> samples = newGuage.collect();

        Assert.assertTrue(guage.getIsInitialized());
        samples.forEach(sample -> {
            Assert.assertEquals("test_guage", sample.name);
        });

        guage.createGauge(name, name, labels);
    }

    @Test
    public void testInc() {
        guage.createGauge("test_guage1", "test_guage1", labels);
        double value = guage.get(labels);

        Assert.assertEquals(0.0, value, 0.0);

        guage.inc(labels);
        value = guage.get(labels);

        Assert.assertEquals(1.0, value, 0.0);
    }

    @Test
    public void testIncBySomeValue() {
        guage.createGauge("test_guage2", "test_guage2", labels);
        Assert.assertEquals(0.0, guage.get(labels), 0.0);

        guage.inc(FIVE, labels);

        Assert.assertEquals(FIVE, guage.get(labels), 0.0);
    }

    @Test
    public void testDecBySomeValue() {
        guage.createGauge("test_guage3", "test_guage3", labels);
        guage.inc(TEN, labels);

        Assert.assertEquals(TEN, guage.get(labels), 0.0);

        guage.dec(THREE, labels);

        Assert.assertEquals(SEVEN, guage.get(labels), 0.0);
    }

    @Test
    public void testDec() {
        guage.createGauge("test_guage4", "test_guage4", labels);
        guage.inc(TEN, labels);

        Assert.assertEquals(TEN, guage.get(labels), 0.0);

        guage.dec(labels);

        Assert.assertEquals(NINE, guage.get(labels), 0.0);
    }

    @Test
    public void testSetGaugeValue() {
        guage.createGauge("test_guage5", "test_guage5", labels);

        Assert.assertEquals(0.0, guage.get(labels), 0.0);

        guage.set(THREE_DOT_FIVE, labels);

        Assert.assertEquals(THREE_DOT_FIVE, guage.get(labels), 0.0);
    }

    @Test
    public void testClear() {
        guage.createGauge("test_guage6", "test_guage6", labels);
        guage.set(SEVEN, labels);

        Assert.assertEquals(SEVEN, guage.get(labels), 0.0);

        guage.clear();

        Assert.assertEquals(0.0, guage.get(labels), 0.0);
    }

    /** Test Gauge class.
     *
     * @see IgniteGuageTest
     */
    public static class IgniteGuageTest extends AbstractIgniteGauge {

    }
}
