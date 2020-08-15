package com.heclient.heapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ai.cmcchina.crm.handler.GatewayManageHandler;
import com.ai.cmcchina.crm.util.DeviceUtil;
import com.ai.cmcchina.crm.util.LocationUtil;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.esop.loc.LocationTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.uap.util.des.EncryptInterface;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heclient.heapp.App;
import com.heclient.heapp.MainActivity;
import com.heclient.heapp.R;
import com.wade.mobile.app.AppRecord;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.app.MobileOperation;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.IWadeMobileClient;
import com.wade.mobile.frame.WadeWebView;
import com.wade.mobile.frame.activity.TemplateMainActivity;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.frame.template.ResVersionManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.ui.build.dialog.progressdialog.SimpleProgressDialog;
import com.wade.mobile.ui.comp.dialog.ConfirmDialog;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.assets.AssetsUtil;
import com.wade.mobile.util.http.HttpTool;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    public static final String TAG = "homeFragment";
   /*界面组件声明*/
    private EditText inputUserName;
    private EditText inputPassword;
    private Button buttonLogin;
    private Button buttonCancel;
    private Button buttonClear;
    private TextView textHome;
    private View root=null; //全局root
    private Context mContext;
    private BottomNavigationView navView;
    private HomeViewModel homeViewModel;
    /*静态变量声明*/
    private static final String BOSS_ACCOUNT_ID = "APPACCTID";
    private static final String BOSS_ACCOUNT_LOCK = "LOCK";
    private static final String BOSS_ACCOUNT_NAME = "NAME";
    private static final String BOSS_ACCOUNT_ORG_ID = "ORGID";
    /*变量声明*/
    private String telephone ;
    private String sessionId ;
    private String accountName ;
    private String basePath;
    /*定位变量*/
    private Location currentLocation;
    private LocationTool.LocationHandler LocationHandler;
    private LatLng userLatLng;
    private BaiduMap bdMap;
    private ProgressDialog updateResProgressDialog;
    private String isForceUpdate;
    protected PluginManager pluginManager;
    private long loadingTime = Long.parseLong(MobileConfig.getInstance().getConfigValue("loading_time", "2000"));
    private static Bundle outState;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                  //  updateClient(); //更新客户端
                    return;
                case 1:
                    updateResource();//更新资源
                    return;
                case 2:
                    if (LoadingDialogStyle.HORIZONTAL.equals(getLoadingDialogStyle())) {
                        updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }
                    return;
                case 3:
                    if (updateResProgressDialog != null) {
                        updateResProgressDialog.dismiss();
                        return;
                    }
                    return;
                case 4:
                  /*try {
                        initWebview();
                        return;
                    } catch (Exception e) {
                        error(e);
                        return;
                    }*/
                    selectNavView(1); //跳转到navigation_dashboard
                    return;
                case 5:
                    //setContentView(TemplateMainActivity.this.mainLayout);
                    return;
                default:
                    return;
            }
        }
    };
    /**
     * activity重建后保存数据状态参考 https://www.jianshu.com/p/fb14480e47fb
     * @param savedInstanceState
     */
