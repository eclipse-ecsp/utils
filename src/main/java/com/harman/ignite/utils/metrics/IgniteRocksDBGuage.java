package com.harman.ignite.utils.metrics;

import org.springframework.stereotype.Component;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

/**
 * 
 * @author hbadshah
 *
 *Creates and registers Guage metric in Prometheus for each one of the property-based metrics in RocksDB,
 *with labels = serviceName and metricName(the actual name of the RocksDB metric property)
 */
@Component
public class IgniteRocksDBGuage extends IgniteGuage {
	private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteRocksDBGuage.class);
	
	public void setup() {
		createGuage("rocksdb_metric","metric_name","svc","node");
		LOGGER.debug("rocksdb_metric guage successfully created.");	
	}
}
