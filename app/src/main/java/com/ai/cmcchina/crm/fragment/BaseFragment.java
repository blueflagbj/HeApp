package com.ai.cmcchina.crm.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ai.cmcchina.crm.bean.Result;
import com.ai.cmcchina.crm.constant.Constant;
import com.ai.cmcchina.crm.handler.CRMRemoteHandler;
import com.ai.cmcchina.crm.listener.AfterListener;
import com.ai.cmcchina.crm.ui.BaseActivity;

import com.ai.cmcchina.crm.util.Global;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.crm.view.AlertDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ai.cmcchina.crm.fragment.BaseFragment */
public abstract class BaseFragment extends Fragment {
    public static int GET_IDCARD_NUM = 1038;
    public static int GET_SN_REQUEST_CODE = 1033;
    public static int NO_PAPER_SUCC = 1036;
    public static int OPEN_GUARDIAN_PAGE = 1039;
    public static int READ_ID_CARD = 1035;
    public static int REQUEST_CODE = 1032;
    public static int TACK_IDCARD_PHOTO_AGAION = 1037;
    public static int WRITE_CARD_REQUEST_CODE = 1034;
    protected static String titleName = "";
    protected boolean back;
    protected boolean loaded;
    protected CharSequence title;

    public BaseFragment() {
        this.back = true;
    }

    public BaseFragment(CharSequence title2) {
        this.title = title2;
    }

    public BaseFragment(CharSequence title2, boolean showBack) {
        this.title = title2;
        this.back = showBack;
    }

    public boolean showBack() {
        return this.back;
    }

    public CharSequence getTitle() {
        return this.title;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).addWatermark();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void addFragment(BaseFragment fragment, String tag) {
        ((BaseActivity) getActivity()).addFragment(fragment, tag);
    }

    /* access modifiers changed from: protected */
    public void remove(BaseFragment fragment) {
        ((BaseActivity) getActivity()).remove(fragment);
    }

    /* access modifiers changed from: protected */
    public void popFragment() {
        ((BaseActivity) getActivity()).popBackStack();
    }

