package com.harman.ignite.diagnostic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

/**
 * This class performs the validation of the values of the properties configured
 * against the expected diagnostic value provided.
 * 
 * @author MaKumari
 *
 */
@Component("propertyDiagnostic")
public class PropertyDiagnosticReporterImpl implements DiagnosticReporter {
    private static IgniteLogger igniteLogger = IgniteLoggerFactory.getLogger(PropertyDiagnosticReporterImpl.class);

    @Value("${" + DiagnosticConstants.PROPERTY_DIAGNOSTIC_REPORTER_ENABLED + ": true }")
    private boolean diagnosticPropertyReporterEnabled;

    private static final String DIAGNOSTIC_PROPERTY_METRIC_NAME = "DIAGNOSTIC_PROPERTY_METRIC";
    private static final String DIAGNOSTIC_PROPERTY_REPORTER_NAME = "DIAGNOSTIC_PROPERTY_REPORTER";

    HashMap<String, Object> configurationMap = new HashMap<>();

    @Autowired
    ApplicationContext ctx;

    @Override
    public DiagnosticData getDiagnosticData() {
        StringBuilder passedParms = new StringBuilder().append('\n');
        StringBuilder failedParms = new StringBuilder().append('\n');
        DiagnosticData diagnosticData = new DiagnosticData();
        MutablePropertySources propSources = ((AbstractEnvironment) ctx.getEnvironment()).getPropertySources();
        StreamSupport.stream(propSources.spliterator(), false)
                .filter( EnumerablePropertySource.class::isInstance)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String> stream)
                .filter(diagnosticPropertyName -> diagnosticPropertyName.startsWith(DiagnosticConstants.DIAGNOSTIC_KEYWORD))
                .forEach(diagnosticPropertyName -> {
                    String propertyToBeValidated = diagnosticPropertyName.substring(DiagnosticConstants.DIAGNOSTIC_KEYWORD.length());
                    if (((AbstractEnvironment) ctx.getEnvironment()).getProperty(diagnosticPropertyName)
                            .equals(((AbstractEnvironment) ctx.getEnvironment())
                                    .getProperty(propertyToBeValidated))) {
                        diagnosticData.put(propertyToBeValidated, DiagnosticResult.PASS);
                        passedParms.append(propertyToBeValidated).append('\n');
                    }
                    else {
                        diagnosticData.put(propertyToBeValidated, DiagnosticResult.FAIL);
                        failedParms.append(propertyToBeValidated).append('\n');
                    }
                });
        igniteLogger.debug("Properties that matches diagnostic criteria are : {}", passedParms.toString());
        igniteLogger.warn("Properties that don't matches diagnostic criteria are : {}", failedParms.toString());
        return diagnosticData;
    }

    @Override
    public boolean isDiagnosticReporterEnabled() {
        return diagnosticPropertyReporterEnabled;
    }

    @Override
    public String getDiagnosticReporterName() {
        return DIAGNOSTIC_PROPERTY_REPORTER_NAME;
    }

    @Override
    public String getDiagnosticMetricName() {
        return DIAGNOSTIC_PROPERTY_METRIC_NAME;
    }

}
