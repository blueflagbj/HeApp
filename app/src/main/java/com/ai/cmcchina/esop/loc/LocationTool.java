package com.ai.cmcchina.esop.loc;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/* renamed from: com.ai.cmcchina.esop.loc.LocationTool */
public class LocationTool {
    /* access modifiers changed from: private */
    public static final String TAG = LocationTool.class.getName();
    private static LocationTool instance;
    private static LocationClient mLocClient;
    private Context context;
    /* access modifiers changed from: private */
    public BDLocation curBdLocation;
    /* access modifiers changed from: private */
    public FenceCalHandler fenceCalHandler;
    /* access modifiers changed from: private */
    public HandlerNotify handlerNotify;
    /* access modifiers changed from: private */
    public LocationHandler locationHandler;
    private BDAbstractLocationListener mLocListener;
    private LocationClientOption mLocOption;
    private BDNotifyListener myBdNotifyListener;
    private int timeSpan;

    /* renamed from: com.ai.cmcchina.esop.loc.LocationTool$FenceCalHandler */
    public interface FenceCalHandler {
        boolean onHandleCalFence(Double d, Double d2);
    }

    /* renamed from: com.ai.cmcchina.esop.loc.LocationTool$HandlerNotify */
    public interface HandlerNotify {
        boolean onHandlerNotify(BDLocation bDLocation, float f);
    }

    /* renamed from: com.ai.cmcchina.esop.loc.LocationTool$LocationHandler */
    public interface LocationHandler {
        void handleLocation(BDLocation bDLocation);
    }

    public LocationTool(Context context2) {
        this(context2, (LocationHandler) null);
    }

    public LocationTool(Context context2, LocationHandler handler) {
        this.timeSpan = 15000;
        this.context = context2;
        initOption();
        mLocClient = new LocationClient(context2, this.mLocOption);
        initListener();
        mLocClient.registerLocationListener(this.mLocListener);
        this.locationHandler = handler;
    }

    private static void init(Context context2) {
        if (instance == null) {
            instance = new LocationTool(context2);
        }
    }

    public static LocationTool getInstance(Context context2) {
        init(context2);
        return instance;
    }

    public void startLocation() {
        mLocClient.start();
    }

    public void stopLocation() {
        mLocClient.stop();
    }

    private void initOption() {
        this.mLocOption = new LocationClientOption();
        this.mLocOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        this.mLocOption.setCoorType("bd09ll");
        this.mLocOption.setScanSpan(this.timeSpan);
        this.mLocOption.setOpenGps(true);
        this.mLocOption.setIsNeedAddress(true);
    }

    private void initListener() {
        this.mLocListener = new BDAbstractLocationListener() {
            public void onReceiveLocation(BDLocation location) {
                Log.d(TAG, "LocationTool onReceiveLocation>>>位置更新");
                BDLocation unused = LocationTool.this.curBdLocation = location;
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Log.d(TAG, LocationTool.TAG + " longitue:" + longitude + "--latitude：" + latitude + " 定位类型：" + location.getLocType() + ";" + location.getLocTypeDescription() + location.getAddress().address);
                if (LocationTool.this.locationHandler != null) {
                    LocationTool.this.locationHandler.handleLocation(location);
                }
                if (LocationTool.this.fenceCalHandler != null) {
                    LocationTool.this.fenceCalHandler.onHandleCalFence(Double.valueOf(latitude), Double.valueOf(longitude));
                }
            }
        };
    }

    public void setLocationHandler(LocationHandler handler) {
        this.locationHandler = handler;
    }

    public BDLocation getCurLoc() {
        return this.curBdLocation;
    }

    public boolean getInOrOut(LatLng curLoc, LatLng centerLoc, Double radius) {
        return Distance.getTwoPointsDistance(String.valueOf(curLoc.longitude), String.valueOf(curLoc.latitude), String.valueOf(centerLoc.longitude), String.valueOf(centerLoc.latitude)).doubleValue() <= radius.doubleValue();
    }

    public Double getBetweenDis(LatLng curLoc, LatLng centerLoc) {
        return Distance.getTwoPointsDistance(String.valueOf(curLoc.longitude), String.valueOf(curLoc.latitude), String.valueOf(centerLoc.longitude), String.valueOf(centerLoc.latitude));
    }

