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

package com.harman.ignite.utils.metrics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * The default implementation of error counter for ignite platform. <br>
 * If prometheus is enabled all base framework and services can inject
 * this bean to register error counter metric and continuously publish values.
 *
 * @author avadakkootko
 */
@Component
public class IgniteErrorCounter extends GenericIgniteCounter {

    public static final String NA = "N/A";
    private static final String ERROR_COUNT = "error_count";
    private static final String NODE = "node";
    private static final String TASKID = "tid";
    private static final String EXCEPTION_CLASS_NAME = "ecn";
    @Value("${NODE_NAME:localhost}")
    private String nodeName;

    public IgniteErrorCounter() {
        super(ERROR_COUNT, ERROR_COUNT, NODE, TASKID, EXCEPTION_CLASS_NAME);
    }

    /**
     * Increments the counter by 1.
     *
     * @param taskId Optional and refers to the stream processor's current task id. Defaults to N/A
     * @param exceptionClassName The exception class name
     */
    public void incErrorCounter(Optional<String> taskId, Class exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        } else {
            tid = NA;
        }
        inc(nodeName, tid, exceptionClassName.getName());
    }

    /**
     * Increments the counter with value specified.
     *
     * @param value The value to increment the counter by.
     * @param taskId Optional and refers to the stream processor's current task id. Defaults to N/A
     * @param exceptionClassName The exception class name
     */
    public void incErrorCounter(double value, Optional<String> taskId, Class exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        } else {
            tid = NA;
        }
        inc(value, nodeName, tid, exceptionClassName.getName());
    }

    /**
     * This returns the error count grouped by nodename, taskId if present and exceptionClassName.
     *
     * @param taskId Optional and refers to the stream processor's current task id. Defaults to N/A
     * @param exceptionClassName The exception class name
     * @return The value of the counter
     */
    public double getErrorCounterValue(Optional<String> taskId, Class exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        } else {
            tid = NA;
        }
        return get(nodeName, tid, exceptionClassName.getName());
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
