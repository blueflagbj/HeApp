package com.ai.cmcchina.crm.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

/* renamed from: com.ai.cmcchina.crm.util.PermissionUtil */
public class PermissionUtil {
    public static final int REQUEST_CAMERA = 2;
    public static final int REQUEST_LOCATION = 1;
    public static final int REQUEST_PHONE_STATE = 3;

    public static boolean selfPermissionGranted(Context context, String permission) {
        boolean result = true;
        Log.d(TAG, "检查手机是否配置该权限: PermissionUtil.selfPermissionGranted");
        Log.d(TAG, "permission: " + permission);
        if (context.getApplicationInfo().targetSdkVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                result = false;
            }
        } else if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
            result = false;
        }
        Log.d(TAG, "permission -result: " + result);
        return result;
    }

    public static void requestPermissions(@NonNull Activity activity, @NonNull ArrayList<String> permissions, int requestCode) {
        Log.d(TAG, "添加权限: PermissionUtil.requestPermissions");
        Log.d(TAG, "permissions:" + permissions.toString());
        if (permissions.size() != 0) {
            ActivityCompat.requestPermissions(activity, (String[]) permissions.toArray(new String[permissions.size()]), requestCode);
        } else {
            Log.d(TAG, "requestPermissions permissions.size() == 0");
        }
    }

    public static void openPermissionSetting(Context context) {
        Log.d(TAG, "开应用的设置页面：PermissionUtil.openPermissionSetting");
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), (String) null));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "开应用的设置页面失败：" + e.getStackTrace().toString());
        }
    }

    public static void onRequestPermissionsResult(Context context, String[] permissions, int[] grantResults, String keyMsg) {
        Log.d(TAG, "处理权限添加回调结果：PermissionUtil.onRequestPermissionsResult");
        Log.d(TAG, "permissions:" + permissions.toString());
        Log.d(TAG, "grantResults:" + grantResults.toString());
        boolean flag = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[0] == 0) {
                flag = true;
            } else {
                flag = false;
            }
        }
        if (!flag) {
            showPermissionConfirmDialog(context, keyMsg);
        }
    }

    public static void showPermissionConfirmDialog(Context context, String keyMsg) {
        String msg = "和助手缺少必要权限，请点击设置-权限，打开所需权限！";
        if (!TextUtils.isEmpty(keyMsg)) {
            msg = "和助手缺少必要【" + keyMsg + "】权限，请打开应用管理-权限管理，添加所需权限！";
        }
        new AlertDialog.Builder(context).setTitle("权限提醒").setMessage(msg).setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public static boolean initLocationPermissions(Context context) {
        ArrayList<String> permissionList = new ArrayList<>();
        if (!selfPermissionGranted(context, "android.permission.ACCESS_FINE_LOCATION")) {
            permissionList.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!selfPermissionGranted(context, "android.permission.ACCESS_COARSE_LOCATION")) {
            permissionList.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionList.size() <= 0) {
            return true;
        }
        showPermissionConfirmDialog(context, "位置");
        return false;
    }

    public static boolean requestLocationPermissions(Context context) {
        ArrayList<String> permissionList = new ArrayList<>();
        if (!selfPermissionGranted(context, "android.permission.ACCESS_FINE_LOCATION")) {
            permissionList.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!selfPermissionGranted(context, "android.permission.ACCESS_COARSE_LOCATION")) {
            permissionList.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionList.size() <= 0) {
            return true;
        }
        requestPermissions((Activity) context, permissionList, 1);
        return false;
    }

    public static boolean initCameraPermissions(Context context) {
        ArrayList<String> permissionList = new ArrayList<>();
        if (!selfPermissionGranted(context, "android.permission.CAMERA")) {
            permissionList.add("android.permission.CAMERA");
        }
        if (permissionList.size() <= 0) {
            return true;
        }
        showPermissionConfirmDialog(context, "拍照");
        return false;
    }

    public static boolean initPhoneStatePermissions(Context context) {
        Log.d(TAG, "检查手机是否配置该权限-读取手机状态权限: PermissionUtil.initPhoneStatePermissions");
        ArrayList<String> permissionList = new ArrayList<>();
        if (!selfPermissionGranted(context, "android.permission.READ_PHONE_STATE")) {
            permissionList.add("android.permission.READ_PHONE_STATE");
        }
        if (permissionList.size() <= 0) {
            return true;
        }
        requestPermissions((Activity) context, permissionList, 3);
        return false;
    }
}