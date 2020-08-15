package com.ai.cmcchina.multiple.util;


import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import com.ai.cmcchina.crm.util.UIUtil;
import com.heclient.heapp.R;

import java.util.List;

/* renamed from: com.ai.cmcchina.multiple.util.KeyboardUtil */
public class KeyboardUtil {
    /* access modifiers changed from: private */
    public Context ctx;
    /* access modifiers changed from: private */
    public Keyboard digitKey;
    /* access modifiers changed from: private */

    /* renamed from: ed */
    public EditText f966ed;
    /* access modifiers changed from: private */
    public String inputType;
    /* access modifiers changed from: private */
    public boolean isSymbol = false;
    public boolean isnun = false;
    public boolean isupper = false;
    /* access modifiers changed from: private */
    public KeyboardView keyboardView;
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        public void swipeUp() {
        }

        public void swipeRight() {
        }

        public void swipeLeft() {
        }

        public void swipeDown() {
        }

        public void onText(CharSequence text) {
        }

        public void onRelease(int primaryCode) {
        }

        public void onPress(int primaryCode) {
        }

        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = KeyboardUtil.this.f966ed.getText();
            int start = KeyboardUtil.this.f966ed.getSelectionStart();
            if (primaryCode == -3) {
                KeyboardUtil.this.hideKeyboard();
            } else if (primaryCode == -5) {
                if (editable != null && editable.length() > 0 && start > 0) {
                    editable.delete(start - 1, start);
                }
            } else if (primaryCode == -1) {
                KeyboardUtil.this.changeKey();
                KeyboardUtil.this.keyboardView.setKeyboard(KeyboardUtil.this.qwertyKey);
            } else if (primaryCode == -2) {
                if (KeyboardUtil.this.isnun) {
                    KeyboardUtil.this.isnun = false;
                    boolean unused = KeyboardUtil.this.isSymbol = false;
                    if ("digitKey".equals(KeyboardUtil.this.inputType)) {
                        UIUtil.toast(KeyboardUtil.this.ctx, (Object) "只能使用数字键盘");
                    } else {
                        KeyboardUtil.this.keyboardView.setKeyboard(KeyboardUtil.this.qwertyKey);
                    }
                } else {
                    KeyboardUtil.this.isnun = true;
                    KeyboardUtil.this.keyboardView.setKeyboard(KeyboardUtil.this.digitKey);
                }
            } else if (primaryCode == 57419) {
                if (start > 0) {
                    KeyboardUtil.this.f966ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) {
                if (start < KeyboardUtil.this.f966ed.length()) {
                    KeyboardUtil.this.f966ed.setSelection(start + 1);
                }
            } else if (primaryCode != 444) {
                editable.insert(start, Character.toString((char) primaryCode));
            } else if (KeyboardUtil.this.isSymbol) {
                boolean unused2 = KeyboardUtil.this.isSymbol = false;
            } else {
                boolean unused3 = KeyboardUtil.this.isSymbol = true;
                KeyboardUtil.this.isnun = true;
                KeyboardUtil.this.keyboardView.setKeyboard(KeyboardUtil.this.symbolKey);
            }
        }
    };
    /* access modifiers changed from: private */
    public Keyboard qwertyKey;
    /* access modifiers changed from: private */
    public Keyboard symbolKey;

    public KeyboardUtil(Activity act, Context ctx2, EditText edit) {
        this.f966ed = edit;
        this.qwertyKey = new Keyboard(ctx2, R.xml.qwerty);
        this.digitKey = new Keyboard(ctx2, R.xml.digit);
        this.symbolKey = new Keyboard(ctx2, R.xml.symbol);
        this.keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        this.keyboardView.setKeyboard(this.qwertyKey);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(true);
        this.keyboardView.setOnKeyboardActionListener(this.listener);
    }

    public KeyboardUtil(Activity act, Context ctx2, EditText edit, String inputTyper) {
        this.ctx = ctx2;
        this.f966ed = edit;
        this.qwertyKey = new Keyboard(ctx2, R.xml.qwerty);
        this.digitKey = new Keyboard(ctx2, R.xml.digit);
        this.symbolKey = new Keyboard(ctx2, R.xml.symbol);
        this.keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
        if (inputTyper.equals("qwertyKey")) {
            this.keyboardView.setKeyboard(this.qwertyKey);
        } else if (inputTyper.equals("digitKey")) {
            this.keyboardView.setKeyboard(this.digitKey);
            this.inputType = "digitKey";
            this.isnun = true;
        } else {
            this.keyboardView.setKeyboard(this.symbolKey);
        }
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(true);
        this.keyboardView.setOnKeyboardActionListener(this.listener);
    }

    /* access modifiers changed from: private */
    public void changeKey() {
        List<Keyboard.Key> keylist = this.qwertyKey.getKeys();
        if (this.isupper) {
            this.isupper = false;
            for (Keyboard.Key key : keylist) {
                if (key.label != null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
            return;
        }
        this.isupper = true;
        for (Keyboard.Key key2 : keylist) {
            if (key2.label != null && isword(key2.label.toString())) {
                key2.label = key2.label.toString().toUpperCase();
                key2.codes[0] = key2.codes[0] - 32;
            }
        }
    }

    public void showKeyboard() {
        int visibility = this.keyboardView.getVisibility();
        if (visibility == 8 || visibility == 4) {
            this.keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        if (this.keyboardView.getVisibility() == View.VISIBLE) {
            this.keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isword(String str) {
        if ("abcdefghijklmnopqrstuvwxyz".indexOf(str.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }
}