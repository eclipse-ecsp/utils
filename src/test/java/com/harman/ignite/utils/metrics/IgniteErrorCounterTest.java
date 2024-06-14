package com.harman.ignite.utils.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class IgniteErrorCounterTest {
	
	@InjectMocks
	private IgniteErrorCounter igniteCounter;

	private String name = "test_count";
	private String[] labels = {"node","taskId","IllegalArgumentException"};
	
	@Before
	public void setup() { 
		MockitoAnnotations.initMocks(this); 
		CollectorRegistry.defaultRegistry.clear();
	}
	
	@Test
	public void testCreateCounter() {
		igniteCounter.createCounter(name, name, labels);
		
		Counter counter = igniteCounter.getCounter();
		List<MetricFamilySamples> samples = counter.collect();
		Assert.assertTrue(counter != null && igniteCounter.isInitialized());
		for(MetricFamilySamples sample: samples) {
			Assert.assertEquals(sample.name,("error_count"));
		}
		
	}
	
	@Test
	public void testIncErrorCounter() {
		//create a counter with initial metric value as 0.0
		igniteCounter.createCounter(name, name, labels);
		igniteCounter.setNodeName("node");
		//increment the metric value of the counter with labels = {"node","taskId","IllegalArgumentException"} by 1
		igniteCounter.inc(labels);
		
		Assert.assertEquals(1.0, igniteCounter.get(labels),0);
	}
	
	@Test
	public void testIncErrorCounterBySomeValue() {
		//create a counter with initial metric value as 0.0
		igniteCounter.createCounter(name, name, labels);
		igniteCounter.setNodeName("node");
		//increment the metric value of the counter with labels = {"node","taskId","IllegalArgumentException"} by 5
		igniteCounter.inc(5.0,labels);
		
		//since initial value was 0.0 and we incremented the metric value by 5.0, hence asserting as 5.0
		Assert.assertEquals(5.0,igniteCounter.get(labels),0);
	}
	
	@Test
	public void testClearCounter() {
		igniteCounter.createCounter(name, name, labels);
		igniteCounter.setNodeName("node");
		igniteCounter.inc(5.0,labels);
		
		Assert.assertEquals(5.0,igniteCounter.getCounter().labels(labels).get(),0);
		igniteCounter.clear();
		Assert.assertEquals(0.0,igniteCounter.getCounter().labels(labels).get(),0);
	}
}
