package com.ai.cmcchina.crm.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/* renamed from: com.ai.cmcchina.crm.util.LocationUtil */
public class LocationUtil implements LocationListener {
    private static LocationUtil instance;
    private static LocationManager manager;
    public Context context;
    public CustomLocationListener locationListener;

    /* renamed from: com.ai.cmcchina.crm.util.LocationUtil$CustomLocationListener */
    public interface CustomLocationListener {
        void onFailed(String str);

        void onSuccess(Location location);
    }

    public LocationUtil(Context context2) {
        this.context = context2;
    }

    public static LocationUtil getInstance(Context context2) {
        if (instance == null) {
            instance = new LocationUtil(context2);
            manager = (LocationManager) context2.getSystemService(Context.LOCATION_SERVICE);
            Log.d(TAG, "getInstance: 1");
        }
        return instance;
    }

    public void setOnLocationListener(CustomLocationListener locationListener2) {
        this.locationListener = locationListener2;
        getCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        try {
            Log.d(TAG, "LocationUtil.getCurrentLocation:-------开始获取位置信息-----");
            String providerName = judgeProvider(manager);
            Log.d(TAG, "使用的provider Name: " + providerName);
            if (!StringUtil.isNotBlank(providerName)) {
                Log.d(TAG, "LocationUtil.getCurrentLocation 没有可用的位置提供器");
                this.locationListener.onFailed("获取位置信息失败，请打开定位服务!");
            } else if (PermissionUtil.requestLocationPermissions(this.context)) {
                Location lastKnownLocation = manager.getLastKnownLocation(providerName);
                if (lastKnownLocation != null) {
                    Log.d(TAG, "获取上次的位置信息 getLastKnownLocation: " + lastKnownLocation.toString());
                    this.locationListener.onSuccess(lastKnownLocation);
                    return;
                }
                Log.d(TAG, "发送更新位置信息请求 requestLocationUpdates");
                manager.requestLocationUpdates(providerName, 0, 0.0f, this);
            } else {
                this.locationListener.onFailed("获取位置信息失败，请打开位置权限！");
                Log.d(TAG, "LocationUtil.getCurrentLocation：获取位置信息失败，请打开位置权限！");
            }
        } catch (Exception e) {
            Log.e(TAG, "getCurrentLocation: ",e);
            this.locationListener.onFailed("获取位置信息失败");
        }
    }

    private static String judgeProvider(LocationManager manager2) {
        String provider;
        List<String> providers = manager2.getProviders(true);
        Log.d(TAG, "位置提供器providers.size()=" + providers.size());
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }else{
            Log.d(TAG, "LocationUtil.judgeProvider 没有可用的位置提供器");
            provider ="" ;
        }
        return provider;
    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged：获取位置信息成功");
        this.locationListener.onSuccess(location);
        removeUpdates();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "位置提供器已更改：provider-" + provider);
        Log.d(TAG, "位置提供器已更改：status-" + status);
        Log.d(TAG, "位置提供器已更改：extras-" + extras.keySet().toString());
    }

    public void onProviderEnabled(String provider) {
        Log.d(TAG, "位置提供器已生效: onProviderEnabled--" + provider);
    }

    public void onProviderDisabled(String provider) {
        Log.d(TAG, "位置提供器已失效：onProviderDisabled --" + provider);
        this.locationListener.onFailed("获取位置失败");
        removeUpdates();
    }

    private void removeUpdates() {
        if (manager != null) {
            manager.removeUpdates(this);
        }
    }

    public static boolean checkLocation(Object object) {
        String lat;
        String lng;
        if (object instanceof Location) {
            Location location = (Location) object;
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
        } else if (!(object instanceof BDLocation)) {
            return false;
        } else {
            BDLocation bdLocation = (BDLocation) object;
            lat = String.valueOf(bdLocation.getLatitude());
            lng = String.valueOf(bdLocation.getLongitude());
        }
        if (!lat.contains("E")) {
            return true;
        }
        Log.d(TAG, "经纬度信息异常：" + lat + "--" + lng);
        return false;
    }
}