package com.harman.ignite.utils.metrics;

/**
 * Gauge for Ignite
 * 
 * @author ssasidharan
 *
 */
public class GenericIgniteGauge extends AbstractIgniteGauge {
    public GenericIgniteGauge(String name, String help, String... labels) {
        createGauge(name, help, labels);
    }
}
