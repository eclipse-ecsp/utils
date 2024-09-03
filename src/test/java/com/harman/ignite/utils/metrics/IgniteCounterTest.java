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

import java.util.List;

/**
 * Test class for TestCounter.
 *
 * @see TestCounter
 */
public class IgniteCounterTest {
    String name = "test_counter";
    String[] labels = { "node", "taskId" };

    private final TestCounter testCounter = new TestCounter();

    @Before
    public void setup() {
        CollectorRegistry.defaultRegistry.clear();
    }

    @Test
    public void testCreateCounter() {
        testCounter.createCounter(name, labels);
        Counter counter = testCounter.getCounter();
        List<MetricFamilySamples> samples = counter.collect();

        samples.forEach(sample -> {
            Assert.assertEquals(name, sample.name);
        });

        //again trying to create counter that's already created, won't do anything
        testCounter.createCounter(name, labels);
    }

    @Test
    public void testInc() {
        testCounter.createCounter(name, labels);
        Assert.assertEquals(0.0, testCounter.get(labels), 0.0);
        testCounter.inc(labels);
        Assert.assertEquals(1.0, testCounter.get(labels), 0.0);
    }

    /** Test class for IgniteCounter.
     *
     * @see IgniteCounter
     */
    public static class TestCounter extends IgniteCounter {

    }
}
