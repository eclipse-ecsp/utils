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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Guage for internal cache(s) metric.
 *
 * @author hbadshah
 */
@Component
public class InternalCacheGuage extends IgniteGuage {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(InternalCacheGuage.class);

    @Value("${internal.metrics.enabled:false}")
    private boolean internalMetricsEnabled;

    @Value("${metrics.prometheus.enabled:true}")
    private boolean prometheusEnabled;

    /**
     * Setup the guage metric for internal cache.
     */
    @PostConstruct
    public void setup() {
        if (prometheusEnabled && internalMetricsEnabled) {
            createGuage("internal_cache_size_metric", "cache_type", "svc", "node", "task_id");
            LOGGER.info("Guage metric for internal cache created.");
        }
    }

    @Override
    public void set(double value, String... labels) {
        if (prometheusEnabled && internalMetricsEnabled) {
            super.set(value, labels);
            LOGGER.debug("Published metrics for labels: {} with value: {}", Arrays.asList(labels), value);
        }
    }
}
