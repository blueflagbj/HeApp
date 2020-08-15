package com.ai.cmcchina.multiple;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.ailk.common.data.impl.DataMap;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.crm.util.UIUtil;
import com.heclient.heapp.App;
import com.wade.mobile.ui.activity.CustomWindowActivity;

/* renamed from: com.ai.cmcchina.multiple.MultipleCustomWindowActivity */
public class MultipleCustomWindowActivity extends CustomWindowActivity {
    private int count = 2;

    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //加水印
       // ((App) getApplication()).addWatermark(this, (FrameLayout) getFlipperLayout().getParent());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String AllowCloseWindow;
        String data = getIntent().getStringExtra("data");
        if (StringUtil.isNotBlank(data) && (AllowCloseWindow = new DataMap(data).getString("AllowCloseWindow")) != null && "N".equals(AllowCloseWindow)) {
            if (this.count == 2) {
                this.count--;
                UIUtil.toast((Context) this, (Object) "请点击页面中的取消或返回按钮进行返回操作");
                return false;
            } else if (this.count == 1) {
                this.count--;
                UIUtil.toast((Context) this, (Object) "再次点击返回按钮后退出该页面！");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}