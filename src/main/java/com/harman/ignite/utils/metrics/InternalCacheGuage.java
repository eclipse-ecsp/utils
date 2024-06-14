package com.harman.ignite.utils.metrics;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

/**
 * Guage for internal cache(s) metric.
 * 
 * @author hbadshah
 *
 */
@Component
public class InternalCacheGuage extends IgniteGuage {
	
	private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(InternalCacheGuage.class);
	
	@Value("${internal.metrics.enabled:false}")
	private boolean internalMetricsEnabled;
	
	@Value("${metrics.prometheus.enabled:true}")
	private boolean prometheusEnabled;
	
	@PostConstruct
	public void setup() {
		if(prometheusEnabled && internalMetricsEnabled) {
			createGuage("internal_cache_size_metric","cache_type","svc","node","task_id");
			LOGGER.info("Guage metric for internal cache created.");
		}
	}
	
	@Override
	public void set(double value, String... labels) {
		if(prometheusEnabled && internalMetricsEnabled) {
			super.set(value, labels);
			LOGGER.debug("Published metrics for labels: {} with value: {}",Arrays.asList(labels),value);
		}
	}
}
