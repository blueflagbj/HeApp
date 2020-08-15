package com.wade.mobile.common.screenlock.view;

public class Point {
    public static int STATE_CHECK = 1;
    public static int STATE_CHECK_ERROR = 2;
    public static int STATE_NORMAL = 0;
    public int index = 0;
    public int state = 0;

    /* renamed from: x */
    public float f9678x;

    /* renamed from: y */
    public float f9679y;

    public Point() {
    }

    public Point(float x, float y) {
        this.f9678x = x;
        this.f9679y = y;
    }
}