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

/** Test POJO class for HealthMonitor with force health check.
 *
 * @see HealthMonitor
 */
public class TestHealthMonitorWithForce implements HealthMonitor {

    private static final int THREE = 3;
    private boolean healthy;
    private boolean needsRestartOnFailure;
    private boolean enabled;
    private String monitorName = "TestHealthMonitorWithForce";
    private String metricName = "TestHealthMonitorWithForceGuage";
    @SuppressWarnings("checkstyle:MemberName")
    private int i = 0;

    @Override
    public boolean isHealthy(boolean forceHealthCheck) {
        if (i > THREE) {
            return true;
        }
        if (forceHealthCheck) {
            i++;
        }
        return false;
    }

    @Override
    public String monitorName() {
        return monitorName;
    }

    @Override
    public boolean needsRestartOnFailure() {
        return needsRestartOnFailure;
    }

    @Override
    public String metricName() {
        return metricName;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setNeedsRestartOnFailure(boolean needsRestartOnFailure) {
        this.needsRestartOnFailure = needsRestartOnFailure;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

}
