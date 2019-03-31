package br.com.furb.util;

import java.util.Random;

public class RandomUtil {

    public static long nextLong(long minValue, long maxValue) {
        long randomMs = new Random().longs(minValue, maxValue).findAny().getAsLong();
        return randomMs;
    }

}
