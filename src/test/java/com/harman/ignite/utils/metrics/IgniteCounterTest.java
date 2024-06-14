package com.harman.ignite.utils.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class IgniteCounterTest {
	String name = "test_counter";
	String[] labels = {"node","taskId"}; 
	
	private TestCounter testCounter = new TestCounter();
	
	@Before
	public void setup() {
		CollectorRegistry.defaultRegistry.clear();
	}
	
	@Test
	public void testCreateCounter() {
		testCounter.createCounter(name, labels);
		Counter counter = testCounter.getCounter();
		List<MetricFamilySamples> samples = counter.collect();
		
		samples.forEach(sample -> {
			Assert.assertEquals(name, sample.name);
		});
		
		//again trying to create counter that's already created, won't do anything
		testCounter.createCounter(name, labels);
	}
	
	@Test
	public void testInc() {
		testCounter.createCounter(name, labels);
		Assert.assertEquals(0.0,testCounter.get(labels),0);
		testCounter.inc(labels);
		Assert.assertEquals(1.0,testCounter.get(labels),0);
	}
	public class TestCounter extends IgniteCounter{
		
	}
}
