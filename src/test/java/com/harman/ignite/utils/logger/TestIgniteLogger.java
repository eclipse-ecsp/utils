/**
 * 
 */
package com.harman.ignite.utils.logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.harman.ignite.entities.IgniteEvent;

/**
 * @author AKumar
 *
 */

public class TestIgniteLogger {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger LOGGER;

    @Before
    public void setup() {
        LOGGER = Mockito.mock(Logger.class);
        when(LOGGER.isTraceEnabled()).thenReturn(true);
        when(LOGGER.isDebugEnabled()).thenReturn(true);
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        igniteLogger.setLogger(LOGGER);
    }

    @Test
    public void testIgniteLoggersInMap() {
        Thread igniteEventTh1 = new Thread(new EventLogger1(), "EventLogger1");
        Thread igniteEventTh2 = new Thread(new EventLogger2(), "EventLogger2");
        igniteEventTh1.start();
        igniteEventTh2.start();

        Map<String, IgniteLoggerImpl> igniteLoggersMap = igniteLogger.getIgniteLoggersMap();
        assertEquals(2, igniteLoggersMap.size());
        Assert.assertTrue(igniteLoggersMap.containsKey(EventLogger1.class.getName()));
        Assert.assertTrue(igniteLoggersMap.containsKey(EventLogger2.class.getName()));
    }

