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

public class TestIgniteLoggerWarn {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersWhenWarnEnabled() {
        when(LOGGER.isWarnEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(LOGGER).warn(arg.capture());
        assertEquals("WarnWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenWarnDisabled() {
        when(LOGGER.isWarnEnabled()).thenReturn(false);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(LOGGER, new Times(1)).warn("WarnWithMsg");
    }
}
