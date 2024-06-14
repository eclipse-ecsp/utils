package com.harman.ignite.diagnostic;

/**
 * 
 * @author avadakkootko
 *
 */
public enum DiagnosticResult {
    /**
     * Used by diagnostic metric to report success
     */
    PASS {
        @Override
        public double getValue() {
            return 1.0;
        }
    },
    /**
     * Used by diagnostic metric to report an issue
     */
    FAIL {
        @Override
        public double getValue() {
            return 0.0;
        }
    };

    public abstract double getValue();
}
