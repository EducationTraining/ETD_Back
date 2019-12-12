package com.etd.etdservice.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {

    /**
     * Generate a random string starting with a seed
     * @param seed prefix of the result
     * @return a random String
     */
    public static String generateRandomString(String seed) {
        return seed + RandomStringUtils.randomAlphanumeric(10);
    }
}
