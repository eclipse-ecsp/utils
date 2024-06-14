package com.harman.ignite.diagnostic;

/**
 * 
 * @author avadakkootko
 *
 */
public interface DiagnosticReporter {

    public DiagnosticData getDiagnosticData();

    /**
     * 
     * @return name of the DiagnosticReporter
     */
    public String getDiagnosticReporterName();

    /**
     * 
     * @return name of the DiagnosticMetric
     */
    public String getDiagnosticMetricName();

    /**
     * 
     * @return is DiagnosticReporter enabled or not
     */
    public boolean isDiagnosticReporterEnabled();

}
