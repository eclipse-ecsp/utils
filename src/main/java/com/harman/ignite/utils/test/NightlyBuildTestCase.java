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

package com.harman.ignite.utils.test;

/**
 * Marker interface to tag a JUnit4 test case to run for nightly build.
 * Typically, a long-running test case is tagged for nightly build process.
 * <p>This interface is specified in Maven pom.xml's &ltexcludeGroups/&gt XML tag to exclude the test cases.</p>
 * <p>Usage: Tag a test method or class,</p>
 * <ul>
 * <li>@org.junit.Test @org.junit.experimental.categories.Category(NightlyBuildTestCase.class)
 * public void testFoo(){...}</li>
 * <li>@org.junit.experimental.categories.Category(NightlyBuildTestCase.class)
 * public class Foo{...}</li>
 * </ul>
 *
 * @author KJalawadi
 */
public interface NightlyBuildTestCase {
}
