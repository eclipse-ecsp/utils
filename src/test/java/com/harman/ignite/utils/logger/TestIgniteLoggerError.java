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

public class TestIgniteLoggerError {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersWhenErrorEnabled() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(LOGGER).error(arg.capture());
        assertEquals("ErrorWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenErrorDisabled() {
        when(LOGGER.isErrorEnabled()).thenReturn(false);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(LOGGER, new Times(1)).error("ErrorWithMsg");
    }
}
