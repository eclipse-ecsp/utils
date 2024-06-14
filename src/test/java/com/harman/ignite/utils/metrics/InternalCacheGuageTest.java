package com.harman.ignite.utils.metrics;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class InternalCacheGuageTest {

	private InternalCacheGuage cacheGuage = new InternalCacheGuage();
	
	@Test
	public void testSetupIfMetricsEnabled() {
		ReflectionTestUtils.setField(cacheGuage, "internalMetricsEnabled", true);
		ReflectionTestUtils.setField(cacheGuage, "prometheusEnabled", true);
		
		cacheGuage.setup();
		Gauge guage = cacheGuage.getIgniteGuageMetric();
		Assert.assertNotNull(guage );
		
		List<MetricFamilySamples> samples = guage.collect();
		for(MetricFamilySamples familySample : samples) {
			Assert.assertEquals("internal_cache_size_metric", familySample.name);
		}
	}
	
	@Test
	public void testSet() {
		//Below code is to avoid "Collector already registered the metric..." error.
		CollectorRegistry.defaultRegistry.clear();
		ReflectionTestUtils.setField(cacheGuage, "internalMetricsEnabled", true);
		ReflectionTestUtils.setField(cacheGuage, "prometheusEnabled", true); 

		cacheGuage.setup();
		cacheGuage.set(10.0, "test_cache","test_svc","test_node","0_1");
		Gauge guage = cacheGuage.getIgniteGuageMetric();
		
		List<MetricFamilySamples> samples = guage.collect();
		for(MetricFamilySamples familySample : samples) {
			for(Sample sample : familySample.samples) {
				Assert.assertEquals(10.0,sample.value,0);
			}
		}
	}
}
