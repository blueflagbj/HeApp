package com.wade.mobile.func;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONException;

public class SoftKeyBoard extends Plugin {
    private static final String NUMBER_TYPE = "number";

    public SoftKeyBoard(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void showKeyBoard(JSONArray param) throws JSONException {
        showKeyBoard(param.getString(0));
    }

    private void showKeyBoard(String type) {
        InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (imm.isActive()) {
                imm.showSoftInput(this.wademobile.getCurrentWebView(), 0);
            }
        }
    }

    public void hideKeyBoard(JSONArray param) {
        ((InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.context.getCurrentFocus().getWindowToken(), 2);
    }

    public void toggleKeyBoard(JSONArray param) {
        ((InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInputFromWindow(this.wademobile.getCurrentWebView().getWindowToken(), 0, 2);
    }
}