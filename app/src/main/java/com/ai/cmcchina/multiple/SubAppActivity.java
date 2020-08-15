package com.ai.cmcchina.multiple;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.ai.cmcchina.crm.constant.Constant;
import com.ai.cmcchina.crm.func.MobileOpenApp;
import com.ai.cmcchina.crm.util.UIUtil;
import com.heclient.heapp.App;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.activity.TemplateSubActivity;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.func.MobileUI;
import com.wade.mobile.ui.view.FlipperLayout;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

/* renamed from: com.ai.cmcchina.multiple.SubAppActivity */
public class SubAppActivity extends TemplateSubActivity {
    private static Map<String, Boolean> firstStatus = new HashMap();

    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:SubAppActivity.onCreate ");

        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        Log.d(TAG, "SubAppActivity.initActivity");
        IData param = new DataMap();
        App app = (App) getApplication();
        param.put(Constant.SESSION_ID, app.getSessionId());
        param.put("ACCOUNT", app.getAccount());
        String indexPage = getIntent().getStringExtra("INDEX_PAGE");
        String type = getIntent().getStringExtra("type");
        Log.d(TAG, "SubAppActivity initActivity(), indexPage:" + indexPage + ", type:" + type);
        if ("2".equals(type) || "3".equals(type)) {
            JSONArray params = new JSONArray();
            params.put(indexPage);
            ((MobileOpenApp) getPluginManager().getPlugin(MobileOpenApp.class)).openNative(params);
        } else {
            String loginChannel = app.getAccountChannelType();
            if (Constant.LOGIN_TYPE_PARTNER_CHANNEL.equals(loginChannel)) {
                String partnerType = app.getPartnerAccountType();
                if (Constant.PartnerChannlRole.ADMIN.getType().equals(partnerType)) {
                    indexPage = "AdminHomePage";
                } else if (Constant.PartnerChannlRole.MANAGER.getType().equals(partnerType)) {
                    indexPage = "ManagerHomePage";
                } else if (Constant.PartnerChannlRole.PARTNER.getType().equals(partnerType)) {
                    indexPage = "PartnerHomePage";
                } else if (Constant.PartnerChannlRole.PARTNER_SUB_ACCOUNT.getType().equals(partnerType)) {
                    indexPage = "PartnerSubAccountHomePage";
                }
            } else if (Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(loginChannel)) {
                String partnerType2 = app.getPartnerAccountType();
                if (Constant.PartnerChannlRole.MANAGER.getType().equals(partnerType2)) {
                    indexPage = "SocietyManagerHome";
                } else if (Constant.PartnerChannlRole.PARTNER.getType().equals(partnerType2)) {
                    indexPage = "SocietyPartnerHome";
                }
            }
            openHomePage(indexPage, param);
        }
        firstStatus.put(MultipleManager.getCurrAppId(), false);
    }

    private void openHomePage(String indexPage, IData param) throws Exception {
        System.out.println("SubAppActivity-openHomePage:0:");
        if (indexPage == null) {
            indexPage = ServerConfig.getInstance().getValue("indexPage");
        }
        final IData iData = param;
        Log.d(TAG, "打开子系统SubAppActivity indexPage:" + indexPage + ", param:" + iData.toString());
        getPluginManager().getPlugin(MobileUI.class).openPage(indexPage, iData.toString());
        System.out.println("SubAppActivity-openHomePage:6:");
    }

    /* access modifiers changed from: protected */
    public TemplateSubActivity.LoadingDialogStyle getLoadingDialogStyle() {
        return TemplateSubActivity.LoadingDialogStyle.HORIZONTAL;
    }

    /* access modifiers changed from: protected */
    public boolean setLoadingPage() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isSkipUpdateRes() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void loadingPage() {
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 100) {
            finish();
        }
    }

    public void onBackPressed() {
        FlipperLayout flipperLayout = getFlipperLayout();
        if (flipperLayout == null || !flipperLayout.isCanBack()) {
            UIUtil.alert(this, "确定要退出子系统吗？", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.cancel();
                    SubAppActivity.this.finish();
                }
            }, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.cancel();
                }
            });
        } else {
            flipperLayout.back();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        Log.d(TAG, "SubAppActivity.onStop");
    }

    /* access modifiers changed from: protected */
    public boolean isHintWithUpdateRes() {
        return false;
    }
}