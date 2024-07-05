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

package com.harman.ignite.utils.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.harman.ignite.utils.logger.LoggerUtils;
import org.slf4j.Marker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class will suppress repeating exception for a configurable amount of time in millis (typically for minutes).
 * It will help non-pollute the log files and reduce disk pressure. <br>
 * The implementation is based on TurboFilter (logback).
 *
 * @author vkoul
 */
public class DuplicateExceptionFilter extends TurboFilter {

    private static ConcurrentHashMap<String, Long> exceptionCache = null;

    static {
        exceptionCache = new ConcurrentHashMap<>();
    }

    // provide suppress time in milliseconds
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private long suppressTimeInMS = 10 * 60 * 1000;

    @Override
    public void start() {
        super.start();
    }

    /**
     * This method is invoked in deciding if log statement would be logged or not.
     * The filter is configured in logback.xml, where we are providing "suppressTimeInMS" like below:
     * <br><br>
     * &lt;turboFilter class="com.harman.ignite.utils.filter.DuplicateExceptionFilter"&gt;<br>
     * &nbsp;&nbsp;&lt;suppressTimeInMS&gt;60000&lt;/suppressTimeInMS&gt;<br>
     * &lt;/turboFilter>&gt;
     *
     */
    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {

        FilterReply reply = FilterReply.NEUTRAL;

        // Throwable is coming from last argument of decide()
        if (null != t) {
            reply = decide(logger, t);
        } else if (LoggerUtils.hasThrowableObject(format, params)) { // Check if we get Throwable from var-args.
            reply = decide(logger, (Throwable) params[params.length - 1]);
        }

        return reply;
    }

    /**
     * Internally, this method maintains ConcurrentHashMap which keeps: <br>
     * Throwable as key and values at which point-in-time it was logged in.
     * Based on setSuppressTimeInMS value multiple logs would be filtered out,
     * that is at most one log statement would be logged-in within setSuppressTimeInMS time.
     *
     * @param logger Logger from which log statement is being invoked.
     * @param t Throwable object.
     * @return decision if a log statement would be logged-in or not.
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
        } else {
            // It means we have entry inside our map, hence we need to check last time when exception came in.
            // We need to keep a gap of 10 minutes (configurable)
            // so that we can allow it to pass through, else DENY.
            long previousTime = exceptionCache.get(exceptionKey.toString());

            // We are returning NEUTRAL to propagate
            // it through next filter chains.
            if ((currTime - previousTime) >= suppressTimeInMS) {
                // Update the time-stamp in cache
                exceptionCache.put(exceptionKey.toString(), currTime);
                reply = FilterReply.NEUTRAL;
            } else {
                reply = FilterReply.DENY;
            }
        }

        return reply;

    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public long getSuppressTimeInMS() {
        return suppressTimeInMS;
    }

    /**
     * The value of suppressTimeInMS will be taken from logback.xml. Refer below for an example:
     * &lt;turboFilter class="com.harman.ignite.utils.filter.DuplicateExceptionFilter"&gt;<br>
     * &nbsp;&nbsp;&lt;suppressTimeInMS&gt;60000&lt;/suppressTimeInMS&gt;<br>
     * &lt;/turboFilter&gt;
     *
     * @param suppressTimeInMS - time in milliseconds.
     */
    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public void setSuppressTimeInMS(long suppressTimeInMS) {
        this.suppressTimeInMS = suppressTimeInMS;
    }

    static void setExceptionCache(ConcurrentHashMap<String, Long> exceptionCache) {
        DuplicateExceptionFilter.exceptionCache = exceptionCache;
    }
}