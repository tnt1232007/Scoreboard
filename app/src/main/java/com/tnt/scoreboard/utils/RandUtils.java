package com.tnt.scoreboard.utils;

import org.joda.time.DateTime;

import java.util.Random;

public final class RandUtils {

    private static Random rand = new Random();

    public static int nextInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static <T> T nextItem(T[] objects) {
        return objects[nextInt(0, objects.length - 1)];
    }

    public static DateTime nextDateTime(int yearsAgo) {
        long start = new DateTime().minusYears(yearsAgo).getMillis();
        long end = new DateTime().getMillis();
        long diff = end - start + 1;
        return new DateTime(start + (long) (rand.nextDouble() * diff));
    }
}