/*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }*/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);

        if(root==null){
            //
            root = inflater.inflate(R.layout.fragment_home, container, false);
        }

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBasePath();
        initView();
        setListener();
        initLocation();//初始化地图

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
      super.onSaveInstanceState(outState);
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(){

        this.textHome = root.findViewById(R.id.text_home);
        this.buttonLogin = root.findViewById(R.id.button_login);
        this.inputUserName = root.findViewById(R.id.edit_User);
        this.inputPassword= root.findViewById(R.id.edit_PSW);
        this.buttonCancel = root.findViewById(R.id.button_cancel);
        this.buttonClear = root.findViewById(R.id.button_clear);
        this.navView = this.getActivity().findViewById(R.id.nav_view); //底部导航栏,测试后需要从上层view获取，才能正常跳转

    }
    public void initBasePath() {
        basePath =MobileAppInfo.getSdcardAppPath(this.mContext)+ File.separator;
      //  System.out.println("HomeFragment-initBasePath0001"+basePath);
        TemplateManager.initBasePath(basePath);
    }
    private void setListener(){
        /*设置文本框监听事件*/
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textHome.setText(s);
            }
        });
        this.inputUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUserName.setText("");
            }
        });
        /*设置登陆按钮监听事件*/
        this.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                inputUserName.setText("gexuxu");
                inputPassword.setText("Aa123456!");*/

                final String username =inputUserName.getText().toString();
                final String password =inputPassword.getText().toString();

                if ("".equals(username)) {
                    inputUserName.setError("请输入用户名");
                    inputUserName.requestFocus();
                } else if ("".equals(password)) {
                    inputPassword.setError("请输入密码");
                    inputPassword.requestFocus();
                } else {

                    selfSupportChannelLogin(username,password);
                }

            }
        });
        /*设置取消录入按钮监听事件*/
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUserName.setText("");
                inputPassword.setText("");
                inputUserName.requestFocus();
            }
        });
        /*设置清空历史资源*/
        this.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String basePath =MobileAppInfo.getSdcardAppPath(this.mContext)+ File.separator;
                File file = new File(basePath);
                if(file.exists()){
                    deleteDirWihtFile(file);
                    UIUtil.toast(mContext, "清理完毕！");
                }else{
                    UIUtil.toast(mContext, "无需清理！");
                }

            }
        });

    }
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    private void initLocation() {
        Log.d(TAG, "initLocation: 0");
        LocationUtil.getInstance(this.getContext()).setOnLocationListener(new LocationUtil.CustomLocationListener() {
            public void onSuccess(Location location) {
                HomeFragment.this.currentLocation  = location;

                Log.d(TAG, "LocationUtil获取位置信息成功:" + location.getLongitude() + "--" + location.getLatitude());
                Log.d(TAG, "获取位置信息成功！");
            }

            public void onFailed(String msg) {
                UIUtil.toast((Context) HomeFragment.this.getContext(), (Object) msg);
            }
        });

        this.LocationHandler = new LocationTool.LocationHandler() {
            public void handleLocation(BDLocation location) {
                if (HomeFragment.this.bdMap != null) {
                    HomeFragment.this.bdMap.setMyLocationData(new MyLocationData.Builder().accuracy(location.getRadius()).direction(100.0f).latitude(location.getLatitude()).longitude(location.getLongitude()).build());
                    HomeFragment.this.bdMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, (BitmapDescriptor) null, 17170445, 17170445));
                    LatLng unused = HomeFragment.this.userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    /*if (HomeFragment.this.isFirstLoc || HomeFragment.this.isManualLoc) {
                        HomeFragment.this.setCenterPoint(HomeFragment.this.userLatLng);
                    }*/
                }
            }
        };
        LocationTool.getInstance(HomeFragment.this.mContext).startLocation();
        LocationTool.getInstance(HomeFragment.this.mContext).setLocationHandler(this.LocationHandler);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
      //  this.mContext =this.getActivity();
    }

