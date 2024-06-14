package com.harman.ignite.utils.metrics;

import java.util.Objects;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

/**
 * Wrapper around Promethus Guage
 * 
 * @author avadakkootko
 * 
 */
public abstract class IgniteGuage {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteGuage.class);

    private Gauge igniteGuageMetric;

    public void set(double value, String... labelValues) {
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        synchronized (this.igniteGuageMetric) {
        	igniteGuageMetric.labels(labelValues).set(value);
		}
    }

    public double get(String... labelValues) {
        double value = 0;
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        value = igniteGuageMetric.labels(labelValues).get();
        return value;
    }

    protected void createGuage(String name, String... labels) {
        if (null == igniteGuageMetric) {
            synchronized (this) {
                igniteGuageMetric = Gauge.build(name, name)
                        .labelNames(labels)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
            }
        }
        else {
            LOGGER.warn("Ignite guage with name : {} and labels {}, already created", name, labels);
        }
    }
    
    // setter and getter for unit tests
    Gauge getIgniteGuageMetric() {
    	return this.igniteGuageMetric;
    }
    
    void setIgniteGuageMetric(Gauge igniteGuageMetric) {
    	this.igniteGuageMetric = igniteGuageMetric;
    }
    
}
