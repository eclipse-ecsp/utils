package com.harman.ignite.utils.metrics;

import org.springframework.stereotype.Component;

/**
 * IgniteDiagnosticGuage will be used by various Diagnostic reporters for
 * publishing their respective report to prometheus
 * 
 * @author avadakkootko
 * 
 */
@Component
public class IgniteDiagnosticGuage extends IgniteGuage {

    public IgniteDiagnosticGuage() {
        createGuage("diagnostic_metric", "node", "diagnostic_reporter_name", "diagnostic_reporter_sublabel");
    }
}