/*    private MainActivity getMainActivity() {
        return ((MainActivity) mActivity);
    }*/

    @Override
    public void onPause() {
        super.onPause();//通常是当前的acitivty被暂停了，比如被另一个透明或者Dialog样式的Activity覆盖了
      //  System.out.println("HomeFragment-onPause");
        super.onPause();
        if (outState != null) {
            outState.clear();
            outState = null;
        }
        outState = new Bundle();
        String inputUserNameStr = inputUserName.getText().toString();
        outState.putString("inputUserName",inputUserNameStr);
        String inputPasswordStr=inputPassword.getText().toString();
        outState.putString("inputPassword",inputPasswordStr);
        outState.putBoolean("loginIsEnable",buttonLogin.isEnabled());//
      //  System.out.println("状态保存：名称"+inputUserNameStr+"密码："+inputPasswordStr);



    }


    @Override
    public void onResume() {
        super.onResume();//被激活
      if(outState!=null){
            String inputUserNameStr= outState.getString("inputUserName");
            inputUserName.setText(inputUserNameStr);
            String inputPasswordStr=outState.getString("inputPassword");
            inputPassword.setText(inputPasswordStr);
            boolean isEnable=outState.getBoolean("loginIsEnable");//
            buttonLogin.setEnabled(isEnable);
         //   System.out.println("恢复：名称"+inputUserNameStr+"密码："+inputPasswordStr);
        }

    }

    private void selectNavView(int ItemId){
          //切换底层导航栏
        switch (ItemId) {
            case 1:
           //     System.out.println("R.id.navigation_dashboard"+R.id.navigation_dashboard);
                Navigation.findNavController(this.root).navigate(R.id.navigation_dashboard);//navigation_dashboard
                return;
            case 2:
                Navigation.findNavController(this.root).navigate(R.id.navigation_notifications);//navigation_notifications
                return;
            default:
                Navigation.findNavController(this.root).navigate(R.id.navigation_home);//navigation_home
                return;
        }
    }
    private String getSessionId() {
        //   ((App) Objects.requireNonNull(getActivity()).getApplication()).getSessionId();
        return  ((App)mContext.getApplicationContext()).getSessionId();
    }
    private void saveSessionIdToApplication(String sessionId2) {
        App app =(App)mContext.getApplicationContext().getApplicationContext();
             //  App app = (App) getActivity().getApplication();
        app.setSessionId(sessionId2);
        app.setAccountChannelType("loginTypeSelfSupportChannel");
    }
    //登陆
    @SuppressLint("HandlerLeak")
    public void selfSupportChannelLogin(String username, String password){
        JSONObject data = new JSONObject();
        data.put("mainAcct",username);
        data.put("terminalModel","ONEPLUS A5000");
        String encLogPwd = EncryptInterface.desEncryptData(password);
        Log.d(TAG, "selfSupportChannelLogin-encLogPwd:"+encLogPwd);
        data.put("loginPwd",encLogPwd );
        data.put("imei","799507427493373");
        data.put("mac","02:00:00:00:00:00");
        data.put("version","3.77");
        Map<String, String> params = new HashMap<>();
        params.put("action", "LoginAction.selfSupportLoginWithOutSms");
        params.put("data", data.toString());
        new GatewayManageHandler(getActivity(),"/mobiledata", params) {
            public void onSuccess(String result) {
                Log.d(TAG, "selfSupportChannelLogin-onSuccess: "+result);
                JSONObject json = (JSONObject) JSONObject.parse(result);
                if ("0000".equals(json.getString("returnCode"))) {
                    buttonLogin.setEnabled(false);//禁用登陆按钮
                    JSONObject beanObj = json.getJSONObject("returnMsg").getJSONObject("bean");
                    JSONArray accountArray = json.getJSONObject("returnMsg").getJSONArray("beans");
                  String unused = telephone = (String) beanObj.get("MOBILE");
                    String unused2 =sessionId = json.getString( "SESSION_ID");
                    HomeFragment.this.saveSessionIdToApplication(unused2);
                    if (accountArray.isEmpty()) {
                        /*无下挂工单，则单账户登陆*/
                        HomeFragment.this.loginWithSingleAccout(result);
                    } else if (accountArray.size() == 1) {
                        JSONObject jsonObj = accountArray.getJSONObject(0);
                        String accountId = jsonObj.getString(BOSS_ACCOUNT_ID);
                        String unused3 =accountName = jsonObj.getString(BOSS_ACCOUNT_NAME);
                        HomeFragment.this.checkLockState(jsonObj,accountName, accountId, jsonObj.getString(HomeFragment.BOSS_ACCOUNT_ORG_ID));
                    } else {
                        HomeFragment.this.showNameItemsAlert(getNameItems(accountArray), transNameAndIdMapping(accountArray), transNameAndOrgIdMapping(accountArray), accountArray);
                    }
                } else {
                    String desc = json.getString("returnMsg");
                    try {
                        desc = ((JSONObject) JSONObject.parse(desc)).getJSONObject("bean").getString("ERRDESC");
                    } catch (Exception e) {
                        Log.e(TAG, "Err-onSuccess: ",e );
                    }
                    UIUtil.toast((Context) HomeFragment.this.mContext, (Object) desc);
                }
            }

            private Map<String, String> transNameAndIdMapping(JSONArray accountArray) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject object = accountArray.getJSONObject(i);
                    map.put(object.getString("NAME"), object.getString("APPACCTID"));
                }
                return map;
            }

            private Map<String, String> transNameAndOrgIdMapping(JSONArray accountArray) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < accountArray.size(); i++) {
                    JSONObject object = accountArray.getJSONObject(i);
                    map.put(object.getString("NAME"), object.getString("ORGID"));
                }
                return map;
            }

            private String[] getNameItems(JSONArray accountArray) {
                String[] items = new String[accountArray.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = accountArray.getJSONObject(i).getString("NAME");
                }
                return items;
            }
        }.start();
    }
    /*判断账号状态,并登陆系统*/
    private void checkLockState(JSONObject jsonObject, String name, String id, String orgId) {

        String lock = jsonObject.getString(BOSS_ACCOUNT_LOCK);
        String desc;
        if ("1".equalsIgnoreCase(lock)) {
            desc="该账号【" + name + "】状态正常，开始登录系统！";
            UIUtil.toast((Context) mContext, (Object) desc);
            loginSystem(name, id, orgId);
        } else if ("0".equalsIgnoreCase(lock)) {
            Toast.makeText(mContext,
                    "该账号【" + name + "】已加锁，不能进行登录和业务办理！", Toast.LENGTH_LONG).show();
        } else if ("2".equalsIgnoreCase(lock)) {
            Toast.makeText(mContext,
                    "该账号【" + name + "】已失效(停用)，不能进行登录和业务办理！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext,
                    "该账号【" + name + "】LOCK状态异常（\" + lock + \"），不能进行登录和业务办理！", Toast.LENGTH_LONG).show();
        }

    }
    /*无下挂工号，则账号直接登陆*/
    private void loginWithSingleAccout(String datas) {
    /*        Intent intent = new Intent(this.mContext,MainActivity.class);
        intent.putExtra("account", "");
        intent.putExtra("data", "");
        intent.putExtra("opName", "");
        startActivity(intent);
        getActivity().finish();*/
        selectNavView(1);

    }
    /*显示选择工号对话框*/
    private void showNameItemsAlert(String[] items, Map<String, String> mapping, Map<String, String> orgIdMapping, JSONArray accountArray) {
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
                HomeFragment.this.checkLockState(jsonObject, name, (String) map.get(name), (String) map2.get(name));
            }
        });
        builder.create().show();
    }
    /*登陆系统*/
    @SuppressLint("HandlerLeak")
    private void loginSystem(String account, String accountId, String ORGID) {
        //检查位置服务
        Location currentLocation = HomeFragment.this.getCurrentLocation();
        if (currentLocation == null) {
            UIUtil.alert(mContext, "获取位置信息失败，请检查位置服务是否打开后重试！");
            return;
        }

        JSONObject data = new JSONObject();
        data.put("SESSION_ID", (Object) getSessionId());
        data.put("LOGINACCT", (Object) account);
        data.put("imei", (Object) DeviceUtil.getImei(this.mContext) + "#" + DeviceUtil.getMacAddress(this.mContext));
        data.put(BOSS_ACCOUNT_ID, (Object) accountId);
        data.put("loginType", (Object) "Andorid");
        data.put("terminalModel", (Object) DeviceUtil.getDeviceModel());
        data.put("latitude",(Object) currentLocation.getLatitude() + "" ); //纬度 (Object) currentLocation.getLatitude() + "" 模拟许昌三鼎华悦坐标
        data.put("longitude",(Object) currentLocation.getLongitude() + "");//经度 (Object) currentLocation.getLongitude() + ""
        Map<String, String> params = new HashMap<>();
        params.put("action", "LoginAction.loginSystem");
        params.put("data", data.toString());
        params.put(BOSS_ACCOUNT_ORG_ID, ORGID);
        params.put("SESSION_ID", this.sessionId);
        final String str = account;
        final String str2 = ORGID;
        new GatewayManageHandler(getActivity(), (String) null, params) {
            public void onSuccess(String result)  {
                Log.d(TAG, "GatewayManageHandler-loginSystem:" + result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!"0000".equals(jsonObject.getString("returnCode"))) {
                    Log.d(TAG, "登录失败，失败原因: \n" + jsonObject.getString("returnMsg"));
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONObject("apps_config").getJSONArray("APPS");

/*              App app = (App) HomeFragment.this.getActivity().getApplication();
                app.setAppsConfig(jsonArray.toJSONString());
                app.setStaffRegionId(str.substring(0, 1).toUpperCase(Locale.ENGLISH));
                HomeFragment.this.saveOperatorInfo(jsonObject, str, str2);
                app.setCurrentMenus(jsonObject.getJSONArray("user_current_menus"));
                HomeFragment.this.setAppAccount(str);*/

                try {
                    update();
                } catch (Exception e) {
                    System.out.println("更新错误:" + e.getMessage());
                }
              //  selectNavView(1); //跳转到navigation_dashboard
               /* HomeFragment.this.closeLocationUpdate();
                HomeFragment.this.getActivity().finish();*/
            }
        }.start();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    public void closeLocationUpdate() {
        try {
            LocationTool.getInstance(this.getContext()).stopLocation();
        } catch (Exception e) {
            Log.e(TAG, "closeLocationUpdate: ",e);
        }
    }
    private void update() throws Exception {
        new MobileThread("Update") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                long start = System.currentTimeMillis();
                String versions = getVersion();//
              //  System.out.println("HomeFragment-update:0:"+versions);
                JSONObject jsonObject = JSONObject.parseObject(versions);
                if (isUpdateClient(jsonObject.getString(Constant.ServerConfig.CLIENT_VERSION))) {
                    String unused = isForceUpdate = jsonObject.getString(Constant.ServerConfig.IS_FORCE_UPDATE);
                    HomeFragment.this.handler.sendEmptyMessage(0);
                    return;
                }
               //  System.out.println("0002:");
                if (AppRecord.isFirst(HomeFragment.this.getActivity())) {
                    try {
                        AssetsUtil.copyAssetsDir(HomeFragment.this.getActivity(), MobileAppInfo.getInstance(HomeFragment.this.getActivity()).getAppPath(), MobileAppInfo.getSdcardAppPath(HomeFragment.this.getActivity()));
                        AppRecord.dirtyFirst(HomeFragment.this.getActivity());
                    } catch (Exception e) {
                      System.out.println("复制文件错误:" + e.getMessage());
                    }
                }
               // System.out.println("HomeFragment-update:2:");
                String resKey = HomeFragment.this.getResKey();
               // System.out.println("0003:"+resKey);
                if (resKey != null) {
                    TemplateManager.initResKey(resKey);
                }
               // System.out.println("0004"+resKey);
                Map<String, ?> remoteResVersions = ResVersionManager.getRemoteResVersions();
               // System.out.println("0005"+remoteResVersions.toString());

                if (ResVersionManager.isUpdateResource(HomeFragment.this.getActivity(), remoteResVersions)) {
                 //   System.out.println("HomeFragment-update:3:"+remoteResVersions.toString());
                    HomeFragment.this.handler.sendEmptyMessage(1);//升级资源
                    return;
                }

                long period = System.currentTimeMillis() - start;
                if (period <HomeFragment.this.loadingTime) {
                    Thread.sleep(HomeFragment.this.loadingTime - period);
                }
               // System.out.println("0005------------------");
                HomeFragment.this.handler.sendEmptyMessage(4);
            }

        }.start();
    }
