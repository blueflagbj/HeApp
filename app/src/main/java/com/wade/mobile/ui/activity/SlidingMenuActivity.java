package com.wade.mobile.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.frame.activity.TemplateMobileActivity;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.ui.util.UiTool;
import com.wade.mobile.util.Constant;
import java.io.File;

/* renamed from: com.wade.mobile.ui.activity.SlidingMenuActivity */
public class SlidingMenuActivity extends TemplateMobileActivity {
    public static final int ERROR_CODE = -1;
    public static final String KEY_DATA = "data";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_LEFT_MARGIN = "leftMargin";
    public static final String KEY_PAGE_ACTION = "pageAction";
    public static final String KEY_RESULT = "result";
    public static final String KEY_TOP_MARGIN = "topMargin";
    public static final String KEY_WIDTH = "width";
    public static final int SUCCESS_CODE = 1;
    protected boolean isMultWebview = false;

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        String pageAction = getIntent().getStringExtra("pageAction");
        String data = getIntent().getStringExtra("data");
        double width = getIntent().getDoubleExtra("width", 0.5d);
        double height = getIntent().getDoubleExtra("height", 1.0d);
        double leftMargin = getIntent().getDoubleExtra("width", 0.0d);
        double topMargin = getIntent().getDoubleExtra("height", 0.0d);
        if (data == null) {
            data = "";
        }
        getWadeMobileClient().execute("openTemplate", new Object[]{pageAction, data, Constant.FALSE});
        FrameLayout mainFrameLayout = null;
        if (getMainLayout() instanceof FrameLayout) {
            mainFrameLayout = (FrameLayout) getMainLayout();
        }
        if (mainFrameLayout != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mainFrameLayout.getLayoutParams();
            params.width = (int) (((double) UiTool.getScreenWidth(this)) * width);
            params.height = (int) (((double) UiTool.getScreenHeight(this)) * height);
            params.leftMargin = (int) (((double) (UiTool.getScreenWidth(this) - params.width)) * leftMargin);
            params.topMargin = (int) (((double) (UiTool.getScreenHeight(this) - params.height)) * topMargin);
            mainFrameLayout.setLayoutParams(params);
        }
    }

    public void closeSlidingMenu(String resultData) {
        closeSlidingMenu(resultData, 1);
    }

    public void closeSlidingMenu(String resultData, int state) {
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
        closeSlidingMenu((String) null);
        return true;
    }

    /* access modifiers changed from: protected */
    public void initBasePath() {
        TemplateManager.initBasePath(String.valueOf(MobileAppInfo.getSdcardAppPath(this)) + File.separator);
    }
}