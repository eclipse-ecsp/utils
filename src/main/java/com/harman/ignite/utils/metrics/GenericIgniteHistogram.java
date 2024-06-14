package com.harman.ignite.utils.metrics;

/**
 * Histogram for Ignite
 * 
 * @author ssasidharan
 *
 */
public class GenericIgniteHistogram extends AbstractIgniteHistogram {

    public GenericIgniteHistogram(String name, String help, double[] buckets, String... labelNames) {
        createHistogram(name, help, buckets, labelNames);
    }

}
