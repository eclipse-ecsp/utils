package com.harman.ignite.utils.metrics;

import org.springframework.stereotype.Component;

/**
 * IgniteHealthGuage will be used by various HealthMonitors for publishing their
 * respective health status to prometheus
 * 
 * @author avadakkootko
 * 
 */
@Component
public class IgniteHealthGuage extends IgniteGuage {

    public IgniteHealthGuage() {
        createGuage("service_health_metric", "node", "monitorname");
    }
}
