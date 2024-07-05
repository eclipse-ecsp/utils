/*
 * *******************************************************************************
 *
 *  Copyright (c) 2023-24 Harman International
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *       
 *
 *  Unless required by applicable law or agreed to in writing, software
 *
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *
 *  limitations under the License.
 *
 *
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  *******************************************************************************
 */

package com.harman.ignite.utils.logger;

import com.harman.ignite.entities.IgniteEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for IgniteLogger.
 *
 * @author AKumar
 */

public class TestIgniteLogger {
    private static final int TWO = 2;
    private static IgniteLoggerImpl igniteLogger =
            IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger logger;

    /**
     * Setup method.
     */
    @Before
    public void setup() {
        logger = Mockito.mock(Logger.class);
        when(logger.isTraceEnabled()).thenReturn(true);
        when(logger.isDebugEnabled()).thenReturn(true);
        when(logger.isInfoEnabled()).thenReturn(true);
        igniteLogger.setLogger(logger);
    }

    @Test
    public void testIgniteLoggersInMap() {
        Thread igniteEventTh1 = new Thread(new EventLogger1(), "EventLogger1");
        Thread igniteEventTh2 = new Thread(new EventLogger2(), "EventLogger2");
        igniteEventTh1.start();
        igniteEventTh2.start();

        Map<String, IgniteLoggerImpl> igniteLoggersMap = igniteLogger.getIgniteLoggersMap();
        assertEquals(TWO, igniteLoggersMap.size());
        Assert.assertTrue(igniteLoggersMap.containsKey(EventLogger1.class.getName()));
        Assert.assertTrue(igniteLoggersMap.containsKey(EventLogger2.class.getName()));
    }

