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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class has utility methods which helps in deciding if Object[] args (AKS var-args) has Throwable. <br>
 * The methods of this class is used in case decide()'s Throwable arg is null,
 * but still we can get Throwable as part of var-args.
 *
 * @author vkoul
 */
public class LoggerUtils {

    private LoggerUtils() {

    }
    
    private static final String CURLYBRACES_REGEX = "\\{\\}";
    private static final Pattern CURLYBRACES_PATTERN = Pattern.compile(CURLYBRACES_REGEX);

    /**
     * Helper method to identify if the last element of object[] is throwable or not.
     *
     * @param format log message format
     * @param args var-args
     * @return true if last element of args is Throwable, else false.
     */
    public static boolean hasThrowableObject(String format, Object[] args) {

        boolean hasThrowable = false;
        if (null == args || args.length < 1) {
            return false;
        }
        int curlyBracesCount = getCurlyBracesCount(format);
        // check if the counts are unequal and curly brace count should exactly
        // be 1 less than arguments length
        if (curlyBracesCount != args.length 
                && (curlyBracesCount == (args.length - 1)) 
                && (args[args.length - 1] instanceof Throwable)) {
            hasThrowable = true;    
        }
        return hasThrowable;
    }

    /**
     * Helper method to retrieve the number of curly braces from the format.
     *
     * @param format log message format
     * @return count of curly braces
     */
    private static int getCurlyBracesCount(String format) {
        Matcher m = CURLYBRACES_PATTERN.matcher(format);
        // we are trying to find the pattern {} and group count will always give
        // 0.
        // Hence, we will iterate and get the count
        int count = 0;

        while (m.find()) {
            count++;
        }
        return count;
    }

}
