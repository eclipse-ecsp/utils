package com.harman.ignite.healthcheck;

/**
 * Custom exception for duplicate metric name.
 */
public class InvalidMetricNamingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidMetricNamingException(String s) {
        super(s);
    }
}
