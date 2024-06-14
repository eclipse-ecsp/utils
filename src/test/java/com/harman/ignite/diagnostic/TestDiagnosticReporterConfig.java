package com.harman.ignite.diagnostic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.harman.ignite" })
public class TestDiagnosticReporterConfig {

    @Bean
    public PropertyDiagnosticReporterTest testPropertyDiagnosticReporter() {
        return new PropertyDiagnosticReporterTest();
    }
}
