package com.wade.mobile.frame.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.frame.WadeMobileActivity;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.frame.template.TemplateWebView;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.ui.helper.LayoutHelper;
import com.wade.mobile.ui.layout.ConstantParams;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import java.io.File;
import java.net.SocketTimeoutException;

public abstract class TemplateMobileActivity extends WadeMobileActivity {
    protected FlipperLayout flipperLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBasePath();
        try {
            new MobileThread("initActivity") {
                /* access modifiers changed from: protected */
                public void execute() throws Exception {
                    TemplateMobileActivity.this.initActivity();
                }

                /* access modifiers changed from: protected */
                public void error(Exception e) {
                    TemplateMobileActivity.this.error(e);
                }
            }.start();
        } catch (Exception e) {
            error(e);
        }
    }

    /* access modifiers changed from: protected */
    public ViewGroup createContentView() {
        this.mainLayout = LayoutHelper.createFrameLayout(this);
        View mainView = createMainView();
        this.flipperLayout = (FlipperLayout) mainView;
        this.mainLayout.addView(mainView);
        return this.mainLayout;
    }

    /* access modifiers changed from: protected */
    public View createMainView() {
        FlipperLayout flipperLayout2 = new FlipperLayout(this);
        flipperLayout2.setLayoutParams(ConstantParams.getFillParams(FrameLayout.LayoutParams.class, true));
        return flipperLayout2;
    }

    public TemplateWebView getCurrentWebView() {
        return (TemplateWebView) this.flipperLayout.getCurrView();
    }

    public FlipperLayout getFlipperLayout() {
        return this.flipperLayout;
    }

    public void onBackPressed() {
        FlipperLayout flipperLayout2 = getFlipperLayout();
        if (flipperLayout2 == null || !flipperLayout2.isCanBack()) {
            getWadeMobileClient().shutdownByConfirm(Messages.CONFIRM_CLOSE);
        } else {
            flipperLayout2.back();
        }
    }

    /* access modifiers changed from: protected */
    public void initBasePath() {
        TemplateManager.initBasePath(MobileAppInfo.getInstance(this).getAssetsAppPath() + File.separator);
    }

    /* access modifiers changed from: protected */
    public void error(Exception e) {
        if (e instanceof SocketTimeoutException) {
            getWadeMobileClient().alert(Messages.CONN_SERVER_FAILED, Constant.Function.close, new Object[]{false});
            return;
        }
        HintHelper.alert(this, e.getMessage());
        MobileLog.e(this.TAG, e.getMessage(), e);
    }

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        getWadeMobileClient().execute("openPage", new Object[]{ServerConfig.getInstance().getValue("indexPage"), "null", false});
    }
}