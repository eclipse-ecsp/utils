package com.harman.ignite.utils.metrics;


import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

import io.prometheus.client.Counter;

public abstract class AbstractIgniteCounter {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteCounter.class);

    private Counter counter;

    private boolean isInitialized;

    protected void createCounter(String name, String help, String... labels) {

        if (null == counter) {
        	synchronized (this) {
	            counter = Counter.build()
	                    .name(name)
	                    .help(help)
	                    .labelNames(labels)
	                    .register();
        	}
        }

        if (null != counter) {
            isInitialized = true;
            LOGGER.info("Created prometheus counter metric with name : {}", name);
        }
        else {
            LOGGER.warn("Error creating prometheus counter metric with name : {}", name);
        }
    }

    public void inc(String... labelValues) {
        if (isInitialized) {
        	synchronized (counter) {
        		counter.labels(labelValues).inc();
        	}
        }
    }

    public void inc(double value,String ...label) {
        if (isInitialized) {
        	synchronized (counter) {
                counter.labels(label).inc(value);
        	}
        }
    }

    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
        	synchronized (counter) {
        		val = counter.labels(labelValues).get();
        	}
        }
        return val;
    }

    public void clear() {
        if (isInitialized) {
        	synchronized (counter) {
                counter.clear();
        	}
        }
    }
    
    //Below setters/getters are just for test cases
    public Counter getCounter() { return this.counter; } 
    
    public boolean isInitialized() { return isInitialized; }

}
