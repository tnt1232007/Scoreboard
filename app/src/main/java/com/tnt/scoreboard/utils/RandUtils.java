package com.tnt.scoreboard.utils;

import java.util.Random;

public final class RandUtils {

    private static Random rand = new Random();

    public static int nextInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
