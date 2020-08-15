package com.wade.mobile.func;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.FuncConstant;
import com.wade.mobile.util.Messages;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TimeZone;
import org.json.JSONArray;

public class MobileInfo extends Plugin {
    public String platform = "Android";

    public MobileInfo(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void getTerminalType(JSONArray params) throws Exception {
        callback("a");
    }

    public void getSysInfo(JSONArray params) throws Exception {
        if (params.length() < 1) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        String key = params.getString(0).toUpperCase();
        if (key.equals(FuncConstant.PLATFORM)) {
            callback(getPlatform());
        } else if (key.equals(FuncConstant.IMEI)) {
            callback(getImei());
        } else if (key.equals(FuncConstant.UUID)) {
            callback(getUuid());
        } else if (key.equals(FuncConstant.SIMNUMBER)) {
            callback(getSimNumber());
        } else if (key.equals(FuncConstant.IMSI)) {
            callback(getImsi());
        } else if (key.equals(FuncConstant.OSVERSION)) {
            callback(getOSVersion());
        } else if (key.equals(FuncConstant.SDKVERSION)) {
            callback(getSDKVersion());
        } else if (key.equals(FuncConstant.TIMEZONEID)) {
            callback(getTimeZoneID());
        } else if (key.equals(FuncConstant.MODEL)) {
            callback(getModel());
        } else if (key.equals(FuncConstant.MANUFACTURER)) {
            callback(getManufacturer());
        } else if (!key.equals(FuncConstant.ALL)) {
            callback(Messages.NO_INFO);
        }
    }

    public String getAll() throws SocketException {
        IData data = new DataMap();
        data.put(FuncConstant.PLATFORM, getPlatform());
        data.put(FuncConstant.IMEI, getImei());
        data.put(FuncConstant.UUID, getUuid());
        data.put(FuncConstant.SIMNUMBER, getSimNumber());
        data.put(FuncConstant.IMSI, getImsi());
        data.put(FuncConstant.OSVERSION, getOSVersion());
        data.put(FuncConstant.SDKVERSION, getSDKVersion());
        data.put(FuncConstant.TIMEZONEID, getTimeZoneID());
        data.put(FuncConstant.MODEL, getModel());
        data.put(FuncConstant.MANUFACTURER, getManufacturer());
        data.put(FuncConstant.MAC, getMac());
        data.put(FuncConstant.f9682IP, getIP());
        data.put(FuncConstant.BRAND, getBrand());
        return data.toString();
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getUuid() {
        return Settings.Secure.getString(this.context.getContentResolver(), "android_id");
    }

    @SuppressLint("MissingPermission")
    public String getTelNumber() {
        return ((TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }

    @SuppressLint("MissingPermission")
    public String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager)this.context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei="";
        if (Build.VERSION.SDK_INT >= 26) {
            imei=telephonyManager.getImei();
        }
        else
        {
            imei=telephonyManager.getDeviceId();
        }
        return imei;
    }

    @SuppressLint("MissingPermission")
    public String getSimNumber() {
        return ((TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
    }

    @SuppressLint("MissingPermission")
    public String getImsi() {
        return ((TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    public String getTimeZoneID() {
        return TimeZone.getDefault().getID();
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getBrand() {
        return Build.BRAND;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getProductName() {
        return Build.PRODUCT;
    }

    public void getNetInfo(JSONArray params) throws Exception {
        if (params.length() < 1) {
            throw new Exception(Messages.EXCEPTION_PARAM);
        }
        String key = params.getString(0).toUpperCase();
        if (key.equals(FuncConstant.MAC)) {
            callback(getMac());
        } else if (key.equals(FuncConstant.f9682IP)) {
            callback(getIP());
        }
    }

    private String getMac() {

        return ((WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    private String getIP() throws SocketException {
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
            while (true) {
                if (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        return null;
    }
}