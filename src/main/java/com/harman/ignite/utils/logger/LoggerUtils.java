package com.harman.ignite.utils.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author vkoul
 * 
 *         This class has utility methods which helps in deciding if Object[]
 *         args (AKS var-args) has Throwable.
 * 
 *         The methods of this class is used in case decide()'s Throwable arg is
 *         null, but still we can get Throwable as part of var-args.
 *
 */
public class LoggerUtils {
    private LoggerUtils(){

    }
    private static final String CURLYBRACES_REGEX = "\\{\\}";
    private static final Pattern CURLYBRACES_PATTERN = Pattern.compile(CURLYBRACES_REGEX);

    /**
     * Helper method to identify if the last element of object[] is throwable or
     * not.
     *
     * @param format
     * @param args
     * @return
     */
    public static boolean hasThrowableObject(String format, Object[] args) {

        boolean hasThrowable = false;
        if (null == args || args.length < 1) {
            return false;
        }
        int curlyBracesCount = getCurlyBracesCount(format);
        // check if the counts are unequal and curly brace count should exactly
        // be 1 less than arguments length
        if (curlyBracesCount != args.length && (curlyBracesCount == (args.length - 1)) && (args[args.length - 1] instanceof Throwable)) {
                hasThrowable = true;
            
        }
        return hasThrowable;

    }

    /**
     * Helper method to retrieve the number of curly braces from the format
     *
     * @param format
     * @return
     */
    private static int getCurlyBracesCount(String format) {
        Matcher m = CURLYBRACES_PATTERN.matcher(format);
        // we are trying to find the pattern {} and groupcount will always gives
        // 0.
        // Hence we will iterate and get the count
        int count = 0;

        while (m.find()) {
            count++;
        }
        return count;

    }

}
