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
 * Test POJO class for HealthMonitor.
 *
 * @see HealthMonitor
 */
public class TestHealthMonitor implements HealthMonitor, HealthServiceCallBack {

    private boolean healthy;
    private boolean needsRestartOnFailure;
    private boolean enabled;
    private String monitorName = "TestHealthMonitor";
    private String metricName = "TestHealthMonitorGuage";

    @Override
    public boolean isHealthy(boolean forceHealthCheck) {
        return healthy;
    }

    @Override
    public boolean needsRestartOnFailure() {
        return needsRestartOnFailure;
    }

    @Override
    public boolean performRestart() {
        return false;
    }

    public void setNeedsRestartOnFailure(boolean needsRestartOnFailure) {
        this.needsRestartOnFailure = needsRestartOnFailure;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String monitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    @Override
    public String metricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

}
