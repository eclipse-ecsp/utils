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

/**
 * MDC (Mapped Diagnostic Context) will help produce customized logs for Ignite
 * product. It will bring together the core parameters like the TimeStamp,
 * RequestId, MessageId, BizTransactionId and CorrelationId as a wrapper for all
 * the events being logged.
 *
 * @author AKumar
 *
 */
public interface IgniteLogger {

    /**
     * Method to know if Trace level log is enable or not.
     *
     * @return - true - if trace level enabled, false - otherwise.
     */
    public boolean isTraceEnabled();

    /**
     * Method to know if Debug level log is enable or not.
     *
     * @return - true - if Debug level enabled, false - otherwise.
     */
    public boolean isDebugEnabled();

    /**
     * Method to know if Info level log is enable or not.
     *
     * @return - true - if Info level enabled, false - otherwise.
     */
    public boolean isInfoEnabled();

    /**
     * Method to know if Warn level log is enable or not.
     *
     * @return - true - if Warn level enabled, false - otherwise.
     */
    public boolean isWarnEnabled();

    /**
     * Method to know if Error level log is enable or not.
     *
     * @return - true - if Error level enabled, false - otherwise.
     */
    public boolean isErrorEnabled();

    public void trace(IgniteEvent event, String msg);

    public void trace(IgniteEvent event, String format, Object... arguments);

    public void trace(IgniteEvent event, String msg, Throwable t);

    public void trace(String msg);

    public void trace(String format, Object... arguments);

    public void trace(String msg, Throwable t);

    public void debug(IgniteEvent event, String msg);

    public void debug(IgniteEvent event, String format, Object... arguments);

    public void debug(IgniteEvent event, String msg, Throwable t);

    public void debug(String msg);

    public void debug(String format, Object... arguments);

    public void debug(String msg, Throwable t);

    public void info(IgniteEvent event, String msg);

    public void info(IgniteEvent event, String format, Object... arguments);

    public void info(IgniteEvent event, String msg, Throwable t);

    public void info(String msg);

    public void info(String format, Object... arguments);

    public void info(String msg, Throwable t);

    public void warn(IgniteEvent event, String msg);

    public void warn(IgniteEvent event, String format, Object... arguments);

    public void warn(IgniteEvent event, String msg, Throwable t);

    public void warn(String msg);

    public void warn(String format, Object... arguments);

    public void warn(String msg, Throwable t);

    public void error(IgniteEvent event, String msg);

    public void error(IgniteEvent event, String format, Object... arguments);

    public void error(IgniteEvent event, String msg, Throwable t);

    public void error(String msg);

    public void error(String format, Object... arguments);

    public void error(String msg, Throwable t);

}