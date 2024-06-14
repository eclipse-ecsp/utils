package com.harman.ignite.healthcheck;

/**
 * Enables the application implementing health service to take appropriate
 * action based on health critical health monitor failure
 * 
 * @author avadakkootko
 *
 */
public interface HealthServiceCallBack {

    /**
     * 
     * @return true if a restart is required, else false
     */
    public boolean performRestart();

}
