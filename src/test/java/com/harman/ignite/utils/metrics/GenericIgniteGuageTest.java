package com.harman.ignite.utils.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class GenericIgniteGuageTest {

	private String name = null;
	private String[] labels = {"node","taskId"};
	
	@InjectMocks
	IgniteGuageTest guage = new IgniteGuageTest();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
		CollectorRegistry.defaultRegistry.clear();
		System.out.println("In before");
	}
	
	@Test
	public void testCreateGuage() {
		guage.createGauge("test_guage", "test_guage", labels);
		Gauge newGuage = guage.getGuage();
		List<MetricFamilySamples> samples = newGuage.collect();
		
		Assert.assertTrue(guage.getIsInitialized());
		samples.forEach(sample -> {
			Assert.assertEquals("test_guage", sample.name);
		});
		
		guage.createGauge(name, name, labels);
	}
	
	@Test
	public void testInc() {
		guage.createGauge("test_guage1", "test_guage1", labels);
		double value = guage.get(labels);
		
		Assert.assertEquals(0.0,value,0);
		
		guage.inc(labels);
		value = guage.get(labels);
		
		Assert.assertEquals( 1.0,value,0);
	}
	
	@Test
	public void testIncBySomeValue() {
		guage.createGauge("test_guage2", "test_guage2", labels);
		Assert.assertEquals(0.0,guage.get(labels),0);
		
		guage.inc(5.0, labels);
		
		Assert.assertEquals(5.0,guage.get(labels),0);
	}
	
	@Test
	public void testDecBySomeValue() {
		guage.createGauge("test_guage3", "test_guage3", labels);
		guage.inc(10.0, labels);
		
		Assert.assertEquals(10.0,guage.get(labels),0);
		
		guage.dec(3.0, labels);
		
		Assert.assertEquals(7.0,guage.get(labels),0);
	}
	
	@Test
	public void testDec() {
		guage.createGauge("test_guage4", "test_guage4", labels);
		guage.inc(10.0, labels);
		
		Assert.assertEquals(10.0,guage.get(labels),0);
		
		guage.dec(labels);
		
		Assert.assertEquals(9.0,guage.get(labels),0);
	}
	
	@Test
	public void testSetGaugeValue() {
		guage.createGauge("test_guage5", "test_guage5", labels);
		
		Assert.assertEquals(0.0,guage.get(labels),0);
		
		guage.set(3.5, labels);
		
		Assert.assertEquals(3.5,guage.get(labels),0);
	}
	
	@Test
	public void testClear() {
		guage.createGauge("test_guage6", "test_guage6", labels);
		guage.set(7.0, labels);
		
		Assert.assertEquals(7.0,guage.get(labels),0 );
		
		guage.clear();
		
		Assert.assertEquals(0.0,guage.get(labels),0 );
	}
	
	public class IgniteGuageTest extends AbstractIgniteGauge{
		
	}
}
