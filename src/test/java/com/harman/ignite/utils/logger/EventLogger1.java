/**
 * 
 */
package com.harman.ignite.utils.logger;

/**
 * @author AKumar
 *
 */
public class EventLogger1 implements Runnable {
    private static IgniteLogger igniteLogger = IgniteLoggerFactory.getLogger(EventLogger1.class);

    public static IgniteLogger getIgniteLogger() {
        return igniteLogger;
    }
    @Override
    public void run() {
        igniteLogger.info("Info message from EventLogger1");
        igniteLogger.trace("Info message from EventLogger1");
        igniteLogger.debug("Info message from EventLogger1");
        igniteLogger.error("Error message from EventLogger1", new Exception("exception occurred"));
    }

}