/*    public void error(Exception e) {
        this.handler.sendEmptyMessage(3);
        if (e instanceof SocketTimeoutException) {
            UIUtil.alert(this.mContext,Messages.CONN_SERVER_FAILED);
            return;
        }
        System.out.println("启动错误:" + e.getMessage());
    }*/
    public String getVersion() throws Exception {
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.Version.VERSION_ACTION);
        String url = MobileConfig.getInstance().getRequestUrl();
       // System.out.println("TemplateMainActivity-getVersion:"+postParam.toString());
        String param= HttpTool.urlEncode(HttpTool.toQueryString(postParam), MobileConfig.getInstance().getEncode());
        String reqStr = HttpTool.httpRequest(url,param, "POST","TemplateMainActivity-getVersion0");
        return reqStr;
    }
    /* access modifiers changed from: protected */
    public String getResKey() throws Exception {
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.MobileSecurity.RES_KEY_ACTION);
        MobileSecurity.init(this.getActivity());
        postParam.put(Constant.Server.KEY, MobileSecurity.getDesKey());

        String url =MobileConfig.getInstance().getRequestUrl();
       // System.out.println("TemplateMainActivity-getResKey:1"+url);
        String param= HttpTool.toQueryStringWithEncode(postParam);
       // System.out.println("TemplateMainActivity-getResKey:2"+param);
        String req=HttpTool.httpRequest(url, param, "POST","TemplateMainActivity-getResKey");
       // System.out.println("TemplateMainActivity-getResKey:3"+req);
        req=MobileSecurity.responseDecrypt(req);
       // System.out.println("TemplateMainActivity-getResKey:4:"+req);
        JSONObject jsonObject = JSONObject.parseObject(req);
        String reqStr =jsonObject.getString("KEY");
        return reqStr;

    }
    public boolean isUpdateClient(String clientVersion) throws PackageManager.NameNotFoundException {
        return !clientVersion.equals(MobileAppInfo.getInstance(this.mContext).getVersionName());
    }
    /*下载主框架资源*/
    public void updateResource() {
        if (isHintWithUpdateRes()) {
            new ConfirmDialog(this.getActivity(), "资源更新", "远端发现新资源,是否更新") {
                /* access modifiers changed from: protected */
                public void okEvent() {
                    super.okEvent();
                    HomeFragment.this.updateRes();
                }

                /* access modifiers changed from: protected */
                public void cancelEvent() {
                    super.cancelEvent();
                    if (isSkipUpdateRes()) {
                        if (AppRecord.isFirst(HomeFragment.this.getActivity())) {
                            MobileOperation.exitApp();
                        }
                        HomeFragment.this.handler.sendEmptyMessage(3);
                        HomeFragment.this.handler.sendEmptyMessage(4);
                        return;
                    }
                    MobileOperation.exitApp();
                }
            }.show();
        } else {
            updateRes();
        }
    }
    public boolean isHintWithUpdateRes() {
        return true;
    }
    public boolean isSkipUpdateRes() {
        return false;
    }
    //
    public void updateRes() {
        this.updateResProgressDialog = createUpdateResProgressDialog();
        this.updateResProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                HintHelper.tip((Activity)getActivity(), "应用退出,请重新启动");
                MobileOperation.exitApp();
            }
        });
        this.updateResProgressDialog.show();
        new MobileThread("updateResource") {
            /* access modifiers changed from: protected */
            public void execute() {
                TemplateManager.resetDownloadPercent();
                try {
                    TemplateManager.downloadResource(HomeFragment.this.handler, HomeFragment.this.getActivity());
                } catch (Exception e) {
                    System.out.println("HomeFragment-updateResource错误："+e.getMessage());
                    return;//退出
                   // e.printStackTrace();
                }
                //下载完毕处理
                HomeFragment.this.handler.sendEmptyMessage(3);
                HomeFragment.this.handler.sendEmptyMessage(4);
            }
            /* access modifiers changed from: protected */
           /* public void error(Exception e) {
                HomeFragment.this.error(e);
            }*/
        }.start();
    }
    /*显示下载资源框*/
    public ProgressDialog createUpdateResProgressDialog() {
        SimpleProgressDialog simpleProgressDialog = (SimpleProgressDialog) new SimpleProgressDialog(this.getActivity()).setMessage(getProgressTitleUpdateRes());
        if (LoadingDialogStyle.HORIZONTAL.equals(getLoadingDialogStyle())) {
            simpleProgressDialog.setProgressStyle(1);
            simpleProgressDialog.getProgressDialog().setMax(ResVersionManager.updateCount);
            simpleProgressDialog.getProgressDialog().getWindow().setGravity(17);
        }
        return simpleProgressDialog.build();
    }
    public String getProgressTitleUpdateRes() {
        return Messages.RES_INIT;
    }

    protected enum LoadingDialogStyle {
        SPINNER,
        HORIZONTAL
    }
    public LoadingDialogStyle getLoadingDialogStyle() {
        return LoadingDialogStyle.HORIZONTAL;
    }
}