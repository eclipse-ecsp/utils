package com.harman.ignite.diagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harman.ignite.healthcheck.InvalidMetricNamingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteDiagnosticGuage;

/**
 * The objective of DiagnosticService is to do reporting in Graylog and publish
 * metrics in prometheus about diagnostic data of a service.
 * 
 * @author avadakkootko
 *
 */
@Component
public class DiagnosticService {
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DiagnosticService.class);

    @Autowired(required = false)
    private List<DiagnosticReporter> reporters;

    @Value("${NODE_NAME:localhost}")
    private String nodeName;

    @Autowired
    private IgniteDiagnosticGuage serviceDiagnosticGuage;

    public void triggerDiagnosis() {
        List<DiagnosticReporter> enabledDiagnosticReporters = getEnabledReporters();
        for (DiagnosticReporter reporter : enabledDiagnosticReporters) {
            String metricName = reporter.getDiagnosticMetricName();
            DiagnosticData data = reporter.getDiagnosticData();
            for (Map.Entry<String, DiagnosticResult> entry : data.entrySet()) {
                String key = entry.getKey();
                DiagnosticResult result = data.get(key);
                serviceDiagnosticGuage.set(result.getValue(), nodeName, metricName, key);
                if (result == DiagnosticResult.PASS) {
                    LOGGER.info("Diagnosis for metric name {} and key {} is {}", metricName, key, result);
                }
                else {
                    LOGGER.warn("Diagnosis for metric name {} and key {} is {}", metricName, key, result);
                }

            }

        }
    }

    protected List<DiagnosticReporter> getEnabledReporters() {
        Map<String, String> metricToReporterMapping = new HashMap<>();
        List<DiagnosticReporter> enabledDiagnosticReporters = new ArrayList<>();
        if (reporters != null) {
            for (DiagnosticReporter reporter : reporters) {
                if (reporter.isDiagnosticReporterEnabled()) {
                    String reporterName = reporter.getDiagnosticReporterName();
                    String metricName = reporter.getDiagnosticMetricName();
                    // If two reporters have metrics with same name throw
                    // exception
                    if (metricToReporterMapping.containsKey(metricName)) {
                        LOGGER.error("Two diagnostic reporters {} and {} cannot have same MetricName :", reporterName,
                                metricToReporterMapping.get(metricName), metricName);
                        throw new InvalidMetricNamingException(
                                "Two diagnostic reporters " + reporterName + "and " + metricToReporterMapping.get(metricName)
                                        + "cannot have same MetricName " + metricName);
                    }
                    LOGGER.info("Diagnostic reporter {}, is enabled.", reporterName);
                    metricToReporterMapping.put(metricName, reporterName);
                    enabledDiagnosticReporters.add(reporter);
                }
                else {
                    LOGGER.info("Diagnostic reporter {}, is disabled.", reporter.getDiagnosticReporterName());
                }
            }
        }
        return enabledDiagnosticReporters;
    }

    // for testing
    void setReporters(List<DiagnosticReporter> reporters) {
        this.reporters = reporters;
    }

    void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    void setServiceDiagnosticGuage(IgniteDiagnosticGuage serviceDiagnosticGuage) {
        this.serviceDiagnosticGuage = serviceDiagnosticGuage;
    }

}
