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

public class TestIgniteLoggerTrace {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersWhenTraceEnabled() {
        when(LOGGER.isTraceEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.trace("TraceWithMsg");
        Mockito.verify(LOGGER).trace(arg.capture());
        assertEquals("TraceWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenTraceDisabled() {
        when(LOGGER.isTraceEnabled()).thenReturn(false);
        igniteLogger.trace("TraceWithMsg");
        Mockito.verify(LOGGER, new Times(0)).trace("TraceWithMsg");
    }
}
