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
import io.prometheus.client.Counter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Test class for IgniteErrorCounter.
 *
 * @see IgniteErrorCounter
 */
public class IgniteErrorCounterTest {

    @InjectMocks
    private IgniteErrorCounter igniteCounter;
    private static final double FIVE = 5.0;
    private final String name = "test_count";
    private final String[] labels = { "node", "taskId", "IllegalArgumentException" };

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollectorRegistry.defaultRegistry.clear();
    }

    @Test
    public void testCreateCounter() {
        igniteCounter.createCounter(name, name, labels);

        Counter counter = igniteCounter.getCounter();
        List<MetricFamilySamples> samples = counter.collect();
        Assert.assertTrue(igniteCounter.isInitialized());
        for (MetricFamilySamples sample : samples) {
            Assert.assertEquals("error_count", sample.name);
        }

    }

    @Test
    public void testIncErrorCounter() {
        //create a counter with initial metric value as 0.0
        igniteCounter.createCounter(name, name, labels);
        igniteCounter.setNodeName("node");
        //increment the metric value of the counter with labels = {"node","taskId","IllegalArgumentException"} by 1
        igniteCounter.inc(labels);

        Assert.assertEquals(1.0, igniteCounter.get(labels), 0.0);
    }

    @Test
    public void testIncErrorCounterBySomeValue() {
        //create a counter with initial metric value as 0.0
        igniteCounter.createCounter(name, name, labels);
        igniteCounter.setNodeName("node");
        //increment the metric value of the counter with labels = {"node","taskId","IllegalArgumentException"} by 5
        igniteCounter.inc(FIVE, labels);

        //since initial value was 0.0 and we incremented the metric value by 5.0, hence asserting as 5.0
        Assert.assertEquals(FIVE, igniteCounter.get(labels), 0.0);
    }

    @Test
    public void testClearCounter() {
        igniteCounter.createCounter(name, name, labels);
        igniteCounter.setNodeName("node");
        igniteCounter.inc(FIVE, labels);

        Assert.assertEquals(FIVE, igniteCounter.get(labels), 0.0);
        igniteCounter.clear();
        Assert.assertEquals(0.0, igniteCounter.get(labels), 0.0);
    }
}
