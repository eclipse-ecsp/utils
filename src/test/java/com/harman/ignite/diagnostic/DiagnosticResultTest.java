package com.harman.ignite.diagnostic;

import org.junit.Assert;
import org.junit.Test;

public class DiagnosticResultTest {
private DiagnosticResult res;

@Test
public void testPassAndFail() {
	Assert.assertEquals(1.0, DiagnosticResult.PASS.getValue(),0);
	Assert.assertEquals(0.0,DiagnosticResult.FAIL.getValue(),0);
}
}
