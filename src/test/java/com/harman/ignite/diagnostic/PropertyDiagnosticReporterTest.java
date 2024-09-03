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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for DiagnosticReporter.
 *
 * @see DiagnosticReporter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDiagnosticReporterConfig.class })
@TestPropertySource(
        locations = {"classpath:property-diagnostic-test.properties", "classpath:property-diagnostic-test-2.properties"}
)
public class PropertyDiagnosticReporterTest {
    @Autowired
    @Qualifier("propertyDiagnostic")
    DiagnosticReporter propertyDiagnosticReporterImpl;

    @Before
    public void setUp() {

    }

    @Test
    public void testDiagnosticReporterName() {
        Assert.assertEquals("DIAGNOSTIC_PROPERTY_REPORTER", propertyDiagnosticReporterImpl.getDiagnosticReporterName());
    }

    @Test
    public void testDiagnosticMetricName() {
        Assert.assertEquals("DIAGNOSTIC_PROPERTY_METRIC", propertyDiagnosticReporterImpl.getDiagnosticMetricName());
    }

    @Test
    public void isDiagnosticReporterEnabled() {
        Assert.assertTrue(propertyDiagnosticReporterImpl.isDiagnosticReporterEnabled());

    }

    @Test
    public void testPropertyDiagnosticReporter() {
        DiagnosticData diagnosticData = propertyDiagnosticReporterImpl.getDiagnosticData();

        diagnosticData.forEach((key, value) -> System.out.println("property: " + key + "value: " + value));

        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("mongodb.hosts"));
        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("mongodb.port"));
        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("mongodb.username"));
        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("mongodb.hosts"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.pool.max.size"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.max.wait.time.ms"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.socket.timeout.ms"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.max.connections.per.host"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.block.threads.allowed.multiplier"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.read.preference"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("morphia.map.packages"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("mongodb.socket.timeout.ms"));
        Assert.assertEquals(DiagnosticResult.PASS, diagnosticData.get("morphia.converters.fqn"));
        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("vault.secret.folder"));
        Assert.assertEquals(DiagnosticResult.FAIL, diagnosticData.get("vault.environment"));
    }
}
