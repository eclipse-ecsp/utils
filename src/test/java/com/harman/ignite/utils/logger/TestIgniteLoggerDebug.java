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

public class TestIgniteLoggerDebug {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersWhenDebugEnabled() {
        when(LOGGER.isDebugEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.debug("DebugWithMsg");
        Mockito.verify(LOGGER).debug(arg.capture());
        assertEquals("DebugWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenDebugDisabled() {
        when(LOGGER.isDebugEnabled()).thenReturn(false);
        igniteLogger.debug("DebugWithMsg");
        Mockito.verify(LOGGER, new Times(0)).debug("DebugWithMsg");
    }
}
