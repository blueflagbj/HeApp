package com.wade.mobile.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.frame.activity.TemplateMobileActivity;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import java.io.File;

/* renamed from: com.wade.mobile.ui.activity.CustomWindowActivity */
public class CustomWindowActivity extends TemplateMobileActivity {
    public static final int ERROR_CODE = -1;
    public static final String KEY_DATA = "data";
    public static final String KEY_PAGE_ACTION = "pageAction";
    public static final String KEY_RESULT = "result";
    public static final int SUCCESS_CODE = 1;
    protected boolean isMultWebview = false;

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        String pageAction = getIntent().getStringExtra("pageAction");
        String data = getIntent().getStringExtra("data");
        if (data == null) {
            data = "";
        }
        getWadeMobileClient().execute("openTemplate", new Object[]{pageAction, data, Constant.FALSE});
    }

    public void closeWindow(String resultData) {
        closeWindow(resultData, 1);
    }

    public void closeWindow(String resultData, int state) {
        Intent intent = new Intent();
        if (resultData != null) {
            intent.putExtra("result", resultData);
        }
        setResult(state, intent);
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        closeWindow((String) null);
        return true;
    }

    /* access modifiers changed from: protected */
    public void initBasePath() {
        TemplateManager.initBasePath(String.valueOf(MobileAppInfo.getSdcardAppPath(this)) + File.separator);
    }
}