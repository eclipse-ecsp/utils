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

import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.Histogram;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * This class tests basic functionality of Histogram.
 * <p>Key point to note while testing:</p>
 *
 * <ul>
 *     <li>Create Histogram.</li>
 *     <li>Do observe.</li>
 *     <li>Check data on Collector.</li>
 * </ul>
 *
 * @author vkoul
 *
 */

public class TestGenericIgniteHistogram {
    private static final double TWO = 2.0;
    private static final double THREE = 3.0;
    private static final int TEN = 10;
    private static final int HUNDRED = 100;
    private static final long TWO_THOUSAND = 2000L;
    private static final long THREE_THOUSAND = 3000L;
    private static final double HUNDRED_DOUBLE = 100.0;

    @SuppressWarnings("unchecked")
    @Test
    public void testObserveExtended() throws Exception {
        GenericIgniteHistogram genericIgniteHistogram = new GenericIgniteHistogram(
                "testHistogram",
                "testHistogramDoubleHelp",
                new double[] { HUNDRED },
                "testHistogramDoubleLabel");
        String[] labels = { "testHistogramDoubleLabel" };
        Assert.assertEquals("Test invocation", genericIgniteHistogram.observeExtended(new Callable() {
            @Override
            public String call() {
                return "Test invocation";
            }
        }, labels));
    }

    /**
     * This test cases tests observe(Double...) functionality.
     */
    @Test
    public void testObserveDouble() {
        GenericIgniteHistogram genericIgniteHistogram = new GenericIgniteHistogram(
                "testHistogramDouble",
                "testHistogramDoubleHelp",
                new double[] { HUNDRED },
                "testHistogramDoubleLabel");
        Histogram hg = genericIgniteHistogram.getHistogram();
        // Observe a value.
        genericIgniteHistogram.observe(HUNDRED, "testHistogramDoubleLabel");
        // Collect all metric from this Collector (Histogram).
        List<MetricFamilySamples> lists = hg.collect();

        // Should be one, since we observed only once.
        Assert.assertEquals(1, lists.size());
        for (MetricFamilySamples mfs : lists) {
            // Sanity check.
            Assert.assertEquals("testHistogramDoubleHelp", mfs.help);
            Assert.assertEquals("testHistogramDouble", mfs.name);
            Assert.assertEquals(Collector.Type.HISTOGRAM, mfs.type);

            // Collect sample.
            List<Sample> samples = mfs.samples;

            // Iterate through all Sample and do sanity check.
            // Refer to io.prometheus.client.Collector and
            // io.prometheus.client.Collector.MetricFamilySamples.Sample for
            // full details.
            for (Sample sample : samples) {
                switch (sample.name) {
                    // Test _count for metric "testHistogram"
                    case "testHistogramDouble_count":
                        Assert.assertEquals(1.0, sample.value, 0);
                        break;
                    // Test _sum for metric "testHistogram"
                    case "testHistogramDouble_sum":
                        Assert.assertEquals(HUNDRED_DOUBLE, sample.value, 0);
                        break;
                    default:
                        // do nothing
                }
            }
        }
    }

    /**
     * This test cases tests observe(Supplier...) functionality.
     */
    @Test
    public void testObserveSupplier() {
        GenericIgniteHistogram genericIgniteHistogram = new GenericIgniteHistogram(
                "testHistogramSupplier",
                "testHistogramSupplierHelp",
                new double[] { HUNDRED },
                "testHistogramSupplierLabel");
        Histogram hg = genericIgniteHistogram.getHistogram();
        genericIgniteHistogram.observe(new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    // Forcefully sleeping for 2 seconds
                    Thread.sleep(TWO_THOUSAND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return TEN;
            }
        }, "testHistogramSupplierLabel");

        List<MetricFamilySamples> lists = hg.collect();
        Assert.assertEquals(1, lists.size());
        for (MetricFamilySamples mfs : lists) {
            Assert.assertEquals("testHistogramSupplierHelp", mfs.help);
            Assert.assertEquals("testHistogramSupplier", mfs.name);
            Assert.assertEquals(Collector.Type.HISTOGRAM, mfs.type);

            List<Sample> samples = mfs.samples;
            for (Sample sample : samples) {
                switch (sample.name) {
                    case "testHistogramSupplier_count":
                        Assert.assertEquals(1.0, sample.value, 0);
                        break;
                    case "testHistogramSupplier_sum":
                        System.out.println("sample.value is ---> " + sample.value);
                        // This means it took between 2-3 seconds. 2 seconds minimum
                        // since we forcefully slept for 2 seconds.
                        Assert.assertEquals(TWO, sample.value, 1);
                        break;
                    default:
                        // do nothing
                }
            }
        }
    }

    /**
     * This test cases tests observe(Runnable...) functionality.
     */
    @Test
    public void testObserveRunnable() {
        GenericIgniteHistogram genericIgniteHistogram = new GenericIgniteHistogram(
                "testHistogramRunnable",
                "testHistogramRunnableHelp",
                new double[] { HUNDRED },
                "testHistogramRunnableLabel");
        Histogram hg = genericIgniteHistogram.getHistogram();
        genericIgniteHistogram.observe(new Runnable() {
            @Override
            public void run() {
                try {
                    // Forcefully sleeping for 3 seconds
                    Thread.sleep(THREE_THOUSAND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "testHistogramRunnableLabel");

        List<MetricFamilySamples> lists = hg.collect();
        Assert.assertEquals(1, lists.size());
        for (MetricFamilySamples mfs : lists) {
            Assert.assertEquals("testHistogramRunnableHelp", mfs.help);
            Assert.assertEquals("testHistogramRunnable", mfs.name);
            Assert.assertEquals(Collector.Type.HISTOGRAM, mfs.type);

            List<Sample> samples = mfs.samples;
            for (Sample sample : samples) {
                switch (sample.name) {
                    case "testHistogramRunnable_count":
                        Assert.assertEquals(1.0, sample.value, 0);
                        break;
                    case "testHistogramRunnable_sum":
                        System.out.println("sample.value is ---> " + sample.value);
                        // This means it took between 3-4 seconds. 3 seconds minimum
                        // since we forcefully slept for 3 seconds.
                        Assert.assertEquals(THREE, sample.value, 1);
                        break;
                    default:
                        // do nothing
                }
            }
        }
    }
}