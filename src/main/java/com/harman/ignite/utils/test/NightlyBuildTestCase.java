package com.harman.ignite.utils.test;

/**
 * Marker interface to tag a JUnit4 test case to run for nightly build.
 * Typically, a long running test case is tagged for nightly build process.
 * <p>
 * This interface is specified in Maven pom.xml's &ltexcludeGroups/&gt XML tag
 * to exclude the test cases.
 * <p>
 * Usage: Tag a test method or class,
 * <p>
 * <ul>
 * <li>@org.junit.Test @org.junit.experimental.categories.Category(NightlyBuildTestCase.class)
 * public void testFoo(){...}</li>
 * <li>@org.junit.experimental.categories.Category(NightlyBuildTestCase.class)
 * public class Foo{...}</li>
 * </ul>
 * 
 * @author KJalawadi
 *
 */
public interface NightlyBuildTestCase {
}