    @Test
    public void testInfo() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.info("InfoWithMsg");
        Mockito.verify(logger).info(arg.capture());
        assertEquals("InfoWithMsg", arg.getValue());
    }

    @Test
    public void testInfoWithMsgAndThrowable() {
        igniteLogger.info("InfoWithMsgAndThrowable", new Throwable());
        Mockito.verify(logger).info(Mockito.eq("InfoWithMsgAndThrowable"), Mockito.<Throwable>any());
    }

    @Test
    public void testInfoWithFormatNArgs() {
        igniteLogger.info("Display message as: parameter1={}", "value1");
        Mockito.verify(logger)
                .info(
                        ArgumentMatchers.contains("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());
    }

    @Test
    public void testInfoWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "InfoWithMsgAndThrowable");
        Mockito.verify(logger).info(ArgumentMatchers.endsWith("InfoWithMsgAndThrowable"));
    }

    @Test
    public void testInfoWithIgniteEventNMsgNThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "InfoWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(logger).info(Mockito.endsWith("InfoWithIgniteEventnMsgnThrowable"), Mockito.<Throwable>any());
    }

    @Test
    public void testInfoWithIgniteEventNFormatNargs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.info(igniteEvent, "Display message as: parameter1={} parameter2={}", "value1", new Object());
        Mockito.verify(logger)
                .info(
                        ArgumentMatchers.contains("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testInfoWithIgniteEventAndCorrId() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        Mockito.when(igniteEvent.getCorrelationId()).thenReturn("correlationIdMock");
        igniteLogger.info(igniteEvent, "InfoWithMsgAndThrowable");
        Mockito.verify(logger).info(Mockito.contains("InfoWithMsgAndThrowable"));
    }

    @Test
    public void testDebug() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.debug("DebugWithMsg");
        Mockito.verify(logger).debug(arg.capture());
        assertEquals("DebugWithMsg", arg.getValue());
    }

    @Test
    public void testDebugWithMsgAndThrowable() {
        igniteLogger.debug("DebugWithMsgAndThrowable", new Throwable());
        Mockito.verify(logger).debug(Mockito.eq("DebugWithMsgAndThrowable"), Mockito.<Throwable>any());
    }

    @Test
    public void testDebugWithFormatNArgs() {
        igniteLogger.debug("Display message as: parameter1={}", "value1");
        Mockito.verify(logger)
                .debug(
                        ArgumentMatchers.contains("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testDebugWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "DebugWithMsgAndThrowable");
        Mockito.verify(logger).debug(ArgumentMatchers.endsWith("DebugWithMsgAndThrowable"));
    }

    @Test
    public void testDebugWithIgniteEventnMsgNThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "DebugWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(logger)
                .debug(
                        Mockito.endsWith("DebugWithIgniteEventnMsgnThrowable"),
                        Mockito.<Throwable>any());
    }

    @Test
    public void testDebugWithIgniteEventNFormatNargs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.debug(igniteEvent, "Display message as: parameter1={} parameter2={}", "value1", new Object());
        Mockito.verify(logger).debug(ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                ArgumentMatchers.<Object[]>any());
    }

    @Test
    public void testWarn() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(logger).warn(arg.capture());
        assertEquals("WarnWithMsg", arg.getValue());
    }

    @Test
    public void testWarnWithMsgAndThrowable() {
        igniteLogger.warn("WarnWithMsgAndThrowable", new Throwable());
        Mockito.verify(logger).warn(Mockito.eq("WarnWithMsgAndThrowable"), Mockito.<Throwable>any());
    }

    @Test
    public void testWarnWithFormatNArgs() {
        igniteLogger.warn("Display message as: parameter1={}", "value1");
        Mockito.verify(logger)
                .warn(
                        ArgumentMatchers.endsWith("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testWarnWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "WarnWithMsgAndThrowable");
        Mockito.verify(logger).warn(Mockito.endsWith("WarnWithMsgAndThrowable"));
    }

    @Test
    public void testWarnWithIgniteEventNMsgNThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "WarnWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(logger)
                .warn(
                        Mockito.endsWith("WarnWithIgniteEventnMsgnThrowable"),
                        Mockito.<Throwable>any());
    }

    @Test
    public void testWarnWithIgniteEventNFormatNargs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.warn(igniteEvent, "Display message as: parameter1={} parameter2={}", "value1", new Object());
        Mockito.verify(logger)
                .warn(
                        ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testTrace() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.trace("TraceWithMsg");
        Mockito.verify(logger).trace(arg.capture());
        assertEquals("TraceWithMsg", arg.getValue());
    }

    @Test
    public void testTraceWithMsgAndThrowable() {
        igniteLogger.trace("TraceWithMsgAndThrowable", new Throwable());
        Mockito.verify(logger).trace(Mockito.eq("TraceWithMsgAndThrowable"), Mockito.<Throwable>any());
    }

    @Test
    public void testTraceWithFormatNArgs() {
        igniteLogger.trace("Display message as: parameter1={}", "value1");
        Mockito.verify(logger)
                .trace(
                        ArgumentMatchers.contains("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());
    }

    @Test
    public void testTraceWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "TraceWithMsgAndThrowable");
        Mockito.verify(logger).trace(ArgumentMatchers.endsWith("TraceWithMsgAndThrowable"));
    }

    @Test
    public void testTraceWithIgniteEventNMsgnThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "TraceWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(logger)
                .trace(
                        Mockito.endsWith("TraceWithIgniteEventnMsgnThrowable"),
                        Mockito.<Throwable>any());
    }

    @Test
    public void testTraceWithIgniteEventNFormatNargs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.trace(igniteEvent, "Display message as: parameter1={} parameter2={}", "value1", new Object());
        Mockito.verify(logger)
                .trace(
                        ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                        ArgumentMatchers.<Object[]>any());
    }

    @Test
    public void testError() {
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(logger).error(arg.capture());
        assertEquals("ErrorWithMsg", arg.getValue());
    }

    @Test
    public void testErrorWithMsgAndThrowable() {
        igniteLogger.error("ErrorWithMsgAndThrowable", new Throwable());
        Mockito.verify(logger)
                .error(
                        Mockito.eq("ErrorWithMsgAndThrowable"),
                        Mockito.<Throwable>any());
    }

    @Test
    public void testErrorWithFormatnArgs() {
        igniteLogger.error("Display message as: parameter1={}", "value1");
        Mockito.verify(logger)
                .error(
                        ArgumentMatchers.contains("Display message as: parameter1={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testErrorWithIgniteEvent() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "ErrorWithMsgAndThrowable");
        Mockito.verify(logger).error(ArgumentMatchers.endsWith("ErrorWithMsgAndThrowable"));
    }

    @Test
    public void testErrorWithIgniteEventNMsgNThrowable() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "ErrorWithIgniteEventnMsgnThrowable", new Throwable());
        Mockito.verify(logger)
                .error(
                        Mockito.endsWith("ErrorWithIgniteEventnMsgnThrowable"),
                        Mockito.<Throwable>any());
    }

    @Test
    public void testErrorWithIgniteEventNFormatNArgs() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "Display message as: parameter1={} parameter2={}", "value1", new Object());
        Mockito.verify(logger)
                .error(
                        ArgumentMatchers.endsWith("Display message as: parameter1={} parameter2={}"),
                        ArgumentMatchers.<Object[]>any());

    }

    @Test
    public void testErrorWithIgniteEventNMsgNException() {
        IgniteEvent igniteEvent = Mockito.mock(IgniteEvent.class);
        igniteLogger.error(igniteEvent, "EXCEPTION", new Exception());
        Mockito.verify(logger).error(Mockito.endsWith("EXCEPTION"), Mockito.<Exception>any());
    }
}
