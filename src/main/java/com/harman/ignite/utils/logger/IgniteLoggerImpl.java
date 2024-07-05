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

import ch.qos.logback.classic.PatternLayout;
import com.harman.ignite.entities.IgniteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation class for IgniteLogger interface.
 * This class is used to customize log messages.
 *
 * @author AKumar
 *
 */
public class IgniteLoggerImpl implements IgniteLogger {

    private Logger logger;
    private static final String MESSAGE = "message";
    private static Map<String, IgniteLoggerImpl> igniteLoggersMap = new ConcurrentHashMap<>();

    private IgniteLoggerImpl(Class<?> clazz) {
        PatternLayout.defaultConverterMap
        .put("caller", com.harman.ignite.utils.logger.IgniteCallerDataConverter.class.getName());
        PatternLayout.defaultConverterMap
        .put("ex", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap
        .put("exception", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());
        PatternLayout.defaultConverterMap
        .put("throwable", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());
        logger = LoggerFactory.getLogger(clazz);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected static IgniteLogger getIgniteLoggerInstance(Class<?> clazz) {
        igniteLoggersMap.putIfAbsent(clazz.getName(), new IgniteLoggerImpl(clazz));
        return (IgniteLogger) igniteLoggersMap.get(clazz.getName());
    }

    /*
     * Added for JUnit test purpose only.
     */
    static IgniteLoggerImpl getIgniteLoggerImplInstance(Class<?> clazz) {
        igniteLoggersMap.putIfAbsent(clazz.getName(), new IgniteLoggerImpl(clazz));
        return igniteLoggersMap.get(clazz.getName());
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void trace(IgniteEvent event, String msg) {
        if (logger.isTraceEnabled()) {
            logger.trace(getMessageWithHeader(event, msg));
        }
    }

    @Override
    public void trace(IgniteEvent event, String format, Object... arguments) {
        if (isTraceEnabled()) {
            logger.trace(getMessageWithHeader(event, format), arguments);
        }
    }

    @Override
    public void trace(IgniteEvent event, String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.trace(getMessageWithHeader(event, msg), t);
        }
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            logger.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (logger.isTraceEnabled()) {
            logger.trace(format, arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (logger.isTraceEnabled()) {
            logger.trace(msg, t);
        }
    }

    @Override
    public void debug(IgniteEvent event, String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(getMessageWithHeader(event, msg));
        }
    }

    @Override
    public void debug(IgniteEvent event, String format, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(getMessageWithHeader(event, format), arguments);
        }
    }

    @Override
    public void debug(IgniteEvent event, String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(getMessageWithHeader(event, msg), t);
        }
    }

    @Override
    public void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, t);
        }
    }

    @Override
    public void info(IgniteEvent event, String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(getMessageWithHeader(event, msg));
        }
    }

    @Override
    public void info(IgniteEvent event, String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(getMessageWithHeader(event, format), arguments);
        }
    }

    @Override
    public void info(IgniteEvent event, String msg, Throwable t) {
        if (logger.isInfoEnabled()) {
            logger.info(getMessageWithHeader(event, msg), t);
        }
    }

    @Override
    public void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            logger.info(msg, t);
        }
    }

    @Override
    public void warn(IgniteEvent event, String msg) {
        logger.warn(getMessageWithHeader(event, msg));
    }

    @Override
    public void warn(IgniteEvent event, String format, Object... arguments) {
        logger.warn(getMessageWithHeader(event, format), arguments);
    }

    @Override
    public void warn(IgniteEvent event, String msg, Throwable t) {
        logger.warn(getMessageWithHeader(event, msg), t);
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public void error(IgniteEvent event, String msg) {
        logger.error(getMessageWithHeader(event, msg));
    }

    @Override
    public void error(IgniteEvent event, String format, Object... arguments) {
        logger.error(getMessageWithHeader(event, format), arguments);
    }

    @Override
    public void error(IgniteEvent event, String msg, Throwable t) {
        logger.error(getMessageWithHeader(event, msg), t);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    private String getMessageWithHeader(IgniteEvent event, String format) {
        StringBuilder formatBuilder = new StringBuilder();
        long timeStamp = event.getTimestamp();
        formatBuilder.append("Timestamp:").append(timeStamp);

        String requestId = event.getRequestId();
        formatBuilder.append(" , RequestId:").append(requestId);

        String messageId = event.getMessageId();
        formatBuilder.append(" , MessageId:").append(messageId);

        String bizTransactionId = event.getBizTransactionId();
        formatBuilder.append(" , BizTransactionId:").append(bizTransactionId);

        formatBuilder.append(" , VehicleID:").append(event.getVehicleId());

        formatBuilder.append(" , EventID:").append(event.getEventId());

        formatBuilder.append(" , Version:").append(event.getSchemaVersion());

        formatBuilder.append(" , SourceDeviceID:").append(event.getSourceDeviceId());

        Optional<String> correlateionId = Optional.ofNullable(event.getCorrelationId());
        if (correlateionId.isPresent()) {
            formatBuilder.append(" , CorrelationId:").append(correlateionId);
        }
        formatBuilder.append(" ," + MESSAGE + ":").append(format);
        return formatBuilder.toString();
    }

    /*
     * Added for JUnit test purpose only.
     */
    Map<String, IgniteLoggerImpl> getIgniteLoggersMap() {
        return igniteLoggersMap;
    }
}
