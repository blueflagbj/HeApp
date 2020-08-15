package com.ai.cmcchina.multiple;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.uap.util.des.EncryptInterface;
import com.baidu.location.BDLocation;
import com.ai.cmcchina.crm.handler.GatewayManageHandler;
import com.ai.cmcchina.crm.util.DeviceUtil;
import com.ai.cmcchina.crm.util.Global;
import com.ai.cmcchina.crm.util.LocationUtil;
import com.ai.cmcchina.crm.util.PreferenceHelper;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.esop.loc.LocationTool;

import com.ai.cmcchina.multiple.util.GestureLockViewGroup;
import com.ai.cmcchina.multiple.util.MemoryCacheUtil;
import com.heclient.heapp.App;

import com.heclient.heapp.MainActivity;
import com.heclient.heapp.R;
import com.wade.mobile.common.contacts.helper.TypeBarView;
import com.wade.mobile.util.Constant;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* renamed from: com.ai.cmcchina.multiple.LoginGesturePassword */
public class LoginGesturePassword extends Activity implements LocationTool.LocationHandler {
    private static final String BOSS_ACCOUNT_ID = "APPACCTID";
    private static final String BOSS_ACCOUNT_LOCK = "LOCK";
    private static final String BOSS_ACCOUNT_NAME = "NAME";
    private static final String BOSS_ACCOUNT_ORG_ID = "ORGID";
    /* access modifiers changed from: private */
    public String accountName = "";
    private BDLocation currentLocation;
    /* access modifiers changed from: private */
    public Location currentLocation2;
    private long exitTime = 0;
    /* access modifiers changed from: private */
    public String loginGesturePassword;
    /* access modifiers changed from: private */
    public String loginType;
    /* access modifiers changed from: private */
    public GestureLockViewGroup mGestureLockViewGroup;
    /* access modifiers changed from: private */
    public String sessionId = "";
    /* access modifiers changed from: private */
    public String telephone;
    private TextView tvInputFrameLogin;
    /* access modifiers changed from: private */
    public String userName;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_gesture_password);
        initView();
        initData();
        setListen();
        initLocation();
    }

    private void initLocation() {
        LocationUtil.getInstance(this).setOnLocationListener(new LocationUtil.CustomLocationListener() {
            public void onSuccess(Location location) {
                Location unused = LoginGesturePassword.this.currentLocation2 = location;
                ////G3Logger.debug("LocationUtil获取位置信息成功:" + location.getLongitude() + "--" + location.getLatitude());
                ////G3Logger.debug("获取位置信息成功！");
            }

            public void onFailed(String msg) {
                UIUtil.toast((Context) LoginGesturePassword.this, (Object) msg);
            }
        });
        LocationTool.getInstance(getApplicationContext()).startLocation();
        LocationTool.getInstance(getApplicationContext()).setLocationHandler(this);
    }

    private void setListen() {
        this.tvInputFrameLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                Intent intent = new Intent(LoginGesturePassword.this, LoginFragmentActivity.class);
                intent.putExtra("username" + PreferenceHelper.get(Constant.APP_LOGIN_TYPE + LoginGesturePassword.this.userName, ""), LoginGesturePassword.this.userName);
                intent.putExtra("username", LoginGesturePassword.this.userName);
                LoginGesturePassword.this.startActivity(intent);
                LoginGesturePassword.this.finish();
            }
        });
    }

    private void initView() {
        this.mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.gesture_password_view_group);
        this.tvInputFrameLogin = (TextView) findViewById(R.id.input_frame_login);
        this.tvInputFrameLogin.setTextSize(0, (float) UIUtil.getFinalSize(this, 20.0f));
    }

    private void initData() {
        this.userName = getIntent().getStringExtra("username");
        this.loginGesturePassword = PreferenceHelper.getWithKeyValueEncry(Constant.GESTURE_PASSWORD_TABLE + this.userName, "");
        this.loginType = PreferenceHelper.get(Constant.APP_LOGIN_TYPE + this.userName, "");
        this.mGestureLockViewGroup.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            public void onUnmatchedExceedBoundary() {
            }

            public void onGestureEvent(boolean matched) {
            }

            public void gesturePasswordResult(String gesturePassword) {
                if (!gesturePassword.equals(LoginGesturePassword.this.loginGesturePassword)) {
                    UIUtil.toast((Context) LoginGesturePassword.this, (Object) "手势密码错误");
                    return;
                }
                LoginGesturePassword.this.mGestureLockViewGroup.reset();
                if (LoginGesturePassword.this.loginType.equals(Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL)) {
                    LoginGesturePassword.this.selfSupportChannelLogin(LoginGesturePassword.this.userName);
                } else {
                    LoginGesturePassword.this.societyChannelLogin(LoginGesturePassword.this.userName);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void selfSupportChannelLogin(String username) {
        String password = PreferenceHelper.getWithKeyValueEncry(Constant.GESTURE_PASSWORD + username, "");
        JSONObject data = new JSONObject();
        data.put("mainAcct", (Object) username);
        data.put("terminalModel", (Object) DeviceUtil.getDeviceModel());
        data.put("loginPwd", (Object) EncryptInterface.desEncryptData(password));
        data.put("imei", (Object) DeviceUtil.getImei(this));
        data.put("mac", (Object) DeviceUtil.getMacAddress(this));
        data.put("version", (Object) DeviceUtil.getAPKVersion(this));
        Map<String, String> params = new HashMap<>();
        params.put(Constant.Server.ACTION, "LoginAction.selfSupportLoginWithOutSms");
        params.put("data", data.toString());
        new GatewayManageHandler(this, "/mobiledata", params) {
            public void onSuccess(String result) {
                ////G3Logger.debug(result);
                JSONObject json = (JSONObject) JSONObject.parse(result);
                if ("0000".equals(json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
                    JSONObject beanObj = json.getJSONObject(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY).getJSONObject("bean");
                    JSONArray accountArray = json.getJSONObject(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY).getJSONArray("beans");
                    String unused = LoginGesturePassword.this.telephone = (String) beanObj.get("MOBILE");
                    String unused2 = LoginGesturePassword.this.sessionId = json.getString(com.ai.cmcchina.crm.constant.Constant.SESSION_ID);
                    LoginGesturePassword.this.saveSessionIdToApplication();
                    if (accountArray.isEmpty()) {
                        LoginGesturePassword.this.loginWithSingleAccout(result);
                    } else if (accountArray.size() == 1) {
                        JSONObject jsonObj = accountArray.getJSONObject(0);
                        String accountId = jsonObj.getString(LoginGesturePassword.BOSS_ACCOUNT_ID);
                        String unused3 = LoginGesturePassword.this.accountName = jsonObj.getString(LoginGesturePassword.BOSS_ACCOUNT_NAME);
                        LoginGesturePassword.this.checkLockState(jsonObj, LoginGesturePassword.this.accountName, accountId, jsonObj.getString(LoginGesturePassword.BOSS_ACCOUNT_ORG_ID));
                    } else {
                        LoginGesturePassword.this.showNameItemsAlert(getNameItems(accountArray), transNameAndIdMapping(accountArray), transNameAndOrgIdMapping(accountArray), accountArray);
                    }
                } else {
                    String desc = json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY);
                    try {
                        desc = ((JSONObject) JSONObject.parse(desc)).getJSONObject("bean").getString(com.ai.cmcchina.crm.constant.Constant.ERRDESC);
                    } catch (Exception e) {
                        //G3Logger.debug(e);
                    }
                    UIUtil.toast((Context) LoginGesturePassword.this, (Object) desc);
                }
            }

            private Map<String, String> transNameAndIdMapping(JSONArray accountArray) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject object = accountArray.getJSONObject(i);
                    map.put(object.getString(LoginGesturePassword.BOSS_ACCOUNT_NAME), object.getString(LoginGesturePassword.BOSS_ACCOUNT_ID));
                }
                return map;
            }

            private Map<String, String> transNameAndOrgIdMapping(JSONArray accountArray) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject object = accountArray.getJSONObject(i);
                    map.put(object.getString(LoginGesturePassword.BOSS_ACCOUNT_NAME), object.getString(LoginGesturePassword.BOSS_ACCOUNT_ORG_ID));
                }
                return map;
            }

            private String[] getNameItems(JSONArray accountArray) {
                String[] items = new String[accountArray.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = accountArray.getJSONObject(i).getString(LoginGesturePassword.BOSS_ACCOUNT_NAME);
                }
                return items;
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void checkLockState(JSONObject jsonObject, String name, String id, String orgId) {
        String lock = jsonObject.getString(BOSS_ACCOUNT_LOCK);
        if ("1".equalsIgnoreCase(lock)) {
            loginSystem(name, id, orgId);
        } else if (Global.SERVICE_PASSWORD.equalsIgnoreCase(lock)) {
            UIUtil.alert((Context) this, "该账号【" + name + "】已加锁，不能进行登录和业务办理！");
        } else if ("2".equalsIgnoreCase(lock)) {
            UIUtil.alert((Context) this, "该账号【" + name + "】已失效(停用)，不能进行登录和业务办理！");
        } else {
            UIUtil.alert((Context) this, "该账号【" + name + "】LOCK状态异常（" + lock + "），不能进行登录和业务办理！");
        }
    }

    /* access modifiers changed from: private */
    public void saveSessionIdToApplication() {
        App app = (App) getApplication();
        app.setSessionId(this.sessionId);
        app.setAccountChannelType(PreferenceHelper.get(com.ai.cmcchina.crm.constant.Constant.APP_LOGIN_TYPE + this.userName, ""));
        app.setAccount(this.accountName);
    }

    /* access modifiers changed from: private */
    public void loginWithSingleAccout(String datas) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("account", "");
        intent.putExtra("data", "");
        intent.putExtra("opName", "");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void loginSystem(String account, String accountId, String ORGID) {
        if (this.currentLocation == null) {
            UIUtil.alert((Context) this, "获取位置信息失败，请检查位置服务是否打开后重试！");
            return;
        }
        JSONObject data = createBaseData();
        data.put("LOGINACCT", (Object) account);
        data.put("imei", (Object) DeviceUtil.getImei(this) + TypeBarView.TYPE_NOTYPE + DeviceUtil.getMacAddress(this));
        data.put(BOSS_ACCOUNT_ID, (Object) accountId);
        data.put("loginType", (Object) "Andorid");
        data.put("terminalModel", (Object) DeviceUtil.getDeviceModel());
        data.put("latitude", (Object) this.currentLocation.getLatitude() + "");
        data.put("longitude", (Object) this.currentLocation.getLongitude() + "");
        Map<String, String> params = new HashMap<>();
        params.put(Constant.Server.ACTION, "LoginAction.loginSystem");
        params.put("data", data.toString());
        params.put(BOSS_ACCOUNT_ORG_ID, ORGID);
        params.put(com.ai.cmcchina.crm.constant.Constant.SESSION_ID, this.sessionId);
        final String str = account;
        final String str2 = ORGID;
        new GatewayManageHandler(this, (String) null, params) {
            public void onSuccess(String result) {
                //G3Logger.debug("====login success:" + result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!"0000".equals(jsonObject.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
                    UIUtil.alert((Context) LoginGesturePassword.this, "登录失败，失败原因: \n" + jsonObject.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY));
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONObject("apps_config").getJSONArray("APPS");
                App app = (App) LoginGesturePassword.this.getApplication();
                app.setAppsConfig(jsonArray.toJSONString());
                app.setStaffRegionId(str.substring(0, 1).toUpperCase(Locale.ENGLISH));
                LoginGesturePassword.this.saveOperatorInfo(jsonObject, str, str2);
                LoginGesturePassword.this.saveSessionIdToApplication();
                app.setCurrentMenus(jsonObject.getJSONArray("user_current_menus"));
                LoginGesturePassword.this.setAppAccount(str);
                LoginGesturePassword.this.startActivity(new Intent(LoginGesturePassword.this, MainActivity.class));
                LoginGesturePassword.this.closeLocationUpdate();
                LoginGesturePassword.this.finish();
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void showNameItemsAlert(String[] items, Map<String, String> mapping, Map<String, String> orgIdMapping, JSONArray accountArray) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                LoginGesturePassword.this.checkLockState(jsonObject, name, (String) map.get(name), (String) map2.get(name));
            }
        });
        builder.create().show();
    }

    private JSONObject createBaseData() {
        JSONObject data = new JSONObject();
        data.put(com.ai.cmcchina.crm.constant.Constant.SESSION_ID, (Object) getSessionId());
        return data;
    }

    private String getSessionId() {
        return ((App) getApplication()).getSessionId();
    }

    /* access modifiers changed from: private */
    public void saveOperatorInfo(JSONObject jsonObject, String account, String ORGID) {
        String opName = jsonObject.getString("opName");
        String orgName = jsonObject.getString("tingName");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("account", account).commit();
        prefs.edit().putString("tingId", ORGID).commit();
        prefs.edit().putString("tingName", orgName).commit();
        PreferenceHelper.commit("opName", opName);
        MemoryCacheUtil.setMemoryCache("IS_UNIFY_CLIENT", "Y");
        MemoryCacheUtil.setMemoryCache("account", account);
        MemoryCacheUtil.setMemoryCache("phone", this.telephone);
    }

    /* access modifiers changed from: private */
    public void setAppAccount(String account) {
        ((App) getApplication()).setAccount(account);
    }

    /* access modifiers changed from: private */
    public void societyChannelLogin(String username) {
        if (this.currentLocation == null) {
            UIUtil.alert((Context) this, "获取位置信息失败，请检查位置服务是否打开后重试！");
            return;
        }
        String password = PreferenceHelper.getWithKeyValueEncry(com.ai.cmcchina.crm.constant.Constant.GESTURE_PASSWORD + username, "");
        this.accountName = PreferenceHelper.getWithKeyValueEncry("gesture_passwordaccount" + username, "");
        JSONObject data = new JSONObject();
        data.put("loginName", (Object) username);
        data.put("staffNumber", (Object) this.accountName);
        data.put("loginPwd", (Object) password);
        data.put("imei", (Object) DeviceUtil.getImei(this));
        data.put("mac", (Object) DeviceUtil.getMacAddress(this));
        data.put("loginType", (Object) "Andorid");
        data.put("terminalModel", (Object) DeviceUtil.getDeviceModel());
        data.put("latitude", (Object) this.currentLocation.getLatitude() + "");
        data.put("longitude", (Object) this.currentLocation.getLongitude() + "");
        Map<String, String> params = new HashMap<>();
        params.put(Constant.Server.ACTION, "LoginAction.societyChannelLoginWithOutSms");
        params.put("data", data.toString());
        final String str = username;
        new GatewayManageHandler(this, "/mobiledata", params) {
            public void onSuccess(String result) {
                //G3Logger.debug(result);
                JSONObject json = (JSONObject) JSONObject.parse(result);
                if ("0000".equals(json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY))) {
                    String unused = LoginGesturePassword.this.sessionId = json.getString(com.ai.cmcchina.crm.constant.Constant.SESSION_ID);
                    PreferenceHelper.commit("usernameEmpty", str);
                    LoginGesturePassword.this.saveSessionIdToApplication();
                    JSONArray jsonArray = json.getJSONObject("apps_config").getJSONArray("APPS");
                    App app = (App) LoginGesturePassword.this.getApplication();
                    app.setAppsConfig(jsonArray.toJSONString());
                    app.setStaffRegionId(LoginGesturePassword.this.accountName.substring(0, 1).toUpperCase(Locale.ENGLISH));
                    MemoryCacheUtil.setMemoryCache("IS_UNIFY_CLIENT", "Y");
                    LoginGesturePassword.this.startActivity(new Intent(LoginGesturePassword.this, MainActivity.class));
                    LoginGesturePassword.this.closeLocationUpdate();
                    LoginGesturePassword.this.finish();
                    return;
                }
                String desc = json.getString(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY);
                try {
                    desc = ((JSONObject) JSONObject.parse(desc)).getString(com.ai.cmcchina.crm.constant.Constant.ERRDESC);
                } catch (Exception e) {
                    //G3Logger.debug(e);
                }
                UIUtil.toast((Context) LoginGesturePassword.this, (Object) desc);
            }
        }.start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            UIUtil.toast((Context) this, (Object) "再按一次退出程序");
            this.exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
        return true;
    }

    public void handleLocation(BDLocation paramBDLocation) {
        if (!LocationUtil.checkLocation(paramBDLocation)) {
            UIUtil.toast((Context) this, MessageFormat.format("经纬度信息异常：{0}--{1}", paramBDLocation.getLatitude(), paramBDLocation.getLongitude()));
            return;
        }
        if (this.currentLocation == null) {
            UIUtil.toast((Context) this, (Object) "获取位置信息成功！");
        }
        this.currentLocation = paramBDLocation;
        //G3Logger.debug("获取位置信息成功：" + this.currentLocation.getLatitude() + "__" + this.currentLocation.getLongitude());
    }

    /* access modifiers changed from: private */
    public void closeLocationUpdate() {
        try {
            LocationTool.getInstance(this).stopLocation();
        } catch (Exception e) {
            //G3Logger.debug(e);
        }
    }
}