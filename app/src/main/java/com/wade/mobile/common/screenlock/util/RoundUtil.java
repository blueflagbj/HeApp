package com.wade.mobile.common.screenlock.util;

public class RoundUtil {
    public static boolean checkInRound(float sx, float sy, float r, float x, float y) {
        return Math.sqrt((double) (((sx - x) * (sx - x)) + ((sy - y) * (sy - y)))) < ((double) r);
    }
}