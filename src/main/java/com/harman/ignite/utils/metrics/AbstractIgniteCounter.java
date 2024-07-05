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
import io.prometheus.client.Counter;

/**
 * Abstract class for creating prometheus counter metric.
 */
public abstract class AbstractIgniteCounter {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteCounter.class);

    private Counter counter;

    private boolean isInitialized;

    protected void createCounter(String name, String help, String... labels) {

        if (null == counter) {
            synchronized (this) {
                counter = Counter.build().name(name).help(help).labelNames(labels).register();
            }
        }

        if (null != counter) {
            isInitialized = true;
            LOGGER.info("Created prometheus counter metric with name : {}", name);
        } else {
            LOGGER.warn("Error creating prometheus counter metric with name : {}", name);
        }
    }

    /**
     * Increment the counter by 1.
     *
     * @param labelValues label values
     */
    public void inc(String... labelValues) {
        if (isInitialized) {
            synchronized (counter) {
                counter.labels(labelValues).inc();
            }
        }
    }

    /** Increment the counter by the given amount.
     *
     * @param value       The value to increment the counter by.
     * @param label values
     */
    public void inc(double value, String ... label) {
        if (isInitialized) {
            synchronized (counter) {
                counter.labels(label).inc(value);
            }
        }
    }

    /** Get the value of the counter.
     *
     * @param labelValues label values
     * @return The value of the counter.
     */
    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
            synchronized (counter) {
                val = counter.labels(labelValues).get();
            }
        }
        return val;
    }

    /**
     * Remove the counter from the registry it was registered with.
     */
    public void clear() {
        if (isInitialized) {
            synchronized (counter) {
                counter.clear();
            }
        }
    }

    //Below setters/getters are just for test cases
    public Counter getCounter() {
        return this.counter;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

}
