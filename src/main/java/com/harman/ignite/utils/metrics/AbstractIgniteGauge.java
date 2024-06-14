package com.harman.ignite.utils.metrics;


import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public abstract class AbstractIgniteGauge {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteGauge.class);

    private Gauge guage;

    private boolean isInitialized;

    protected void createGauge(String name, String help, String... labels) {
        if (null == guage) {
        	synchronized (this) {
                guage = Gauge.build(name, name)
                        .labelNames(labels)
                        .help(help)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
        	}
        }

        if (null != guage) {
            isInitialized = true;
            LOGGER.info("Created prometheus gauge metric with name : {}", name);
        }
        else {
            LOGGER.warn("Error creating prometheus guage metric with name : {}", name);
        }

    }

    public void inc(String... labelValues) {
        if (isInitialized) {
        	synchronized (guage) {
                guage.labels(labelValues).inc();
        	}
        }
    }

    public void inc(double value, String... labelValues) {
        if (isInitialized) {
        	synchronized (guage) {
                guage.labels(labelValues).inc(value);
        	}
        }
    }

    public void dec(String... labelValues) {
        if (isInitialized) {
        	synchronized (guage) {
                guage.labels(labelValues).dec();
        	}
        }
    }


    public void dec(double value, String... labelValues) {
        if (isInitialized) {
        	synchronized (guage) {
                guage.labels(labelValues).dec(value);
        	}
        }
    }

    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
        	synchronized (guage) {
        		val = guage.labels(labelValues).get();
        	}
        }
        return val;
    }

    public void set(double value, String... labelValues) {
        if (isInitialized) {
        	synchronized (guage) {
        		guage.labels(labelValues).set(value);
        	}
        }
    }

    public void clear() {
        if (isInitialized) {
        	synchronized (guage) {
                guage.clear();
        	}
        }
    }
    
    Gauge getGuage() { return this.guage; }
    
    boolean getIsInitialized() { return this.isInitialized; }
}
