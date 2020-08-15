package com.ai.cmcchina.multiple.fragment.login;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.uap.util.des.EncryptInterface;
import com.baidu.location.BDLocation;

import com.ai.cmcchina.crm.handler.GatewayManageHandler;
import com.ai.cmcchina.crm.util.DeviceUtil;
import com.ai.cmcchina.crm.util.Global;
import com.ai.cmcchina.crm.util.PreferenceHelper;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.crm.view.VerifyCodeView;
import com.ai.cmcchina.esop.loc.LocationTool;

import com.ai.cmcchina.multiple.LoginFragmentActivity;
import com.ai.cmcchina.multiple.LoginGesturePassword;
import com.ai.cmcchina.multiple.LoginHelpActivity;

import com.ai.cmcchina.multiple.util.KeyboardUtil;
import com.ai.cmcchina.multiple.util.MemoryCacheUtil;
import com.heclient.heapp.App;

import com.heclient.heapp.R;
import com.wade.mobile.common.contacts.helper.TypeBarView;
import com.wade.mobile.util.Constant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.ai.cmcchina.multiple.fragment.login.SelfSupportChannelLoginFragment */
public class SelfSupportChannelLoginFragment extends Fragment {
    private static final String BOSS_ACCOUNT_ID = "APPACCTID";
    private static final String BOSS_ACCOUNT_LOCK = "LOCK";
    private static final String BOSS_ACCOUNT_NAME = "NAME";
    private static final String BOSS_ACCOUNT_ORG_ID = "ORGID";
    private static final String FIRST_LOGIN = "0";
    private static final String LOGIN_VALIDATE_URL_ACTION = "LoginAction.loginValidate";
    private static final String SECOND_DESC = "秒";
    private static final String SEND_SMS_URL_ACTION = "LoginAction.sendSmsValidate";
    private static final int timerTaskFlag = 100;
    /* access modifiers changed from: private */
    public int duration = 30;
    private TextView helper;
    /* access modifiers changed from: private */
    public EditText inputPassword;
    /* access modifiers changed from: private */
    public AutoCompleteTextView inputUserName;
    /* access modifiers changed from: private */
    public EditText inputVerifycode;
    /* access modifiers changed from: private */
    public boolean isEditTextChange = false;
    private KeyboardUtil keyboard;
    private Button loginBtn;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public View mView;
    /* access modifiers changed from: private */
    public CheckBox rememberPwd;
    /* access modifiers changed from: private */
    public TextView sendSmsBtn;
    /* access modifiers changed from: private */
    public SendSmsTask sendSmsTask = null;
    /* access modifiers changed from: private */
    public String sessionId = "";
    /* access modifiers changed from: private */
    public String smsCode;
    /* access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    public Handler smsHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (SelfSupportChannelLoginFragment.this.duration > 0) {
                        SelfSupportChannelLoginFragment.this.sendSmsBtn.setTextColor(-1);
                        SelfSupportChannelLoginFragment.this.sendSmsBtn.setClickable(false);
                        SelfSupportChannelLoginFragment.this.sendSmsBtn.setText(SelfSupportChannelLoginFragment.this.duration + SelfSupportChannelLoginFragment.SECOND_DESC);
                        SelfSupportChannelLoginFragment.access$3110(SelfSupportChannelLoginFragment.this);
                        return;
                    }
                    SelfSupportChannelLoginFragment.this.sendSmsTask.cancel();
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setClickable(true);
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setText("再次发送");
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String telephone;
    private Timer timer = null;
    /* access modifiers changed from: private */
    public boolean timerIsAlive = false;
    private TextView tvGestureLogin;
    /* access modifiers changed from: private */
    public VerifyCodeView verifycodeV;
    /* access modifiers changed from: private */
    public TextWatcher watcher = new TextWatcher() {
        String text = "";

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.text = s.toString();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!s.toString().equals(this.text)) {
                boolean unused = SelfSupportChannelLoginFragment.this.isEditTextChange = true;
            }
        }

        public void afterTextChanged(Editable arg0) {
        }
    };

    static /* synthetic */ int access$3110(SelfSupportChannelLoginFragment x0) {
        int i = x0.duration;
        x0.duration = i - 1;
        return i;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_self_support_channel_login, container, false);
        this.mContext = getActivity();
        initUI();
        return this.mView;
    }

    private void initUI() {
        int textSizeSmall = UIUtil.getFinalSize(this.mContext, 18.0f);
        ((TextView) this.mView.findViewById(R.id.label_username)).setTextSize(0, (float) textSizeSmall);
        ((TextView) this.mView.findViewById(R.id.label_password)).setTextSize(0, (float) textSizeSmall);
        ((TextView) this.mView.findViewById(R.id.label_verifycode)).setTextSize(0, (float) textSizeSmall);
        int height = UIUtil.getFinalSize(this.mContext, 50.0f);
        this.inputUserName = (AutoCompleteTextView) this.mView.findViewById(R.id.input_username);
        this.inputUserName.setTextSize(0, (float) textSizeSmall);
        this.inputUserName.getLayoutParams().height = height;
        this.inputUserName.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                arg0.performClick();
                SelfSupportChannelLoginFragment.this.closeCustomSoftInput();
                return false;
            }
        });
        this.inputPassword = (EditText) this.mView.findViewById(R.id.input_password);
        this.inputPassword.setTextSize(0, (float) textSizeSmall);
        this.inputPassword.getLayoutParams().height = height;
        this.inputPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent arg1) {
                v.performClick();
                SelfSupportChannelLoginFragment.this.showPasswordSoftInput();
                return false;
            }
        });
        this.inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View paramView, boolean paramBoolean) {
                if (paramBoolean) {
                    SelfSupportChannelLoginFragment.this.showPasswordSoftInput();
                }
            }
        });
        this.rememberPwd = (CheckBox) this.mView.findViewById(R.id.remember_pwd);
        this.rememberPwd.setTextSize(0, (float) textSizeSmall);
        ViewGroup.LayoutParams lpRP = this.rememberPwd.getLayoutParams();
        lpRP.width = UIUtil.getFinalSize(this.mContext, 140.0f);
        lpRP.height = height;
        this.helper = (TextView) this.mView.findViewById(R.id.helper);
        this.helper.setTextSize(0, (float) textSizeSmall);
        ViewGroup.LayoutParams hl = this.helper.getLayoutParams();
        hl.width = UIUtil.getFinalSize(this.mContext, 140.0f);
        hl.height = UIUtil.getFinalSize(this.mContext, 35.0f);
        this.helper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelfSupportChannelLoginFragment.this.startActivity(new Intent(SelfSupportChannelLoginFragment.this.mContext, LoginHelpActivity.class));
            }
        });
        this.inputVerifycode = (EditText) this.mView.findViewById(R.id.input_verifycode);
        this.inputVerifycode.setTextSize(0, (float) textSizeSmall);
        this.inputVerifycode.getLayoutParams().height = height;
        this.inputVerifycode.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                arg0.performClick();
                SelfSupportChannelLoginFragment.this.closeCustomSoftInput();
                return false;
            }
        });
        this.inputVerifycode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (6 != actionId) {
                    return false;
                }
                SelfSupportChannelLoginFragment.this.login(SelfSupportChannelLoginFragment.this.mView.findViewById(R.id.button_login));
                return false;
            }
        });
        this.verifycodeV = (VerifyCodeView) this.mView.findViewById(R.id.view_verifycode);
        ViewGroup.LayoutParams lp4 = this.verifycodeV.getLayoutParams();
        lp4.width = UIUtil.getFinalSize(this.mContext, 140.0f);
        lp4.height = height;
        this.verifycodeV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelfSupportChannelLoginFragment.this.verifycodeV.refresh();
            }
        });
        this.timer = new Timer(true);
        this.sendSmsBtn = (TextView) this.mView.findViewById(R.id.textView_send_sms);
        this.sendSmsBtn.setText("发送短信");
        this.sendSmsBtn.setTextColor(getResources().getColor(R.color.white));
        this.sendSmsBtn.setTextSize(0, (float) UIUtil.getFinalSize(this.mContext, 20.0f));
        this.sendSmsBtn.getLayoutParams().height = height;
        this.sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelfSupportChannelLoginFragment.this.closeCustomSoftInput();
                SelfSupportChannelLoginFragment.this.sendSMS();
            }
        });
        this.loginBtn = (Button) this.mView.findViewById(R.id.button_login);
        this.loginBtn.setTextSize(0, (float) UIUtil.getFinalSize(this.mContext, 28.0f));
        this.loginBtn.getLayoutParams().height = height;
        this.loginBtn.setClickable(false);
        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SelfSupportChannelLoginFragment.this.login(SelfSupportChannelLoginFragment.this.mView.findViewById(R.id.button_login));
            }
        });
        this.tvGestureLogin = (TextView) this.mView.findViewById(R.id.gesture_password_login);
        this.tvGestureLogin.setTextSize(0, (float) UIUtil.getFinalSize(this.mContext, 20.0f));
        this.tvGestureLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String userName = SelfSupportChannelLoginFragment.this.inputUserName.getText().toString();
                if (!PreferenceHelper.getWithKeyValueEncry(Constant.GESTURE_PASSWORD_TABLE + userName, "").equals("")) {
                    Intent intent = new Intent(SelfSupportChannelLoginFragment.this.mContext, LoginGesturePassword.class);
                    intent.putExtra("username", userName);
                    SelfSupportChannelLoginFragment.this.startActivity(intent);
                    SelfSupportChannelLoginFragment.this.getActivity().finish();
                    return;
                }
                UIUtil.toast(SelfSupportChannelLoginFragment.this.mContext, (Object) "该账号(" + userName + ")没有手势密码，请登录后设置");
            }
        });
        this.inputUserName.setAdapter(new ArrayAdapter<>(this.mContext, R.layout.autocomplete, PreferenceHelper.get("usernames", com.wade.mobile.util.Constant.PARAMS_SQE).split(com.wade.mobile.util.Constant.PARAMS_SQE)));
        String userName = getActivity().getIntent().getStringExtra("username");
        String loginType = PreferenceHelper.get(Constant.APP_LOGIN_TYPE + userName, "");
        if (userName == null || !Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL.equals(loginType)) {
            userName = PreferenceHelper.get("username", "");
        }
        this.inputUserName.setText(userName);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SelfSupportChannelLoginFragment.this.inputUserName.dismissDropDown();
            }
        }, 150);
        boolean isRemembered = PreferenceHelper.getWithKeyEncry(Global.PrefKey.REMEMBER_PASSWORD + this.inputUserName.getText(), false);
        this.rememberPwd.setChecked(isRemembered);
        this.inputUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                boolean isRemembered = PreferenceHelper.getWithKeyEncry(Global.PrefKey.REMEMBER_PASSWORD + SelfSupportChannelLoginFragment.this.inputUserName.getText(), false);
                SelfSupportChannelLoginFragment.this.rememberPwd.setChecked(isRemembered);
                if (isRemembered) {
                    SelfSupportChannelLoginFragment.this.inputPassword.setText(PreferenceHelper.getWithKeyValueEncry(Global.PrefKey.PASSWORD + SelfSupportChannelLoginFragment.this.inputUserName.getText(), ""));
                } else {
                    SelfSupportChannelLoginFragment.this.inputPassword.setText("");
                }
                SelfSupportChannelLoginFragment.this.inputPassword.requestFocus();
                SelfSupportChannelLoginFragment.this.inputPassword.setSelection(SelfSupportChannelLoginFragment.this.inputPassword.getText().length());
            }
        });
        if (isRemembered) {
            this.inputPassword.setText(PreferenceHelper.getWithKeyValueEncry(Global.PrefKey.PASSWORD + this.inputUserName.getText(), ""));
        }
    }

    /* access modifiers changed from: private */
    public void closeCustomSoftInput() {
        if (this.keyboard != null) {
            this.keyboard.hideKeyboard();
        }
    }

    /* access modifiers changed from: private */
    public void sendSMS() {
        String model = DeviceUtil.getDeviceModel();
        final String username = this.inputUserName.getText().toString();
        final String password = this.inputPassword.getText().toString();
        if ("".equals(username)) {
            UIUtil.toast(this.mContext, (Object) "用户名不能为空");
            this.inputUserName.setError("请输入用户名");
            this.inputUserName.requestFocus();
        } else if ("".equals(password)) {
            UIUtil.toast(this.mContext, (Object) "密码不能为空");
            this.inputPassword.setError("请输入密码");
            this.inputPassword.requestFocus();
        } else {
            JSONObject data = new JSONObject();
            data.put("mainAcct", (Object) username);
            data.put("terminalModel", (Object) model);
            data.put("loginPwd", (Object) EncryptInterface.desEncryptData(password));
            data.put("imei", (Object) DeviceUtil.getImei(this.mContext));
            data.put("mac", (Object) DeviceUtil.getMacAddress(this.mContext));
            data.put("version", (Object) DeviceUtil.getAPKVersion(this.mContext));
            data.put("clientTime", (Object) getCurrentDate());
            this.sendSmsBtn.setClickable(false);
            Map<String, String> params = new HashMap<>();
            params.put(Constant.Server.ACTION, SEND_SMS_URL_ACTION);
            params.put("data", data.toString());
            new GatewayManageHandler(getActivity(), getReqUrl(), params) {
                public void onSuccess(String result) {
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setClickable(true);
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setText("再次发送");
                    JSONObject json = (JSONObject) JSONObject.parse(result);
                    if ("0000".equals(json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
                        if (!SelfSupportChannelLoginFragment.this.timerIsAlive) {
                            SelfSupportChannelLoginFragment.this.startTimerTask();
                        }
                        boolean unused = SelfSupportChannelLoginFragment.this.isEditTextChange = false;
                        SelfSupportChannelLoginFragment.this.inputUserName.addTextChangedListener(SelfSupportChannelLoginFragment.this.watcher);
                        SelfSupportChannelLoginFragment.this.inputPassword.addTextChangedListener(SelfSupportChannelLoginFragment.this.watcher);
                        UIUtil.toast(SelfSupportChannelLoginFragment.this.mContext, (Object) "短信验证码已发送，请注意查收");
                        PreferenceHelper.commit("username", username);
                        if (!PreferenceHelper.get("usernames", "").contains(username)) {
                            PreferenceHelper.commit("usernames", PreferenceHelper.get("usernames", "") + username + com.wade.mobile.util.Constant.PARAMS_SQE);
                        }
                        String unused2 = SelfSupportChannelLoginFragment.this.telephone = (String) json.getJSONObject(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY).get("MOBILE");
                        PreferenceHelper.commitWithKeyEncry(Global.PrefKey.REMEMBER_PASSWORD + username, SelfSupportChannelLoginFragment.this.rememberPwd.isChecked());
                        PreferenceHelper.commitWithKeyValueEncry(com.ai.cmcchina.crm.constant.Constant.GESTURE_PASSWORD + username, password);
                        PreferenceHelper.commit(com.ai.cmcchina.crm.constant.Constant.APP_LOGIN_TYPE + username, com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL);
                        if (SelfSupportChannelLoginFragment.this.rememberPwd.isChecked()) {
                            PreferenceHelper.commitWithKeyValueEncry(Global.PrefKey.PASSWORD + username, password);
                        } else {
                            PreferenceHelper.commitWithKeyValueEncry(Global.PrefKey.PASSWORD + username, "");
                        }
                        PreferenceHelper.commit("password", password);
                        String unused3 = SelfSupportChannelLoginFragment.this.sessionId = json.getString(com.ai.cmcchina.crm.constant.Constant.SESSION_ID);
                        SelfSupportChannelLoginFragment.this.saveSessionIdToApplication(SelfSupportChannelLoginFragment.this.sessionId);
                        String unused4 = SelfSupportChannelLoginFragment.this.smsCode = json.getString("sms_code");
                        SelfSupportChannelLoginFragment.this.inputVerifycode.setText(SelfSupportChannelLoginFragment.this.smsCode);
                        return;
                    }
                    String desc = json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY);
                    try {
                        desc = ((JSONObject) JSONObject.parse(desc)).getString(com.ai.cmcchina.crm.constant.Constant.ERRDESC);
                    } catch (Exception e) {
                       e.printStackTrace();
                      //  G3Logger.debug(e);
                    }
                    UIUtil.alert(SelfSupportChannelLoginFragment.this.mContext, desc);
                }

                public void onError(String result) {
                    super.onError(result);
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setClickable(true);
                    SelfSupportChannelLoginFragment.this.sendSmsBtn.setText("再次发送");
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void startTimerTask() {
        this.sendSmsTask = new SendSmsTask();
        this.timer.schedule(this.sendSmsTask, 0, 1000);
        this.duration = 30;
    }

    private String getReqUrl() {
        return "/mobiledata";
    }

    private String getSessionId() {
        return ((App) getActivity().getApplication()).getSessionId();
    }

    /* access modifiers changed from: private */
    public void saveSessionIdToApplication(String sessionId2) {
        App app = (App) getActivity().getApplication();
        app.setSessionId(sessionId2);
      //  app.setAccountChannelType(com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL);
    }

    /* access modifiers changed from: private */
    public void login(View view) {
        final String username = this.inputUserName.getText().toString();
        final String password = this.inputPassword.getText().toString();
        String verificationCode = this.inputVerifycode.getText().toString();
        if (this.isEditTextChange) {
            UIUtil.toast(this.mContext, (Object) "输入的信息改变，请重新发送验证码");
        } else if ("".equals(username)) {
            UIUtil.toast(this.mContext, (Object) "用户名不能为空");
            this.inputUserName.setError("请输入用户名");
            this.inputUserName.requestFocus();
        } else if ("".equals(password)) {
            UIUtil.toast(this.mContext, (Object) "密码不能为空");
            this.inputPassword.setError("请输入密码");
            this.inputPassword.requestFocus();
        } else if (TextUtils.isEmpty(verificationCode)) {
            UIUtil.toast(this.mContext, (Object) "请输入验证码");
            this.inputVerifycode.setError("请输入验证码");
        } else if ("".equals(this.sessionId)) {
            UIUtil.toast(this.mContext, (Object) "验证码发送失败或未发送成功，请重新发送");
        } else {
            view.setClickable(false);
            JSONObject data = createBaseData();
            data.put("smsCode", (Object) verificationCode);
            data.put("imei", (Object) DeviceUtil.getImei(this.mContext));
            Map<String, String> params = new HashMap<>();
            params.put(Constant.Server.ACTION, LOGIN_VALIDATE_URL_ACTION);
            params.put("data", data.toString());
            final View view2 = view;
            new GatewayManageHandler(getActivity(), getReqUrl(), params) {
                public void onError(String result) {
                    super.onError(result);
                    view2.setClickable(true);
                }

                public void onSuccess(String result) {
                    //G3Logger.debug(result);
                    view2.setClickable(true);
                    if (result == null || "".equals(result)) {
                        UIUtil.alert(SelfSupportChannelLoginFragment.this.mContext, "亲，您登错系统了");
                        return;
                    }
                    SelfSupportChannelLoginFragment.this.rememberUserNameAndPassword(username, password);
                    JSONObject rspJsonObj = (JSONObject) JSONObject.parse(result);
                    if (SelfSupportChannelLoginFragment.this.validateSuccess(rspJsonObj)) {
                        JSONObject msgObject = rspJsonObj.getJSONObject(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY);
                        if ("0".equals(msgObject.getJSONObject("bean").getString("firstLogin"))) {
                            UIUtil.alert(SelfSupportChannelLoginFragment.this.mContext, "您是首次登录，请到CRM系统中修改密码。");
                            return;
                        }
                        JSONArray accountArray = msgObject.getJSONArray("beans");
                        if (accountArray.isEmpty()) {
                            SelfSupportChannelLoginFragment.this.loginWithSingleAccout(result);
                        } else if (accountArray.size() == 1) {
                            JSONObject jsonObj = accountArray.getJSONObject(0);
                            String accountId = jsonObj.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_ID);
                            SelfSupportChannelLoginFragment.this.checkLockState(jsonObj, jsonObj.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_NAME), accountId, jsonObj.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_ORG_ID));
                        } else {
                            SelfSupportChannelLoginFragment.this.showNameItemsAlert(getNameItems(accountArray), transNameAndIdMapping(accountArray), transNameAndOrgIdMapping(accountArray), accountArray);
                        }
                    }
                }

                private Map<String, String> transNameAndIdMapping(JSONArray accountArray) {
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < accountArray.size(); i++) {
                        JSONObject object = accountArray.getJSONObject(i);
                        map.put(object.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_NAME), object.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_ID));
                    }
                    return map;
                }

                private Map<String, String> transNameAndOrgIdMapping(JSONArray accountArray) {
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < accountArray.size(); i++) {
                        JSONObject object = accountArray.getJSONObject(i);
                        map.put(object.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_NAME), object.getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_ORG_ID));
                    }
                    return map;
                }

                private String[] getNameItems(JSONArray accountArray) {
                    String[] items = new String[accountArray.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = accountArray.getJSONObject(i).getString(SelfSupportChannelLoginFragment.BOSS_ACCOUNT_NAME);
                    }
                    return items;
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public boolean validateSuccess(JSONObject rspJsonObj) {
        if ("0000".equals(rspJsonObj.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
            return true;
        }
        UIUtil.toast(this.mContext, (Object) rspJsonObj.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY));
        this.verifycodeV.refresh();
        return false;
    }

    /* access modifiers changed from: private */
    public void showNameItemsAlert(String[] items, Map<String, String> mapping, Map<String, String> orgIdMapping, JSONArray accountArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle("请选择工号");
        final String[] strArr = items;
        final Map<String, String> map = mapping;
        final Map<String, String> map2 = orgIdMapping;
        final JSONArray jSONArray = accountArray;
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                String name = strArr[item];
                JSONObject jsonObject = jSONArray.getJSONObject(item);
                SelfSupportChannelLoginFragment.this.checkLockState(jsonObject, name, (String) map.get(name), (String) map2.get(name));
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public void checkLockState(JSONObject jsonObject, String name, String id, String orgId) {
        String lock = jsonObject.getString(BOSS_ACCOUNT_LOCK);
        if ("1".equalsIgnoreCase(lock)) {
            loginSystem(name, id, orgId);
        } else if ("0".equalsIgnoreCase(lock)) {
            UIUtil.alert((Context) getActivity(), "该账号【" + name + "】已加锁，不能进行登录和业务办理！");
        } else if ("2".equalsIgnoreCase(lock)) {
            UIUtil.alert((Context) getActivity(), "该账号【" + name + "】已失效(停用)，不能进行登录和业务办理！");
        } else {
            UIUtil.alert((Context) getActivity(), "该账号【" + name + "】LOCK状态异常（" + lock + "），不能进行登录和业务办理！");
        }
    }

    /* access modifiers changed from: private */
    public void rememberUserNameAndPassword(String username, String password) {
        PreferenceHelper.commit("username", username);
        if (!PreferenceHelper.get("usernames", "").contains(username)) {
            PreferenceHelper.commit("usernames", PreferenceHelper.get("usernames", "") + username + com.wade.mobile.util.Constant.PARAMS_SQE);
        }
        PreferenceHelper.commit("password", password);
    }

    /* access modifiers changed from: private */
    public void loginWithSingleAccout(String datas) {
        this.sendSmsTask.cancel();
     /*   Intent intent = new Intent(this.mContext, MainActivity.class);
        intent.putExtra("account", "");
        intent.putExtra("data", "");
        intent.putExtra("opName", "");
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
    }

    private void loginSystem(String account, String accountId, String ORGID) {
        BDLocation currentLocation = ((LoginFragmentActivity) getActivity()).getCurrentLocation();
        if (currentLocation == null) {
            UIUtil.alert((Context) getActivity(), "获取位置信息失败，请检查位置服务是否打开后重试！");
            return;
        }
        JSONObject data = createBaseData();
        data.put("LOGINACCT", (Object) account);
        data.put("imei", (Object) DeviceUtil.getImei(this.mContext) + TypeBarView.TYPE_NOTYPE + DeviceUtil.getMacAddress(this.mContext));
        data.put(BOSS_ACCOUNT_ID, (Object) accountId);
        data.put("loginType", (Object) "Andorid");
        data.put("terminalModel", (Object) DeviceUtil.getDeviceModel());
        data.put("latitude", (Object) currentLocation.getLatitude() + "");
        data.put("longitude", (Object) currentLocation.getLongitude() + "");
        Map<String, String> params = new HashMap<>();
        params.put(Constant.Server.ACTION, "LoginAction.loginSystem");
        params.put("data", data.toString());
        params.put(BOSS_ACCOUNT_ORG_ID, ORGID);
        params.put(com.ai.cmcchina.crm.constant.Constant.SESSION_ID, this.sessionId);
        final String str = account;
        final String str2 = ORGID;
        new GatewayManageHandler(getActivity(), (String) null, params) {
            public void onSuccess(String result) {
                //G3Logger.debug("====login success:" + result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!"0000".equals(jsonObject.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
                    UIUtil.alert((Context) SelfSupportChannelLoginFragment.this.getActivity(), "登录失败，失败原因: \n" + jsonObject.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY));
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONObject("apps_config").getJSONArray("APPS");
                App app = (App) SelfSupportChannelLoginFragment.this.getActivity().getApplication();
/*                app.setAppsConfig(jsonArray.toJSONString());
                app.setStaffRegionId(str.substring(0, 1).toUpperCase(Locale.ENGLISH));
                SelfSupportChannelLoginFragment.this.saveOperatorInfo(jsonObject, str, str2);
                app.setCurrentMenus(jsonObject.getJSONArray("user_current_menus"));*/
                SelfSupportChannelLoginFragment.this.setAppAccount(str);
               // Intent intent = new Intent(SelfSupportChannelLoginFragment.this.mContext, MainActivity.class);
                SelfSupportChannelLoginFragment.this.sendSmsTask.cancel();
               // SelfSupportChannelLoginFragment.this.startActivity(intent);
                SelfSupportChannelLoginFragment.this.closeLocationUpdate();
                SelfSupportChannelLoginFragment.this.getActivity().finish();
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void saveOperatorInfo(JSONObject jsonObject, String account, String ORGID) {
        String opName = jsonObject.getString("opName");
        String orgName = jsonObject.getString("tingName");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        prefs.edit().putString("account", account).commit();
        prefs.edit().putString("tingId", ORGID).commit();
        prefs.edit().putString("tingName", orgName).commit();
        PreferenceHelper.commit("opName", opName);
        MemoryCacheUtil.setMemoryCache("IS_UNIFY_CLIENT", "Y");
        MemoryCacheUtil.setMemoryCache("account", account);
        MemoryCacheUtil.setMemoryCache("phone", this.telephone);
        MemoryCacheUtil.setMemoryCache("opName", opName);
    }

    /* access modifiers changed from: private */
    public void setAppAccount(String account) {
       // ((App) getActivity().getApplication()).setAccount(account);
    }

    private JSONObject createBaseData() {
        JSONObject data = new JSONObject();
        data.put(com.ai.cmcchina.crm.constant.Constant.SESSION_ID, (Object) getSessionId());
        return data;
    }

    /* renamed from: com.ai.cmcchina.multiple.fragment.login.SelfSupportChannelLoginFragment$SendSmsTask */
    private class SendSmsTask extends TimerTask {
        private SendSmsTask() {
        }

        public void run() {
            SelfSupportChannelLoginFragment.this.smsHandler.sendEmptyMessage(100);
        }
    }

    private void closeSoftInput() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 2);
        } catch (Exception e) {
            //G3Logger.debug(e);
        }
    }

    /* access modifiers changed from: private */
    public void showPasswordSoftInput() {
        closeSoftInput();
        this.inputPassword.setText("");
        int inputback = this.inputPassword.getInputType();
        this.inputPassword.setInputType(0);
        if (this.keyboard == null) {
            this.keyboard = new KeyboardUtil(getActivity(), this.mContext, this.inputPassword);
        }
        this.keyboard.showKeyboard();
        this.inputPassword.setInputType(inputback);
    }

    private String getCurrentDate() {
        return new SimpleDateFormat(Global.DATE_FORMAT_1).format(new Date());
    }

    /* access modifiers changed from: private */
    public void closeLocationUpdate() {
        try {
            LocationTool.getInstance(getActivity()).stopLocation();
        } catch (Exception e) {
           // G3Logger.debug(e);
        }
    }
}