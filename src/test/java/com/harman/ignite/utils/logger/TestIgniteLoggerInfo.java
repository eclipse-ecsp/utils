/**
 * 
 */
package com.harman.ignite.utils.logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.slf4j.Logger;

/**
 * @author AKumar
 *
 */

public class TestIgniteLoggerInfo {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersWhenInfoEnabled() {
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.info("InfoWithMsg");
        Mockito.verify(LOGGER).info(arg.capture());
        assertEquals("InfoWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenInfoDisabled() {
        when(LOGGER.isInfoEnabled()).thenReturn(false);
        igniteLogger.info("InfoWithMsg");
        Mockito.verify(LOGGER, new Times(0)).info("InfoWithMsg");
    }
}
