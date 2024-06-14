package com.harman.ignite.utils.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harman.ignite.utils.logger.LoggerUtils;

import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * This test case is used to test the filter DuplicateExceptionFilter.
 * 
 * @author vkoul
 *
 */
public class TestDuplicateExceptionFilter {

    private DuplicateExceptionFilter def = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDuplicateExceptionFilter.class);
    
    @Mock
    private LoggerUtils loggerUtils;

    @Before
    public void setUp() {
        def = new DuplicateExceptionFilter();
        def.setSuppressTimeInMS(60000);
        def.start();
    }

    /**
     * This test case calls decide() twice within a minute.
     * 
     * First invocation to decide() should return NEUTRAL whereas the next
     * invocation should return DENY, as it is occurring within a minute, for
     * which suppression should happen.
     * 
     * 
     */
    @Test
    public void testExceptionSuppressed() {

        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.NEUTRAL);

        // Now, next log is within one minute, so it should be DENY
        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.DENY);
        assertThat(def.decide(null, (ch.qos.logback.classic.Logger) LOGGER, null, "", new Object[] {new IOException()}, null)).isEqualTo(FilterReply.DENY);
        
        ConcurrentHashMap<String,Long> exceptionCache = new ConcurrentHashMap<>();
        StringBuilder sb = new StringBuilder().append(IOException.class.getName()).append(LOGGER.toString());
        exceptionCache.put(sb.toString(), new Long(2628077220L));
        def.setExceptionCache(exceptionCache);
        
        assertThat(logMessage(def, new IOException())).isEqualTo(FilterReply.NEUTRAL);
    }
    
    @Test
    public void testGetSuppressTimeInMS() {
    	Assert.assertEquals(60000,def.getSuppressTimeInMS());
    }

    @After
    public void tearDown() {
        def.stop();
    }

    private FilterReply logMessage(final TurboFilter def, Throwable t) {
        return def.decide(null, (ch.qos.logback.classic.Logger) LOGGER, null, null, null, t);
    }
}