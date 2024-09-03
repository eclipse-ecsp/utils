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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/** Test class for HealthServiceState.
 *
 * @see HealthServiceState
 */
public class HealthServiceStateTest {

    @InjectMocks
    private HealthServiceState serviceState;

    private double state;
    private String message;

    /**
     * Setup the test.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        state = 0;
        message = "Test message";
    }

    @Test
    public void testSetState() {
        serviceState.setState(state);
        Assert.assertEquals(0, serviceState.getState(), 0);
    }

    @Test
    public void testSetMessage() {
        serviceState.setMessage(message);
        Assert.assertEquals("Test message", serviceState.getMessage());
    }

    @Test
    public void testEquals() {
        HealthServiceState serviceState1 = new HealthServiceState();
        serviceState1.setState(1);
        serviceState1.setMessage("Unhealthy");
        HealthServiceState serviceState2 = new HealthServiceState();
        serviceState2.setState(1);
        serviceState2.setMessage("Unhealthy");
        Assert.assertEquals(serviceState1, (serviceState2));
        serviceState1.setState(0);
        Assert.assertNotEquals(serviceState1, (serviceState2));
        serviceState2.setMessage("test");
        Assert.assertNotNull(String.valueOf(serviceState1), (serviceState2));
        serviceState2.setMessage(null);
        Assert.assertNotEquals(serviceState1, (serviceState2));
        Assert.assertNotNull(serviceState1);
        Assert.assertNotEquals(serviceState1, (new ArrayList<String>()));
        serviceState1.setMessage(null);
        serviceState2.setMessage("test");
        Assert.assertNotEquals(serviceState1, (serviceState2));
    }
        

    @Test
    public void testHashCode() {
        serviceState.setState(0);
        serviceState.setMessage(message);
        Assert.assertNotEquals(serviceState.hashCode(), 0);
    }
}
