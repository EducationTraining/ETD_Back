package com.etd.etdservice.utils;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class StringUtil {

    /**
     * Generate a random string starting with a seed
     * @param seed prefix of the result
     * @return a random String
     */
    public static String generateRandomString(String seed) {
        return seed + RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * generate a random phoneNumber
     * @return random phoneNumber
     */
    public static String generatePhoneNumbers() {
        Random random = new Random();
        String s = String.valueOf(random.nextInt(6) + 3);
        return "1" + s + generateRandomNumbers("", 9);
    }

    /**
     * Generate a random string with numbers as suffix
     * @param seed prefix of the result
     * @return a random string
     */
    public static String generateRandomNumbers(String seed) {
        return generateRandomNumbers(seed, 10);
    }

    /**
     * generate a random numeric string
     * @param length string length
     */
    public static String generateRandomNumbers(int length) {
        return generateRandomNumbers("", length);
    }

    /**
     * Generate a random string with numbers as suffix
     * @param seed original string
     * @param suffixLength suffix's length
     * @return
     */
    public static String generateRandomNumbers(String seed, int suffixLength) {
        return seed + RandomStringUtils.randomNumeric(suffixLength);
    }
}
