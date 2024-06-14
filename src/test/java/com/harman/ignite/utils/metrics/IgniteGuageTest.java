package com.harman.ignite.utils.metrics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

import java.util.List;

public class IgniteGuageTest {
	private String name = "test_guage";
	private String[] labels = {"test_metric","node","monitorname"};
	
	@InjectMocks
	private TestGuage testGuage = new TestGuage();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
		CollectorRegistry.defaultRegistry.clear();
	}
	
	@Test
	public void testCreateGuage() {
		testGuage.createGuage(name, labels);
		Gauge guage = testGuage.getIgniteGuageMetric();
		
		Assert.assertTrue(guage != null && guage instanceof Gauge);
		
		List<MetricFamilySamples> samples = guage.collect();
		for(MetricFamilySamples sample : samples) {
			Assert.assertEquals("test_guage", sample.name);
		}
		
		//trying to create a guage that already exists, it won't create and publish a WARN logger
		testGuage.createGuage(name, labels);
	}
	
	@Test
	public void testSetValueOnGuage() {
		testGuage.createGuage(name, labels);
		Gauge guage = testGuage.getIgniteGuageMetric();
		testGuage.setIgniteGuageMetric(guage);
		testGuage.set(10.5, labels);
		double value = testGuage.get(labels);
		
		Assert.assertEquals(10.5, value, 0);
	}
	
	public class TestGuage extends IgniteGuage{
		
	}
}
