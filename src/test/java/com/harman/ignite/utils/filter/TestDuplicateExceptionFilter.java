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

package com.harman.ignite.utils.filter;

import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.harman.ignite.utils.logger.LoggerUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test case is used to test the filter DuplicateExceptionFilter.
 *
 * @author vkoul
 */
public class TestDuplicateExceptionFilter {

    private static final long SIXTY_THOUSAND = 60000L;
    private static final long LONG_VALUE_FOR_LOGGER = 2628077220L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDuplicateExceptionFilter.class);
    private DuplicateExceptionFilter def = null;
    @Mock
    private LoggerUtils loggerUtils;

    /**
     * Setup the test.
     */
    @Before
    public void setUp() {
        def = new DuplicateExceptionFilter();
        def.setSuppressTimeInMS(SIXTY_THOUSAND);
        def.start();
    }

    /**
     * This test case calls decide() twice within a minute.
     * <br>
     * First invocation to decide() should return NEUTRAL whereas the next invocation should return DENY,
     * as it is occurring within a minute, for which suppression should happen.
     */
    @Test
    public void testExceptionSuppressed() {

        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.NEUTRAL);

        // Now, next log is within one minute, so it should be DENY
        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.DENY);
        assertThat(def.decide(null, (ch.qos.logback.classic.Logger) LOGGER,
                null,
                "",
                new Object[] { new IOException() }, null))
                .isEqualTo(FilterReply.DENY);

        ConcurrentHashMap<String, Long> exceptionCache = new ConcurrentHashMap<>();
        exceptionCache.put(IOException.class.getName() + LOGGER.toString(), LONG_VALUE_FOR_LOGGER);
        def.setExceptionCache(exceptionCache);

        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.NEUTRAL);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Test
    public void testGetSuppressTimeInMS() {
        Assert.assertEquals(SIXTY_THOUSAND, def.getSuppressTimeInMS());
    }

    @After
    public void tearDown() {
        def.stop();
    }

    private FilterReply logMessage(final TurboFilter def, Throwable t) {
        return def.decide(null, (ch.qos.logback.classic.Logger) LOGGER, null, null, null, t);
    }
}