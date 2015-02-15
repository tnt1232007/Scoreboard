package com.tnt.scoreboard.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Random;

public final class RandUtils {

    private static Random rand = new Random();

    public static int nextInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static <T> T nextItem(T[] objects) {
        return objects[nextInt(0, objects.length - 1)];
    }

    public static Date nextDate() {
        long offset = Timestamp.valueOf("2010-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp stamp = new Timestamp(offset + (long) (rand.nextDouble() * diff));
        return new Date(stamp.getTime());
    }
}
