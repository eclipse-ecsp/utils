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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for IgniteLogger for warn.
 *
 * @author AKumar
 *
 */

public class TestIgniteLoggerWarn {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger logger;

    @Before
    public void setup() {
        logger = Mockito.mock(Logger.class);
        igniteLogger.setLogger(logger);
    }

    @Test
    public void testIgniteLoggersWhenWarnEnabled() {
        when(logger.isWarnEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(logger).warn(arg.capture());
        assertEquals("WarnWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenWarnDisabled() {
        when(logger.isWarnEnabled()).thenReturn(false);
        igniteLogger.warn("WarnWithMsg");
        Mockito.verify(logger, new Times(1)).warn("WarnWithMsg");
    }
}
