package com.harman.ignite.utils.logger;

import java.util.List;

import ch.qos.logback.classic.pattern.CallerDataConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.status.ErrorStatus;

/**
 * This is used to customize log messages. This class overrides methods from
 * CallerDataConverter and customize log messages.
 * 
 * Customizations include: 1. removing of "caller" from log message. 2. Printing
 * short-form of fully qualified package
 * name(eg:com.harman.ignite.utils.logger.IgniteCallerDataConverter will be
 * replaced as c.h.i.u.l.IgniteCallerDataConverter)
 *
 * @author vishnu.k;
 */
public class IgniteCallerDataConverter extends CallerDataConverter {

    private int depthStart = 1;
    static final int MAX_ERR_COUNT = 4;
    private int errCount = 0;
    List<EventEvaluator<ILoggingEvent>> eventEvaluatorList = null;

    @Override
    public String convert(ILoggingEvent logEvent) {
        if (eventEvaluatorList != null) {
            boolean printCallerData = false;
            for (int i = 0; i < eventEvaluatorList.size(); i++) {
                EventEvaluator<ILoggingEvent> eventEvaluator = eventEvaluatorList.get(i);
                if(canEvaluatorProcessLogEvent(logEvent, eventEvaluator)) {
                    printCallerData = true;
                    break;
                }
            }

            // no evaluator can process the logging event
            if (!printCallerData) {
                return CoreConstants.EMPTY_STRING;
            }
        }

        return convertToCallerData(logEvent);
    }

    private String convertToCallerData(ILoggingEvent le) {
        StringBuilder buf = new StringBuilder();
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > depthStart) {
            buf.append(getCallerLinePrefix());
            buf.append(cda[depthStart]);
            buf.append(" ");

            return buf.toString();
        }
        else {
            return CallerData.CALLER_DATA_NA;
        }
    }

    @Override
    protected String getCallerLinePrefix() {
        return "";
    }

    private boolean canEvaluatorProcessLogEvent(ILoggingEvent logEvent, EventEvaluator<ILoggingEvent> eventEvaluator) {
        try {
            if (eventEvaluator.evaluate(logEvent)) {
                return true;
            }
        } catch (EvaluationException eex) {
            errCount++;
            processErrorCount(eventEvaluator, eex);
        }
        return false;
    }

    private void processErrorCount(EventEvaluator<ILoggingEvent> ee, EvaluationException eex) {
        if (errCount < MAX_ERR_COUNT) {
            addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
        }
        else if (errCount == MAX_ERR_COUNT) {
            ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + ee.getName() + "].", this,
                    eex);
            errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors."
                    + "We don't want the StatusManager to get flooded.", this));
            addStatus(errorStatus);
        }
    }

}
