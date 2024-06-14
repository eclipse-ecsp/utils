package com.harman.ignite.healthcheck;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.harman.ignite" })
public class TestHealthServiceConfig {

    @Bean
    public TestHealthMonitor testHealthMonitor() {
        TestHealthMonitor monitor1 = new TestHealthMonitor();
        monitor1.setEnabled(true);
        return monitor1;
    }

    // This shoudl be skipped at the time of init of healthservice as enabled is
    // false
    @Bean
    public TestHealthMonitor testHealthMonitor2() {
        TestHealthMonitor monitor1 = new TestHealthMonitor();
        monitor1.setEnabled(false);
        return monitor1;
    }

    @Bean
    public TestHealthMonitorWithForce testHealthMonitorWithForce() {
        TestHealthMonitorWithForce monitor1 = new TestHealthMonitorWithForce();
        monitor1.setEnabled(true);
        return monitor1;
    }

}
