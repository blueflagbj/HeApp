package com.ai.cmcchina.crm.func;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.ai.cmcchina.crm.util.StringUtil;
import com.wade.mobile.app.MobileUtil;
import com.wade.mobile.app.SimpleUpdate;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.util.Constant;
import org.json.JSONArray;

/* renamed from: com.ai.cmcchina.crm.func.MobileOpenApp */
public class MobileOpenApp extends Plugin {
    public MobileOpenApp(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void openNative(JSONArray param) throws Exception {
        IData appInfo = new DataMap(param.getString(0));
        String packageName = appInfo.getString("packageName", "");
        String className = appInfo.getString("className", "");
        String downloadUrl = appInfo.getString("downloadUrl", "");
        String url = appInfo.getString(Constant.MobileWebCacheDB.URL_COLUMN, "");
        String title = appInfo.getString("title");
        String icon = appInfo.getString(Constant.WadeMobileActivity.ICON);
        String fromWhichSubsystem = appInfo.getString(com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
        if (MobileUtil.checkActivity(this.context, packageName, className)) {
            ComponentName cn = new ComponentName(packageName, className);
            Intent intent = new Intent();
            intent.setComponent(cn);
            if (StringUtil.isNotBlank(url)) {
                intent.putExtra(Constant.MobileWebCacheDB.URL_COLUMN, url);
            }
            if (StringUtil.isNotBlank(title)) {
                intent.putExtra("title", title);
            }
            if (StringUtil.isNotBlank(icon)) {
                intent.putExtra(Constant.WadeMobileActivity.ICON, icon);
            }
            if (StringUtil.isNotBlank(fromWhichSubsystem)) {
                if (StringUtil.isNotBlank(fromWhichSubsystem)) {
                    SharedPrefHelper spHelper = new SharedPrefHelper(this.context);
                    spHelper.remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
                    spHelper.put(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem, (Object) fromWhichSubsystem);
                } else {
                    new SharedPrefHelper(this.context).remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
                }
            }
            try {
                if ("index".equals(appInfo.getString("openType", ""))) {
                    startActivityForResult(intent, 100);
                } else {
                    startActivityForResult(intent, 0);
                }
            } catch (Exception e) {
                Log.e(TAG, "openNative: ",e);
            }
        } else {
            HintHelper.tip(this.context, "正在下载，请稍后...", 1);
            new SimpleUpdate(this.context, downloadUrl).update();
        }
    }

    public void openBrowser(JSONArray param) throws Exception {
        this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(param.getString(0))));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 100) {
        }
    }
}