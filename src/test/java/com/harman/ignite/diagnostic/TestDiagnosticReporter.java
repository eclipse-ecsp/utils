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

package com.harman.ignite.diagnostic;

/**
 * Test POJO for DiagnosticReporter.
 */
public class TestDiagnosticReporter implements DiagnosticReporter {

    private boolean enabled;
    private String reporterName = "TestDiagnosticReporter";
    private String metricName = "TestDiagnosticReporterGuage";
    private DiagnosticData data = new DiagnosticData();

    @Override
    public boolean isDiagnosticReporterEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getDiagnosticReporterName() {
        return reporterName;
    }

    @Override
    public String getDiagnosticMetricName() {
        return metricName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    @Override
    public DiagnosticData getDiagnosticData() {
        return data;
    }

    public void setData(DiagnosticData data) {
        this.data = data;
    }

}
