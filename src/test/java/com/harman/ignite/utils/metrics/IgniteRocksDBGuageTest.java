package com.harman.ignite.utils.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Gauge;

public class IgniteRocksDBGuageTest {
	@InjectMocks
	private IgniteRocksDBGuage guage = new IgniteRocksDBGuage();
	
	@Test
	public void testSetup() {
		guage.setup();
		Gauge testGuage = guage.getIgniteGuageMetric();
		Assert.assertTrue(testGuage != null && testGuage instanceof Gauge);
		
		List<MetricFamilySamples> samples = testGuage.collect();
		for(MetricFamilySamples familySample : samples) {
			Assert.assertEquals("rocksdb_metric", familySample.name);
		}
	}
}
