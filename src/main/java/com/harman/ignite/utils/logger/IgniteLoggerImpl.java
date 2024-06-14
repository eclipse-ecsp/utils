/**
 * 
 */
package com.harman.ignite.utils.logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harman.ignite.entities.IgniteEvent;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author AKumar
 *
 */
public class IgniteLoggerImpl implements IgniteLogger {
    private Logger logger;
    private static final String MESSAGE = "message";
    private static Map<String, IgniteLoggerImpl> igniteLoggersMap = new ConcurrentHashMap<>();

    private IgniteLoggerImpl(Class<?> clazz) {
        PatternLayout.DEFAULT_CONVERTER_MAP.put("caller", com.harman.ignite.utils.logger.IgniteCallerDataConverter.class.getName());
        PatternLayout.DEFAULT_CONVERTER_MAP.put("ex", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());
        PatternLayout.DEFAULT_CONVERTER_MAP.put("exception", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());
        PatternLayout.DEFAULT_CONVERTER_MAP.put("throwable", com.harman.ignite.utils.logger.IgniteThrowableProxyConverter.class.getName());

        logger = LoggerFactory.getLogger(clazz);
    }

    public void setLogger(Logger lOGGER) {
        logger = lOGGER;
    }

    protected static IgniteLogger getIgniteLoggerInstance(Class<?> clazz) {
        igniteLoggersMap.putIfAbsent(clazz.getName(), new IgniteLoggerImpl(clazz));
        return (IgniteLogger) igniteLoggersMap.get(clazz.getName());
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
        if (logger.isTraceEnabled())
            logger.trace(getMessageWithHeader(event, msg));
    }

    @Override
    public void trace(IgniteEvent event, String format, Object... arguments) {
        if (isTraceEnabled())
            logger.trace(getMessageWithHeader(event, format), arguments);

    }

    @Override
    public void trace(IgniteEvent event, String msg, Throwable t) {
        if (isTraceEnabled())
            logger.trace(getMessageWithHeader(event, msg), t);
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled())
            logger.trace(msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (logger.isTraceEnabled())
            logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (logger.isTraceEnabled())
            logger.trace(msg, t);
    }

    @Override
    public void debug(IgniteEvent event, String msg) {
        if (logger.isDebugEnabled())
            logger.debug(getMessageWithHeader(event, msg));
    }

    @Override
    public void debug(IgniteEvent event, String format, Object... arguments) {
        if (logger.isDebugEnabled())
            logger.debug(getMessageWithHeader(event, format), arguments);
    }

    @Override
    public void debug(IgniteEvent event, String msg, Throwable t) {
        if (logger.isDebugEnabled())
            logger.debug(getMessageWithHeader(event, msg), t);
    }

    @Override
    public void debug(String msg) {
        if (logger.isDebugEnabled())
            logger.debug(msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (logger.isDebugEnabled())
            logger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (logger.isDebugEnabled())
            logger.debug(msg, t);
    }

    @Override
    public void info(IgniteEvent event, String msg) {
        if (logger.isInfoEnabled())
            logger.info(getMessageWithHeader(event, msg));
    }

    @Override
    public void info(IgniteEvent event, String format, Object... arguments) {
        if (logger.isInfoEnabled())
            logger.info(getMessageWithHeader(event, format), arguments);
    }

    @Override
    public void info(IgniteEvent event, String msg, Throwable t) {
        if (logger.isInfoEnabled())
            logger.info(getMessageWithHeader(event, msg), t);
    }

    @Override
    public void info(String msg) {
        if (logger.isInfoEnabled())
            logger.info(msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        if (logger.isInfoEnabled())
            logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled())
            logger.info(msg, t);
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

    /*
     * Added for JUnit test purpose only.
     */
    static IgniteLoggerImpl getIgniteLoggerImplInstance(Class<?> clazz) {
        igniteLoggersMap.putIfAbsent(clazz.getName(), new IgniteLoggerImpl(clazz));
        return igniteLoggersMap.get(clazz.getName());

    }
}
