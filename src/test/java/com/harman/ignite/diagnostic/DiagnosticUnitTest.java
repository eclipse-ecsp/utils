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

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteDiagnosticGuage;
import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Test class for DiagnosticService.
 *
 * @see DiagnosticService
 */
public class DiagnosticUnitTest {
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DiagnosticUnitTest.class);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private DiagnosticService diagnosticService = new DiagnosticService();

    @Mock
    private IgniteDiagnosticGuage diagnosticGuage;

    /**
     * Setup the test.
     */
    @Before
    public void setUp() {
        clearMetrics();
        MockitoAnnotations.initMocks(this);
        diagnosticService.setServiceDiagnosticGuage(diagnosticGuage);
        diagnosticService.setNodeName("localhost");

    }

    /**
     * If two DiagnosticReporters have same name it should throw runtime exception.
     */
    @Test(expected = RuntimeException.class)
    public void testInitTwoReportersWithSameName() {
        final List<DiagnosticReporter> reporters = new ArrayList<>();
        TestDiagnosticReporter reporter1 = new TestDiagnosticReporter();
        reporter1.setEnabled(true);
        reporter1.setMetricName("Metric1");
        reporter1.setReporterName("Reporter1");
        reporters.add(reporter1);

        TestDiagnosticReporter reporter2 = new TestDiagnosticReporter();
        reporter2.setEnabled(true);
        reporter2.setMetricName("Metric1");
        reporter2.setReporterName("Reporter2");
        reporters.add(reporter2);

        diagnosticService.setReporters(reporters);
        diagnosticService.getEnabledReporters();
    }

    /**
     * If a DiagnosticReporter is disabled it should not be added in the list of DiagnosticReporters.
     */
    @Test
    public void testInitWithDisabledReporter() {
        final List<DiagnosticReporter> reporters = new ArrayList<>();
        TestDiagnosticReporter reporter1 = new TestDiagnosticReporter();
        reporter1.setEnabled(true);
        reporter1.setMetricName("Metric1");
        reporter1.setReporterName("Reporter1");
        reporters.add(reporter1);

        TestDiagnosticReporter reporter2 = new TestDiagnosticReporter();
        reporter2.setEnabled(false);
        reporter2.setMetricName("Metric1");
        reporter2.setReporterName("Reporter2");
        reporters.add(reporter2);

        diagnosticService.setReporters(reporters);

        List<DiagnosticReporter> enabledReporters = diagnosticService.getEnabledReporters();
        Assert.assertEquals(1, enabledReporters.size());
        Assert.assertEquals("Metric1", enabledReporters.get(0).getDiagnosticMetricName());
        Assert.assertEquals("Reporter1", enabledReporters.get(0).getDiagnosticReporterName());
    }

    @Test
    public void testMetricPublishing() {
        final List<DiagnosticReporter> reporters = new ArrayList<>();
        TestDiagnosticReporter reporter1 = new TestDiagnosticReporter();
        reporter1.setEnabled(true);
        reporter1.setMetricName("Metric1");
        reporter1.setReporterName("Reporter1");
        DiagnosticData data = new DiagnosticData();
        data.put("Metric1Key1", DiagnosticResult.FAIL);
        data.put("Metric1Key2", DiagnosticResult.PASS);
        data.put("Metric1Key3", DiagnosticResult.FAIL);
        reporter1.setData(data);
        reporters.add(reporter1);

        TestDiagnosticReporter reporter2 = new TestDiagnosticReporter();
        reporter2.setEnabled(true);
        reporter2.setMetricName("Metric2");
        reporter2.setReporterName("Reporter2");
        DiagnosticData data2 = new DiagnosticData();
        data2.put("Metric2Key1", DiagnosticResult.PASS);
        reporter2.setData(data2);
        reporters.add(reporter2);

        diagnosticService.setReporters(reporters);
        diagnosticService.triggerDiagnosis();

        Mockito.verify(diagnosticGuage, Mockito.times(1))
                .set(DiagnosticResult.FAIL.getValue(), "localhost", "Metric1", "Metric1Key1");
        Mockito.verify(diagnosticGuage, Mockito.times(1))
                .set(DiagnosticResult.PASS.getValue(), "localhost", "Metric1", "Metric1Key2");
        Mockito.verify(diagnosticGuage, Mockito.times(1))
                .set(DiagnosticResult.FAIL.getValue(), "localhost", "Metric1", "Metric1Key3");
        Mockito.verify(diagnosticGuage, Mockito.times(1))
                .set(DiagnosticResult.PASS.getValue(), "localhost", "Metric2", "Metric2Key1");

    }

    /**
     * Clear all the metrics.
     */
    public void clearMetrics() {
        Enumeration<MetricFamilySamples> mfsEnumerator = CollectorRegistry.defaultRegistry.metricFamilySamples();
        while (mfsEnumerator.hasMoreElements()) {
            MetricFamilySamples mfs = mfsEnumerator.nextElement();
            if (mfs.samples != null) {
                mfs.samples.clear();
            }
        }
    }

}