    /* renamed from: com.ai.cmcchina.esop.loc.LocationTool$Distance */
    private static class Distance {
        private static double DEF_2PI = 6.283185307179586d;
        private static double DEF_PI = 3.141592653589793d;
        private static double DEF_PI180 = 0.017453292519943295d;
        private static double DEF_R = 6370996.81d;

        private Distance() {
        }

        static Double getTwoPointsDistance(String lng1Str, String lat1Str, String lng2Str, String lat2Str) {
            if (TextUtils.isEmpty(lat1Str) || TextUtils.isEmpty(lng1Str) || TextUtils.isEmpty(lat2Str) || TextUtils.isEmpty(lng2Str)) {
                return null;
            }
            Double lon1 = Double.valueOf(Double.parseDouble(lng1Str));
            Double lat1 = Double.valueOf(Double.parseDouble(lat1Str));
            Double lon2 = Double.valueOf(Double.parseDouble(lng2Str));
            Double lat2 = Double.valueOf(Double.parseDouble(lat2Str));
            double ew1 = lon1.doubleValue() * DEF_PI180;
            double ns1 = lat1.doubleValue() * DEF_PI180;
            double ew2 = lon2.doubleValue() * DEF_PI180;
            double ns2 = lat2.doubleValue() * DEF_PI180;
            double distance = (Math.sin(ns1) * Math.sin(ns2)) + (Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2));
            if (distance > 1.0d) {
                distance = 1.0d;
            } else if (distance < -1.0d) {
                distance = -1.0d;
            }
            return Double.valueOf(new BigDecimal(DEF_R * Math.acos(distance)).setScale(2, 4).doubleValue());
        }
    }

    public void setFenceHandler(FenceCalHandler fenceHandler) {
        this.fenceCalHandler = fenceHandler;
    }

    /* renamed from: com.ai.cmcchina.esop.loc.LocationTool$MyNotifyListener */
    private class MyNotifyListener extends BDNotifyListener {
        private MyNotifyListener() {
        }

        public void onNotify(BDLocation mlocation, float distance) {
            if (LocationTool.this.handlerNotify != null) {
                LocationTool.this.handlerNotify.onHandlerNotify(mlocation, distance);
            }
        }
    }

    public void setHandlerNotify(HandlerNotify handlerNotify2) {
        this.handlerNotify = handlerNotify2;
    }

    public void setNotify(Double lat, Double lng, float radius) {
        this.myBdNotifyListener.SetNotifyLocation(lat.doubleValue(), lng.doubleValue(), radius, mLocClient.getLocOption().getCoorType());
        mLocClient.registerNotify(this.myBdNotifyListener);
    }

    public static boolean outOfChina(double lat, double lon) {
        Log.d(TAG, "outOfHN>>>lat:" + lat + "--lon:" + lon);
        if (lon < 72.004d || lon > 137.8347d || lat < 0.8293d || lat > 55.8271d) {
            Log.d(TAG, "在中国范围外");
            return true;
        }
        Log.d(TAG, "在中国范围内");
        return false;
    }

    public static boolean outOfHN(double lat, double lon) {
        Log.d(TAG, "outOfHN>>>lat:" + lat + "--lon:" + lon);
        if (lon < 105.0d || lon > 120.0d || lat < 31.0d || lat > 37.0d) {
            Log.d(TAG, "在河南范围---外");
            return true;
        }
        Log.d(TAG, "在河南范围---内");
        return false;
    }

    public static String formatDouble(Double num, int length) {
        String result;
        if (num == null) {
            return "";
        }
        try {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMaximumFractionDigits(length);
            format.setRoundingMode(RoundingMode.UP);
            result = format.format(num);
        } catch (Exception e) {
            result = num.toString();
            Log.d(TAG, "formatDouble:（Double数据保留小数转换）:" + num);
            Log.d(TAG, "formatDouble:Exception>>>" + e.getStackTrace().toString());
        }
        Log.d(TAG, "formatDouble:（Double数据保留小数转换）:num = " + num + " ;result = " + result);
        return result;
    }
}