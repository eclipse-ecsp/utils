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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** Test configuration for initializing TestHealthMonitor and TestHealthMonitorWithForce beans.
 *
 * @see HealthServiceStateTest
 */
@Configuration
@ComponentScan(basePackages = { "com.harman.ignite" })
public class TestHealthServiceConfig {

    /** Bean for TestHealthMonitor.
     *
     * @return TestHealthMonitor
     */
    @Bean
    public TestHealthMonitor testHealthMonitor() {
        TestHealthMonitor monitor1 = new TestHealthMonitor();
        monitor1.setEnabled(true);
        return monitor1;
    }

    /** Bean for TestHealthMonitor.
     *  This should be skipped at the time of init of healthservice as enabled is false.
     *
     * @return TestHealthMonitor
     */
    @Bean
    public TestHealthMonitor testHealthMonitor2() {
        TestHealthMonitor monitor1 = new TestHealthMonitor();
        monitor1.setEnabled(false);
        return monitor1;
    }

    /**
     * Bean for TestHealthMonitorWithForce.
     *
     * @return TestHealthMonitorWithForce
     */
    @Bean
    public TestHealthMonitorWithForce testHealthMonitorWithForce() {
        TestHealthMonitorWithForce monitor1 = new TestHealthMonitorWithForce();
        monitor1.setEnabled(true);
        return monitor1;
    }

}
