package com.wade.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.frame.activity.TemplateMobileActivity;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.ui.util.UiTool;
import com.wade.mobile.util.Constant;
import java.io.File;

/* renamed from: com.wade.mobile.ui.activity.CustomDialogActivity */
public class CustomDialogActivity extends TemplateMobileActivity {
    public static final int ERROR_CODE = -1;
    public static final String KEY_DATA = "data";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_PAGE_ACTION = "pageAction";
    public static final String KEY_RESULT = "result";
    public static final String KEY_WIDTH = "width";
    public static final int SUCCESS_CODE = 1;
    protected boolean isMultWebview = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
    }

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        String pageAction = getIntent().getStringExtra("pageAction");
        String data = getIntent().getStringExtra("data");
        double width = getIntent().getDoubleExtra("width", 0.5d);
        double height = getIntent().getDoubleExtra("height", 0.5d);
        if (data == null) {
            data = "";
        }
        getWadeMobileClient().execute("openTemplate", new Object[]{pageAction, data, Constant.FALSE});
        ViewGroup.LayoutParams params = getMainLayout().getLayoutParams();
        params.width = (int) (((double) UiTool.getScreenWidth(this)) * width);
        params.height = (int) (((double) UiTool.getScreenHeight(this)) * height);
        getMainLayout().setLayoutParams(params);
    }

    public void closeDialog(String resultData) {
        closeDialog(resultData, 1);
    }

    public void closeDialog(String resultData, int state) {
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
        closeDialog((String) null);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (4 == event.getAction()) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void initBasePath() {
        TemplateManager.initBasePath(String.valueOf(MobileAppInfo.getSdcardAppPath(this)) + File.separator);
    }
}