    @Test
    public void testInfo() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.info("InfoWithMsg");
        Mockito.verify(LOGGER).info(arg.capture());
        assertEquals("InfoWithMsg", arg.getValue());
    }

    @Test
    public void testInfoWithMsgAndThrowable() {
        igniteLogger.info("InfoWithMsgAndThrowable", new Throwable());
        Mockito.verify(LOGGER).info(Mockito.eq("InfoWithMsgAndThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testInfoWithFormatnArgs() {
        igniteLogger.info("Display message as: parameter1={}", new String("value1"));
        Mockito.verify(LOGGER).info(ArgumentMatchers.contains("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());
    }

    @Test
    public void testInfoWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "InfoWithMsgAndThrowable");
        Mockito.verify(LOGGER).info(ArgumentMatchers.endsWith("InfoWithMsgAndThrowable"));
    }

    @Test
    public void testInfoWithIgniteEventnMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "InfoWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(LOGGER).info(Mockito.endsWith("InfoWithIgniteEventnMsgnThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testInfoWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "Display message as: parameter1={} parameter2={}", new String("value1"), new Object());
        Mockito.verify(LOGGER).info(ArgumentMatchers.contains("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testInfoWithIgniteEventAndCorrId() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        Mockito.when(igniteEvent.getCorrelationId()).thenReturn("correlationIdMock");
        igniteLogger.info(igniteEvent, "InfoWithMsgAndThrowable");
        Mockito.verify(LOGGER).info(Mockito.contains("InfoWithMsgAndThrowable"));
    }

    @Test
    public void testDebug() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.debug("DebugWithMsg");
        Mockito.verify(LOGGER).debug(arg.capture());
        assertEquals("DebugWithMsg", arg.getValue());
    }

    @Test
    public void testDebugWithMsgAndThrowable() {
        igniteLogger.debug("DebugWithMsgAndThrowable", new Throwable());
        Mockito.verify(LOGGER).debug(Mockito.eq("DebugWithMsgAndThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testDebugWithFormatnArgs() {
        igniteLogger.debug("Display message as: parameter1={}", new String("value1"));
        Mockito.verify(LOGGER).debug(ArgumentMatchers.contains("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testDebugWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "DebugWithMsgAndThrowable");
        Mockito.verify(LOGGER).debug(ArgumentMatchers.endsWith("DebugWithMsgAndThrowable"));
    }

    @Test
    public void testDebugWithIgniteEventnMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "DebugWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(LOGGER).debug(Mockito.endsWith("DebugWithIgniteEventnMsgnThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testDebugWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "Display message as: parameter1={} parameter2={}", new String("value1"), new Object());
        Mockito.verify(LOGGER).debug(ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                ArgumentMatchers.<Object[]> any());
    }

    @Test
    public void testWarn() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(LOGGER).warn(arg.capture());
        assertEquals("WarnWithMsg", arg.getValue());
    }

    @Test
    public void testWarnWithMsgAndThrowable() {
        igniteLogger.warn("WarnWithMsgAndThrowable", new Throwable());
        Mockito.verify(LOGGER).warn(Mockito.eq("WarnWithMsgAndThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testWarnWithFormatnArgs() {
        igniteLogger.warn("Display message as: parameter1={}", new String("value1"));
        Mockito.verify(LOGGER).warn(ArgumentMatchers.endsWith("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testWarnWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "WarnWithMsgAndThrowable");
        Mockito.verify(LOGGER).warn(Mockito.endsWith("WarnWithMsgAndThrowable"));
    }

    @Test
    public void testWarnWithIgniteEventnMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "WarnWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(LOGGER).warn(Mockito.endsWith("WarnWithIgniteEventnMsgnThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testWarnWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "Display message as: parameter1={} parameter2={}", new String("value1"), new Object());
        Mockito.verify(LOGGER).warn(ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testTrace() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.trace("TraceWithMsg");
        Mockito.verify(LOGGER).trace(arg.capture());
        assertEquals("TraceWithMsg", arg.getValue());
    }

    @Test
    public void testTraceWithMsgAndThrowable() {
        igniteLogger.trace("TraceWithMsgAndThrowable", new Throwable());
        Mockito.verify(LOGGER).trace(Mockito.eq("TraceWithMsgAndThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testTraceWithFormatnArgs() {
        igniteLogger.trace("Display message as: parameter1={}", new String("value1"));
        Mockito.verify(LOGGER).trace(ArgumentMatchers.contains("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());
    }

    @Test
    public void testTraceWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "TraceWithMsgAndThrowable");
        Mockito.verify(LOGGER).trace(ArgumentMatchers.endsWith("TraceWithMsgAndThrowable"));
    }

    @Test
    public void testTraceWithIgniteEventnMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "TraceWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(LOGGER).trace(Mockito.endsWith("TraceWithIgniteEventnMsgnThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testTraceWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "Display message as: parameter1={} parameter2={}", new String("value1"), new Object());
        Mockito.verify(LOGGER).trace(ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                ArgumentMatchers.<Object[]> any());
    }

    @Test
    public void testError() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(LOGGER).error(arg.capture());
        assertEquals("ErrorWithMsg", arg.getValue());
    }

    @Test
    public void testErrorWithMsgAndThrowable() {
        igniteLogger.error("ErrorWithMsgAndThrowable", new Throwable());
        Mockito.verify(LOGGER).error(Mockito.eq("ErrorWithMsgAndThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testErrorWithFormatnArgs() {
        igniteLogger.error("Display message as: parameter1={}", new String("value1"));
        Mockito.verify(LOGGER).error(ArgumentMatchers.contains("Display message as: parameter1={}"), ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testErrorWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "ErrorWithMsgAndThrowable");
        Mockito.verify(LOGGER).error(ArgumentMatchers.endsWith("ErrorWithMsgAndThrowable"));
    }

    @Test
    public void testErrorWithIgniteEventnMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "ErrorWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(LOGGER).error(Mockito.endsWith("ErrorWithIgniteEventnMsgnThrowable"), Mockito.<Throwable> any());
    }

    @Test
    public void testErrorWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "Display message as: parameter1={} parameter2={}", new String("value1"), new Object());
        Mockito.verify(LOGGER).error(ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                ArgumentMatchers.<Object[]> any());

    }

    @Test
    public void testErrorWithIgniteEventnMsgnException() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "EXCEPTION", new Exception());
        Mockito.verify(LOGGER).error(Mockito.endsWith("EXCEPTION"), Mockito.<Exception> any());
    }
}
