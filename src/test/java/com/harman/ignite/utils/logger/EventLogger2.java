/**
 * 
 */
package com.harman.ignite.utils.logger;

/**
 * @author AKumar
 *
 */
public class EventLogger2 implements Runnable{

    private static IgniteLogger igniteLogger = IgniteLoggerFactory.getLogger(EventLogger2.class);
    
    @Override
    public void run() {
            igniteLogger.info("Info message from EventLogger2");
            igniteLogger.trace("Info message from EventLogger2");
            igniteLogger.debug("Info message from EventLogger2");
            igniteLogger.error("Error message from EventLogger2", new Exception("exception occurred"));
    }

}
