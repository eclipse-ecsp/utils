package com.harman.ignite.utils.metrics;

/**
 * The default Ignite Counter implementation
 * 
 * @author avadakkootko
 *
 */
public class GenericIgniteCounter extends AbstractIgniteCounter {

    public GenericIgniteCounter(String name, String help, String... labels) {
        createCounter(name, help, labels);
    }

}
