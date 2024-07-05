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

package com.harman.ignite.healthcheck;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for thread related operations.
 */
public abstract class ThreadUtils {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(ThreadUtils.class);

    private ThreadUtils() {
    }
    
    /**
     * Shuts down an executor reliably. Optionally allows shutting down the JVM if executor doesn't shutdown
     *
     * @param exec       Executor service to shut down
     * @param waitTimeMs Time to wait for executor to shut down
     * @param exitOnFailure Exit the JVM if executor doesn't shut down
     */
    public static void shutdownExecutor(ExecutorService exec, int waitTimeMs, boolean exitOnFailure) {
        if (exec != null && !exec.isShutdown()) {
            LOGGER.info("Shutting down executor service");
            exec.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!exec.awaitTermination(waitTimeMs, TimeUnit.MILLISECONDS)) {
                    LOGGER.info("Shutting down executor service forcefully,"
                            + " as it has not responded to graceful shutdown");
                    exec.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    execWithAwaitTermination(exec, waitTimeMs, exitOnFailure);
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                exec.shutdownNow();
                try {
                    execWithAwaitTermination(exec, waitTimeMs, exitOnFailure);
                } catch (InterruptedException e) {
                    logErrorAndExit(exitOnFailure);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static void execWithAwaitTermination(ExecutorService exec, int waitTimeMs, boolean exitOnFailure)
            throws InterruptedException {
        if (!exec.awaitTermination(waitTimeMs, TimeUnit.MILLISECONDS)) {
            LOGGER.error("Executor service not closed after waiting {} ms", waitTimeMs);
            if (exitOnFailure) {
                LOGGER.error("Executor service not closed after waiting {} ms . Exiting application", waitTimeMs);
                System.exit(1);
            }
        }
    }

    private static void logErrorAndExit(boolean exitOnFailure) {
        LOGGER.error("Interrupted when waiting on executor");
        if (exitOnFailure) {
            LOGGER.error("Executor service shutdown failed. Interrupted. Exiting application");
            System.exit(1);
        }
    }
}
