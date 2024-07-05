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
 * This interface is used to report the diagnostic data.
 *
 * @author avadakkootko
 */
public interface DiagnosticReporter {

    public DiagnosticData getDiagnosticData();

    /**
     * This method is used to name of the DiagnosticReporter.
     *
     * @return name of the DiagnosticReporter
     */
    public String getDiagnosticReporterName();

    /**
     * This method is used to return the name of the DiagnosticMetric.
     *
     * @return name of the DiagnosticMetric
     */
    public String getDiagnosticMetricName();

    /**
     * This method is used to check if the DiagnosticReporter is enabled.
     *
     * @return is DiagnosticReporter enabled or not
     */
    public boolean isDiagnosticReporterEnabled();

}