    /* access modifiers changed from: protected */
    public void getSignature(String telephoneNum, String account, AfterListener afterListener) {
        Map<String, String> params = new HashMap<>();
        params.put("BILL_ID", telephoneNum);
        params.put("OPTRID", account);
        final AfterListener afterListener2 = afterListener;
        new CRMRemoteHandler(getActivity(), "/front/sh/business!getSignature", params, true) {
            public void onSuccess(String result) {
                afterListener2.doAction(result);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void call10085(String transactionId, String signatureCharSequence, String channelId, String phone, String provinceCode, String account, String nodeCode) {
        open10085(transactionId, signatureCharSequence, channelId, phone, provinceCode, account, nodeCode, "");
    }

    /* access modifiers changed from: protected */
    public void call10085(String transactionId, String signatureCharSequence, String channelId, String phone, String provinceCode, String account, String nodeCode, String newCardFlag) {
        open10085(transactionId, signatureCharSequence, channelId, phone, provinceCode, account, nodeCode, newCardFlag);
    }

    /* access modifiers changed from: protected */
    public void open10085(String transactionId, String signatureCharSequence, String channelId, String phone, String provinceCode, String account, String nodeCode, String newCardFlag) {
        try {
            getActivity().getPackageManager().getPackageInfo("com.asiainfo.cm10085", PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.asiainfo.cm10085", "com.asiainfo.cm10085.IdentityAuthenticationActivity"));
            intent.putExtra("transactionID", transactionId);
            intent.putExtra("billId", phone);
            intent.putExtra("account", account);
            intent.putExtra("channelCode", channelId);
            intent.putExtra("provinceCode", provinceCode);
            intent.putExtra("signature", signatureCharSequence);
            intent.putExtra("mode", "3");
            intent.putExtra("newCardFlag", newCardFlag);
            intent.putExtra("nodeCode", nodeCode);
            getActivity().startActivityForResult(intent, REQUEST_CODE);
        } catch (PackageManager.NameNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("注意");
            builder.setMessage("本机未安装10085实名信息采集app，是否下载？");
            builder.setPositiveButton("下载", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                @TargetApi(9)
                public void onClick(DialogInterface dialog, int which) {
                    BaseFragment.this.dowanLoadApk();
                    if (Build.VERSION.SDK_INT > 9) {
                        Intent intent1 = new Intent("android.intent.action.VIEW_DOWNLOADS");
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );//268435456
                        BaseFragment.this.getActivity().startActivity(intent1);
                    }
                }
            }).setNegativeButton("取消", (DialogInterface.OnClickListener) null);
            builder.create().show();
        }
    }

    /* access modifiers changed from: private */
    @TargetApi(11)
    public void dowanLoadApk() {
        String url = "http://smrz.realnameonline.cn:30202" + "/down/apk/smrz_nfc.apk";
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(3);
        request.setAllowedOverRoaming(true);
        if (11 > Build.VERSION.SDK_INT) {
            request.setShowRunningNotification(true);
        } else if (13 >= Build.VERSION.SDK_INT) {
            request.setNotificationVisibility(0);
        } else {
            request.setNotificationVisibility(1);
        }
        request.setVisibleInDownloadsUi(true);
        request.setTitle("10085客户端");
        final String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "10085.apk";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "10085.apk");
        request.setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url)));
        final long enqueueId = downloadManager.enqueue(request);
        IntentFilter filter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        filter.addAction("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED");
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                    if (enqueueId == intent.getExtras().getLong("extra_download_id")) {
                        BaseFragment.this.installApk(new File(filePath));
                    } else {
                        UIUtil.toast((Context) BaseFragment.this.getActivity(), (Object) "下载失败！");
                    }
                    BaseFragment.this.getActivity().unregisterReceiver(this);
                } else if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(intent.getAction()) && Build.VERSION.SDK_INT > 9) {
                    Intent intent1 = new Intent("android.intent.action.VIEW_DOWNLOADS");
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    BaseFragment.this.getActivity().startActivity(intent1);
                }
            }
        }, filter);
    }

    /* access modifiers changed from: protected */
    public void installApk(File apk) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void validateUser(String validateType, String value, AfterListener afterListener) {
        Map<String, String> params = new HashMap<>();
        if (Global.SERVICE_PASSWORD.equals(validateType)) {
            if (TextUtils.isEmpty(value)) {
                UIUtil.toast((Context) getActivity(), (Object) "请输入服务密码!");
            }
            params.put("uid", "1");
            params.put("passwd", value);
        } else {
            if (TextUtils.isEmpty(value)) {
                UIUtil.toast((Context) getActivity(), (Object) "请输入短信验证码!");
            }
            params.put("uid", Global.SERVICE_PASSWORD);
            params.put("smsCode", value);
        }
        final AfterListener afterListener2 = afterListener;
        new CRMRemoteHandler(getActivity(), "/front/sh/business!validateUser", params, true) {
            public void onSuccess(String content) {
                //G3Logger.debug(content);
                JSONObject json = JSON.parseObject(content);
                if ("0000".equals(json.getString(Constant.RETURN_CODE_KEY))) {
                    afterListener2.doAction(content);
                    return;
                }
                UIUtil.alert((Context) BaseFragment.this.getActivity(), json.getString("returnMessage"));
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void validateUserSmsAndPsw(String tel, String smsCode, String password, AfterListener afterListener) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(password)) {
            UIUtil.toast((Context) getActivity(), (Object) "请输入服务密码!");
        }
        params.put("passwd", password);
        if (TextUtils.isEmpty(smsCode)) {
            UIUtil.toast((Context) getActivity(), (Object) "请输入短信验证码!");
        }
        params.put("smsCode", smsCode);
        params.put("svcNum", tel);
        final AfterListener afterListener2 = afterListener;
        new CRMRemoteHandler(getActivity(), "/front/sh/business!validateUserSmsAndPsw", params, true) {
            public void onSuccess(String content) {
                //G3Logger.debug(content);
                JSONObject json = JSON.parseObject(content);
                if ("0000".equals(json.getString(Constant.RETURN_CODE_KEY))) {
                    afterListener2.doAction(content);
                    return;
                }
                UIUtil.alert((Context) BaseFragment.this.getActivity(), json.getString("returnMessage"));
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void sendSmsCode(String telNum, AfterListener afterListener) {
        if (StringUtil.isBlank(telNum)) {
            UIUtil.toast((Context) getActivity(), (Object) "请输入手机号码");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("type", "smsCode");
        params.put("svcNum", telNum);
        final AfterListener afterListener2 = afterListener;
        new CRMRemoteHandler(getActivity(), "/front/sh/business!sendMessage", params, true) {
            public void onSuccess(String content) {
                //G3Logger.debug(content);
                if ("0000".equals(((Result) JSON.toJavaObject(JSON.parseObject(content), Result.class)).getReturnCode())) {
                    UIUtil.toast((Context) BaseFragment.this.getActivity(), (Object) "短信验证码已发送，请注意查收");
                    afterListener2.doAction(content);
                    return;
                }
                onError("短信发送失败，请重试!");
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void createInvoice(String orderCode, String busiType, String invoiceContent, Button btn) {
        ((BaseActivity) getActivity()).createInvoice(orderCode, busiType, invoiceContent, btn);
    }

    /* access modifiers changed from: protected */
    public void createInvoice(String orderCode, String busiType, String invoiceContent, String num, String name, Button btn) {
        ((BaseActivity) getActivity()).createInvoice(orderCode, busiType, invoiceContent, num, name, btn);
    }

    /* access modifiers changed from: protected */
/*    public SimCardService getSimCardService(String type, Intent data) throws Exception {
        String deviceName = data.getStringExtra("device_name");
        G3Logger.debug("deviceName:" + deviceName);
        return DeviceTypeFactory.getSimCardService(deviceName, getActivity());
    }*/

    /* access modifiers changed from: protected */
    public void saveOrderCodeForBill(String billId, String orderCode) {
        ((BaseActivity) getActivity()).saveOrderCodeForBill(billId, orderCode);
    }

    /* access modifiers changed from: protected */
    public void getIDCardInfo() {
        ((BaseActivity) getActivity()).getIDCardInfo();
    }

    /* access modifiers changed from: protected */
    public void takeIDCardPhotoAgain() {
        ((BaseActivity) getActivity()).takeIDCardPhotoAgain();
    }

    /* access modifiers changed from: protected */
    public void readIDCardNum() {
        ((BaseActivity) getActivity()).readIDCardNum();
    }

    /* access modifiers changed from: protected */
    public void openGuardianCertifyPage(String billId) {
        ((BaseActivity) getActivity()).openGuardianCertifyPage(billId);
    }

    /* access modifiers changed from: protected */
    public void addBusinessRecordInfo(Activity activity, String busiName, String busiCode, String userName, String userTel) {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        BaseActivity.addBusinessRecordInfo(activity, busiName, busiCode, userName, userTel);
    }
}