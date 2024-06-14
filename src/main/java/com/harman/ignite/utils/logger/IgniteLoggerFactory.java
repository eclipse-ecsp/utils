package com.harman.ignite.utils.logger;

/**
 * @author AKumar
 *
 */
public class IgniteLoggerFactory {

    private IgniteLoggerFactory() {
    }
    public static <T> IgniteLogger getLogger(Class<T> clazz) {
        return IgniteLoggerImpl.getIgniteLoggerInstance(clazz);
    }

}
