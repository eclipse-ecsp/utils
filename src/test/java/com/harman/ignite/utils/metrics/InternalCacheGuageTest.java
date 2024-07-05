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
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

/** Test class for InternalCacheGuage.
 *
 * @see InternalCacheGuage
 */
public class InternalCacheGuageTest {

    private final InternalCacheGuage cacheGuage = new InternalCacheGuage();
    private static final double TEN = 10.0;

    @Test
    public void testSetupIfMetricsEnabled() {
        ReflectionTestUtils.setField(cacheGuage, "internalMetricsEnabled", true);
        ReflectionTestUtils.setField(cacheGuage, "prometheusEnabled", true);

        cacheGuage.setup();
        Gauge guage = cacheGuage.getIgniteGuageMetric();
        Assert.assertNotNull(guage);

        List<MetricFamilySamples> samples = guage.collect();
        for (MetricFamilySamples familySample : samples) {
            Assert.assertEquals("internal_cache_size_metric", familySample.name);
        }
    }

    @Test
    public void testSet() {
        //Below code is to avoid "Collector already registered the metric..." error.
        CollectorRegistry.defaultRegistry.clear();
        ReflectionTestUtils.setField(cacheGuage, "internalMetricsEnabled", true);
        ReflectionTestUtils.setField(cacheGuage, "prometheusEnabled", true);

        cacheGuage.setup();
        cacheGuage.set(TEN, "test_cache", "test_svc", "test_node", "0_1");
        Gauge guage = cacheGuage.getIgniteGuageMetric();

        List<MetricFamilySamples> samples = guage.collect();
        for (MetricFamilySamples familySample : samples) {
            for (Sample sample : familySample.samples) {
                Assert.assertEquals(TEN, sample.value, 0.0);
            }
        }
    }
}
