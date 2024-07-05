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

package com.harman.ignite.healthcheck;

/**
 * Contract for health monitors that intend to publish health to the HealthService.
 *
 * @author avadakkootko
 */
public interface HealthMonitor {

    /**
     * This method is used to check if the health monitor is healthy.
     *
     * @param forceHealthCheck if true, then it needs to trigger a forced health check
     * @return true if health monitor is healthy. If forceHealthCheck is true, trigger a forced health check.
     */
    public boolean isHealthy(boolean forceHealthCheck);

    /**
     * This method is used to get name of the health monitor.
     *
     * @return name of the health monitor
     */
    public String monitorName();

    /**
     * This method is used to check if the health monitor is unhealthy and hence service should be restarted.
     *
     * @return true if the health monitor is unhealthy and hence service should be restarted.
     */
    public boolean needsRestartOnFailure();

    /**
     * This method is used to get the name of the ignite gauge.
     *
     * @return name of the ignite gauge
     */
    public String metricName();

    /**
     * This method is used to check if the service is enabled or not.
     *
     * @return if the service is enabled or not
     */
    public boolean isEnabled();

}
