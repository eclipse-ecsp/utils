package com.harman.ignite.utils.filter;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Marker;

import com.harman.ignite.utils.logger.LoggerUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * The purpose of this class is to suppress repeating exception for a
 * configurable amount of time in millis (typically for minutes).
 * 
 * It will help non-pollute the log files and reduce disk pressure.
 * 
 * The implementation is based on TurboFilter (logback).
 * 
 * @author vkoul
 *
 */
public class DuplicateExceptionFilter extends TurboFilter {

    private static ConcurrentHashMap<String, Long> exceptionCache = null;

    // provide suppress time in milliseconds
    private long suppressTimeInMS = 10L * 60 * 1000;

    static {
        exceptionCache = new ConcurrentHashMap<>();
    }


    /**
     * 
     * This method is invoked in deciding if log statement would be logged or
     * not.
     * 
     * The filter is configured in logback.xml, where we are providing
     * "suppressTimeInMS" like below:
     * 
     * 
     * <turboFilter class=
     * "com.harman.ignite.utils.filter.DuplicateExceptionFilter">
     * <suppressTimeInMS>60000</suppressTimeInMS> </turboFilter>
     */
    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {

        FilterReply reply = FilterReply.NEUTRAL;

        // Throwable is coming from last argument of decide()
        if (null != t) {
            reply = decide(logger, t);
        }
        // Check if we get Throwable from var-args.
        else if (LoggerUtils.hasThrowableObject(format, params)) {
            reply = decide(logger, (Throwable) params[params.length - 1]);
        }
        return reply;
    }

    /**
     * 
     * @param logger
     * @param t
     * @return
     * 
     *         This method is being invoked to decide if a log statement would
     *         be logged-in or not.
     * 
     *         Internally it maintains ConcurrentHashMap which keeps Throwable
     *         as key and values at which point-in-time it was logged in.
     * 
     *         Based on setSuppressTimeInMS value multiple logs would be
     *         filtered out, that is at most one log statement would be
     *         logged-in within setSuppressTimeInMS time.
     */

    private FilterReply decide(Logger logger, Throwable t) {
        FilterReply reply;
        long currTime = System.currentTimeMillis();

        // The exceptionKey need to have both:
        // 1) Exception-Name and 2) Logger from which it came from.
        //
        // Else, we may have a scenario in which if we have entry for an
        // exception in cache,
        // even though we may be logging it for first time, it would get
        // discarded.

        StringBuilder exceptionKey = new StringBuilder().append(t.getClass().getName()).append(logger.toString());

        if (!exceptionCache.containsKey(exceptionKey.toString())) {
            exceptionCache.put(exceptionKey.toString(), currTime);

            // We are returning NEUTRAL to propagate
            // it thru next filter chains.
            reply = FilterReply.NEUTRAL;
        }
        // It means we have entry inside our map
        // hence we need to check last time when
        // exception came in.

        // We need to keep a gap of 10 minutes (configurable)
        // so that we can allow it to pass thru, else DENY.
        else {
            long previousTime = exceptionCache.get(exceptionKey.toString());

            // We are returning NEUTRAL to propagate
            // it thru next filter chains.
            if ((currTime - previousTime) >= suppressTimeInMS) {
                // Update the time-stamp in cache
                exceptionCache.put(exceptionKey.toString(), currTime);
                reply = FilterReply.NEUTRAL;
            }
            else {
                reply = FilterReply.DENY;
            }
        }

        return reply;

    }

    public long getSuppressTimeInMS() {
        return suppressTimeInMS;
    }

    /**
     * 
     * 
     * The value of suppressTimeInMS will be taken from logback.xml. Refer below
     * for an example:
     * 
     * <turboFilter class=
     * "com.harman.ignite.utils.filter.DuplicateExceptionFilter">
     * <suppressTimeInMS>60000</suppressTimeInMS> </turboFilter>
     * 
     * @param suppressTimeInMS
     */
    public void setSuppressTimeInMS(long suppressTimeInMS) {
        this.suppressTimeInMS = suppressTimeInMS;
    }

    static void setExceptionCache(ConcurrentHashMap<String,Long> exceptionCache) {
    	DuplicateExceptionFilter.exceptionCache = exceptionCache;
    }
}