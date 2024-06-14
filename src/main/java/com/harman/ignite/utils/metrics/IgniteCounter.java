package com.harman.ignite.utils.metrics;

import java.util.Objects;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

/**
 * Wrapper around Promethus Counter
 * 
 * @author sanketadhikari
 *
 */
public abstract class IgniteCounter {

	private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteCounter.class);

	private  Counter counter;

	public void inc(String... labelValues) {
		Objects.requireNonNull(counter, "IgniteCounter is not initialized");
		synchronized (this.counter) {
			counter.labels(labelValues).inc();
		}
	}

	public double get(String... labelValues) {
		double value = 0;
		Objects.requireNonNull(counter, "IgniteCounter is not initialized");
		value = counter.labels(labelValues).get();
		return value;
	}

	protected void createCounter(String name, String... labels) {
		if (null == counter) {
			synchronized (this) {
				counter = Counter.build(name, name).labelNames(labels).register(CollectorRegistry.defaultRegistry);
				LOGGER.info("Created ignite counter with name : {} and labels {}", name, labels);
			}
		} else {
			LOGGER.warn("Ignite counter with name : {} and labels {}, already created", name, labels);
		}
	}
	
	Counter getCounter() { return this.counter; }
}
