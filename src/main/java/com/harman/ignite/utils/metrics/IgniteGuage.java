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

import java.util.Objects;

/**
 * Wrapper around Prometheus Guage.
 *
 * @author avadakkootko
 */
public abstract class IgniteGuage {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteGuage.class);

    private Gauge igniteGuageMetric;
    
    /**
     * Set the metric's value.

     * @param value The value to set.
     * @param labelValues Label values to set the value for.
     */
    public void set(double value, String... labelValues) {
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        synchronized (this.igniteGuageMetric) {
            igniteGuageMetric.labels(labelValues).set(value);
        }
    }

    /**
     * Get the value of the guage.
     *
     * @param labelValues label values
     * @return The value of the guage.
     */
    public double get(String... labelValues) {
        double value = 0;
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        value = igniteGuageMetric.labels(labelValues).get();
        return value;
    }

    protected void createGuage(String name, String... labels) {
        if (null == igniteGuageMetric) {
            synchronized (this) {
                igniteGuageMetric = Gauge.build(name, name)
                        .labelNames(labels)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
            }
        } else {
            LOGGER.warn("Ignite guage with name : {} and labels {}, already created", name, labels);
        }
    }
    
    // setter and getter for unit tests
    Gauge getIgniteGuageMetric() {
        return this.igniteGuageMetric;
    }
    
    void setIgniteGuageMetric(Gauge igniteGuageMetric) {
        this.igniteGuageMetric = igniteGuageMetric;
    }
}
