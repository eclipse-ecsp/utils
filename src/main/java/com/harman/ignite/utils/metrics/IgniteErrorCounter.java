package com.harman.ignite.utils.metrics;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The default implementation of error counter for ignite platform. If
 * prometheus is enabled all base frame work and services can inject this bean
 * to register error counter metric and continuously publish values.
 *
 * @author avadakkootko
 *
 */
@Component
public class IgniteErrorCounter extends GenericIgniteCounter {

    @Value("${NODE_NAME:localhost}")
    private String nodeName;

    private static final String ERROR_COUNT = "error_count";
    private static final String NODE = "node";
    private static final String TASKID = "tid";
    private static final String EXCEPTION_CLASS_NAME = "ecn";
    public static final String NA = "N/A";

    public IgniteErrorCounter() {
        super(ERROR_COUNT, ERROR_COUNT, NODE, TASKID, EXCEPTION_CLASS_NAME);
    }

    /**
     * Increments the counter by 1
     *
     * @param taskId
     *            Optional and refers to the stream processor's current task id.
     *            Defaults to N/A
     * @param source
     * @param exceptionClassName
     */
    public void incErrorCounter(Optional<String> taskId, Class<?> exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        }
        else {
            tid = NA;
        }
        inc(nodeName, tid, exceptionClassName.getName());
    }

    /**
     * Increments the counter with value specified
     *
     * @param value
     * @param taskId
     *            Optional and refers to the stream processor's current task id.
     *            Defaults to N/A
     * @param source
     * @param exceptionClassName
     */
    public void incErrorCounter(double value, Optional<String> taskId, Class<?> exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        }
        else {
            tid = NA;
        }
        inc(value, nodeName, tid, exceptionClassName.getName());
    }

    /**
     * This returns the error count grouped by nodename, taskId if present and
     * exceptionClassName
     *
     * @param taskId
     *            Optional and refers to the stream processor's current task id.
     *            Defaults to N/A
     * @param exceptionClassName
     * @return
     */
    public double getErrorCounterValue(Optional<String> taskId, Class<?> exceptionClassName) {
        String tid = null;
        if (taskId.isPresent()) {
            tid = taskId.get();
        }
        else {
            tid = NA;
        }
        return get(nodeName, tid, exceptionClassName.getName());
    }
    
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }

}
