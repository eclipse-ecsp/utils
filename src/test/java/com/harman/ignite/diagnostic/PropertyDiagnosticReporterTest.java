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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestDiagnosticReporterConfig.class })
@TestPropertySource(locations = { "classpath:property-diagnostic-test.properties", "classpath:property-diagnostic-test-2.properties" })
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

        diagnosticData.entrySet().forEach(entry -> {
            System.out.println("property: " + entry.getKey() + "value: " + entry.getValue());
        });

        Assert.assertEquals(DiagnosticResult.FAIL,diagnosticData.get("mongodb.hosts"));
        Assert.assertEquals(DiagnosticResult.FAIL,diagnosticData.get("mongodb.port"));
        Assert.assertEquals(DiagnosticResult.FAIL,diagnosticData.get("mongodb.username"));
        Assert.assertEquals(DiagnosticResult.FAIL,diagnosticData.get("mongodb.hosts"));
        Assert.assertEquals(DiagnosticResult.PASS,diagnosticData.get("mongodb.pool.max.size"));
        Assert.assertEquals( DiagnosticResult.PASS,diagnosticData.get("mongodb.max.wait.time.ms"));
        Assert.assertEquals( DiagnosticResult.PASS,diagnosticData.get("mongodb.socket.timeout.ms"));
        Assert.assertEquals(DiagnosticResult.PASS,diagnosticData.get("mongodb.max.connections.per.host"));
        Assert.assertEquals( DiagnosticResult.PASS,diagnosticData.get("mongodb.block.threads.allowed.multiplier")
               );
        Assert.assertEquals(DiagnosticResult.PASS,diagnosticData.get("mongodb.read.preference")
                );
        Assert.assertEquals(DiagnosticResult.PASS,diagnosticData.get("morphia.map.packages"));
        Assert.assertEquals( DiagnosticResult.PASS,diagnosticData.get("mongodb.socket.timeout.ms")
               );
        Assert.assertEquals(DiagnosticResult.PASS,diagnosticData.get("morphia.converters.fqn"));
        Assert.assertEquals( DiagnosticResult.FAIL,diagnosticData.get("vault.secret.folder"));
        Assert.assertEquals(DiagnosticResult.FAIL,diagnosticData.get("vault.environment")
                );

    }
}
