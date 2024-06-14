package com.harman.ignite.utils.logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class IgniteLoggerFactoryTest {

@InjectMocks
private IgniteLoggerFactory factory;

@Test
public void testGetLogger() {
	MockitoAnnotations.initMocks(this);
	IgniteLogger logger = factory.getLogger(EventLogger1.class);
	Assert.assertTrue(logger != null && logger instanceof IgniteLoggerImpl);
}
}
