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

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

/**
 * Abstract class for creating prometheus gauge metric.
 */
public abstract class AbstractIgniteGauge {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteGauge.class);

    private Gauge guage;

    private boolean isInitialized;

    protected void createGauge(String name, String help, String... labels) {
        if (null == guage) {
            synchronized (this) {
                guage = Gauge.build(name, name)
                        .labelNames(labels)
                        .help(help)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
            }
        }

        if (null != guage) {
            isInitialized = true;
            LOGGER.info("Created prometheus gauge metric with name : {}", name);
        } else {
            LOGGER.warn("Error creating prometheus guage metric with name : {}", name);
        }
    }

    /** Increment the gauge by 1.
     *
     * @param labelValues label values
     */
    public void inc(String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).inc();
            }
        }
    }

    /** Increment the gauge by the given amount.
     *
     * @param value       The value to increment the gauge by.
     * @param labelValues label values
     */
    public void inc(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).inc(value);
            }
        }
    }

    /** Decrement the gauge by 1.
     *
     * @param labelValues label values
     */
    public void dec(String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).dec();
            }
        }
    }

    /** Decrement the gauge by the given amount.
     *
     * @param value       The value to decrement the gauge by.
     * @param labelValues label values
     */
    public void dec(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).dec(value);
            }
        }
    }

    /** Get the value of the gauge.
     *
     * @param labelValues label values
     * @return The value of the gauge.
     */
    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
            synchronized (guage) {
                val = guage.labels(labelValues).get();
            }
        }
        return val;
    }

    /** Set the gauge to the given value.
     *
     * @param value       The value to set the gauge to.
     * @param labelValues label values
     */
    public void set(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).set(value);
            }
        }
    }

    /**
     * Remove the gauge from the registry it was registered with.
     */
    public void clear() {
        if (isInitialized) {
            synchronized (guage) {
                guage.clear();
            }
        }
    }

    Gauge getGuage() {
        return this.guage;
    }

    boolean getIsInitialized() {
        return this.isInitialized;
    }
}
