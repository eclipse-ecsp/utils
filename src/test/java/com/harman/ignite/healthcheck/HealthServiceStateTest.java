package com.harman.ignite.healthcheck;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HealthServiceStateTest {

@InjectMocks
private HealthServiceState serviceState;

private double state;
private String message;

@Before
public void setup() {
	MockitoAnnotations.initMocks(this);
	state = 0;
	message = "Test message";
}

@Test
public void testSetState() {
serviceState.setState(state);
Assert.assertEquals(0, serviceState.getState(),0);
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
	
	Assert.assertEquals(serviceState1,(serviceState2));
	
	serviceState1.setState(0);
	Assert.assertNotEquals(serviceState1,(serviceState2));
	
	serviceState2.setMessage("test");
	Assert.assertNotNull(String.valueOf(serviceState1),(serviceState2));
	
	serviceState2.setMessage(null);
	Assert.assertNotEquals(serviceState1,(serviceState2));
	Assert.assertNotNull(serviceState1);
	Assert.assertNotEquals(serviceState1,(new ArrayList<String>()));
	
	serviceState1.setMessage(null);
	serviceState2.setMessage("test");
	Assert.assertNotEquals(serviceState1,(serviceState2));
}

@Test
public void testHashCode() {
	serviceState.setState(0);
	serviceState.setMessage(message);
	Assert.assertNotEquals(serviceState.hashCode() , 0);
}
}
