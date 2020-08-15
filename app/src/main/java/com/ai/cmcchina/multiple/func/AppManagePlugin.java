package com.ai.cmcchina.multiple.func;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import androidx.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.ailk.common.data.impl.DatasetList;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.uap.util.des.EncryptInterface;

import com.ai.cmcchina.crm.bean.OrderMarketingProduct;

import com.ai.cmcchina.crm.fragment.BaseFragment;

import com.ai.cmcchina.crm.util.DeviceUtil;
import com.ai.cmcchina.crm.util.Global;
import com.ai.cmcchina.crm.util.ImageUtil;
import com.ai.cmcchina.crm.util.OrderMarketingDBUtil;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.crm.util.UIUtil;

import com.ai.cmcchina.multiple.LoginFragmentActivity;

import com.ai.cmcchina.multiple.MultipleCustomWindowActivity;
import com.ai.cmcchina.multiple.SubAppActivity;
import com.ai.cmcchina.multiple.WebViewActivity;
import com.ai.cmcchina.multiple.handler.FileUploadHandler;

import com.ai.cmcchina.multiple.util.FileSelectorUtil;
import com.ai.cmcchina.multiple.util.OpenRealNameAuthClientUtil;
import com.heclient.heapp.App;


import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.app.MobileUtil;
import com.wade.mobile.app.SimpleUpdate;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeMobileActivity;
import com.wade.mobile.frame.multiple.MultipleAppConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.ui.activity.CustomWindowActivity;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.DirectionUtil;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.MobileGraphics;
import com.wade.mobile.util.http.HttpTool;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;


/* renamed from: com.ai.cmcchina.multiple.func.AppManagePlugin */
public class AppManagePlugin extends Plugin {
    private static final String APP_ID = "wx8b169c17a6a29e5e";
    private static final int TAKE_PHOTO = 18823;
    private static final int TAKE_PHOTO_200K = 1907;
    private int OPEN_IPU_APP = 1;
    private int OPEN_NATIVE_APP = 1;
    /* access modifiers changed from: private */
   // public HardwareConnectHelper hardwareConnectHelper;
    private Map<String, NativeAppConfig> nativeAppConfigs = new HashMap();
    private String photoFullPath;

    public AppManagePlugin(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void initAppConfig(JSONArray params) throws Exception {
        App app = (App)this.wademobile.getActivity().getApplication();
        String str = params.getString(0);
        System.out.println("AppManagePlugin-initAppConfig:0:"+str);
        if (isNull(str)) {
            error("App配置信息初始化异常!");
            return;
        }
        com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray) JSON.parse(str);
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                System.out.println("AppManagePlugin-initAppConfig:1:"+json.getString("appPath"));
                if ("I".equals(json.getString("appType"))) {
                    MultipleAppConfig multipleAppConfig = new MultipleAppConfig(json.getString("requestHost"), json.getString("requestPath"), json.getString("requestServlet"), json.getString("appPath"));
                    multipleAppConfig.putDefine("appWelcomePage", json.getString("appWelcomePage"));
                    multipleAppConfig.putDefine("appIndexPage", json.getString("appIndexPage"));
                    String str1 = json.getString("appId");
                    if ("raw/crmApp".equals(str1)) {
                        app.setCrmServerPath(json.getString("requestHost") + json.getString("requestPath"));
                        System.out.println("AppManagePlugin-initAppConfig:2:"+json.getString("requestHost") + json.getString("requestPath"));
                    } else if ("marketingApp".equals(str1)) {
                     //   app.setPartnerServerPath(json.getString("requestHost") + json.getString("requestPath"));
                    } else if ("esopApp".equals(str1)) {
                     //   app.setEsopServerPath(json.getString("requestHost") + json.getString("requestPath"));
                    }
                    MultipleManager.putAppConfig(str1, multipleAppConfig);
                }
                if ("N".equals(json.getString("appType"))){
                    this.nativeAppConfigs.put(json.getString("appId"), new NativeAppConfig(json.getString("packageName"), json.getString("className"), json.getString("downloadUrl")));
                }
            }
        }



     /*   Iterator<Object> iterator = (new DatasetList(str)).iterator();
        while (true) {
            if (iterator.hasNext()) {
                IData iData =(IData) iterator.next();
                System.out.println("AppManagePlugin-initAppConfig:1:"+iData.getString("appType"));
                if ("I".equals(iData.getString("appType"))) {
                    MultipleAppConfig multipleAppConfig = new MultipleAppConfig(iData.getString("requestHost"), iData.getString("requestPath"), iData.getString("requestServlet"), iData.getString("appPath"));
                    multipleAppConfig.putDefine("appWelcomePage", iData.getString("appWelcomePage"));
                    multipleAppConfig.putDefine("appIndexPage", iData.getString("appIndexPage"));
                    String str1 = iData.getString("appId");
                    if ("crmApp".equals(str1)) {
                        app.setCrmServerPath(iData.getString("requestHost") + iData.getString("requestPath"));
                    } else if ("marketingApp".equals(str1)) {
                        app.setPartnerServerPath(iData.getString("requestHost") + iData.getString("requestPath"));
                    } else if ("esopApp".equals(str1)) {
                        app.setEsopServerPath(iData.getString("requestHost") + iData.getString("requestPath"));
                    }
                    MultipleManager.putAppConfig(str1, multipleAppConfig);
                    continue;
                }
                if ("N".equals(iData.getString("appType")))
                    this.nativeAppConfigs.put(iData.getString("appId"), new NativeAppConfig(iData.getString("packageName"), iData.getString("className"), iData.getString("downloadUrl")));
                continue;
            }
            return;
        }*/
    }

    public void openIpuApp(JSONArray params) throws Exception {
        String basePath = TemplateManager.getBasePath();
        String appId = params.getString(0);
      //  G3Logger.debug("openIpuApp(JSONArray params) params:" + params.toString());
        System.out.println("AppManagePlugin-openIpuApp:0:"+basePath);
        System.out.println("AppManagePlugin-openIpuApp:1:"+params.toString());
        MultipleManager.setCurrAppId(appId);
        MultipleManager.setMultiple(true);
        MultipleManager.setMultBasePath(basePath);
        Intent intent = new Intent(this.wademobile.getActivity(), SubAppActivity.class);
        String appKey =MultipleManager.getAppConfig(appId).getDefine("appIndexPage");
        System.out.println("AppManagePlugin-openIpuApp:1:"+appKey);
        intent.putExtra("INDEX_PAGE",appKey);
        startActivityForResult(intent, this.OPEN_IPU_APP);

    }

    public void openSubAppPage(JSONArray params) throws Exception {
        String basePath = TemplateManager.getBasePath();
        IData info = new DataMap(params.getString(0));
        String appId = info.getString("appId");
        String subPageInfo = info.getString("subPage");
        String type = info.getString("type", "");
        MultipleManager.setCurrAppId(appId);
        MultipleManager.setMultiple(true);
        MultipleManager.setMultBasePath(basePath);
        Intent intent = new Intent(this.wademobile.getActivity(), SubAppActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("INDEX_PAGE", subPageInfo);
        startActivityForResult(intent, this.OPEN_IPU_APP);
    }

    public void openNativeApp(JSONArray params) throws Exception {
        NativeAppConfig appConfig = this.nativeAppConfigs.get(params.getString(0));
        String packageName = appConfig.getPackageName();
        String className = appConfig.getClassName();
        if (MobileUtil.checkActivity(this.context, packageName, className)) {
            String ipuParam = params.getString(1);
            if (isNull(ipuParam)) {
                error("打开第三方应用参数异常");
            }
            IData ipuData = new DataMap(ipuParam);
            String subsystem = ipuData.getString(Constant.FromWhichSubsystem);
            if (!StringUtil.isNotBlank(subsystem) || !"ESOP".equalsIgnoreCase(subsystem)) {
                new SharedPrefHelper(this.context).remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
            } else {
                SharedPrefHelper spHelper = new SharedPrefHelper(this.context);
                spHelper.remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
                spHelper.put(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem, (Object) subsystem);
            }
            ComponentName cn = new ComponentName(packageName, className);
            Intent intent = new Intent();
            intent.putExtra("STAFF_ID", ipuData.getString("STAFF_ID"));
            intent.putExtra("ACCOUNT", ipuData.getString("ACCOUNT"));
            intent.setComponent(cn);
            startActivityForResult(intent, this.OPEN_NATIVE_APP);
            return;
        }
        new SimpleUpdate(this.context, appConfig.getDownloadUrl()).update();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String resultData;
//        G3Logger.debug("onActivityResult requestCode>>>" + requestCode);
//        G3Logger.debug("onActivityResult resultCode>>>" + resultCode);
        if (requestCode == this.OPEN_IPU_APP) {
            MultipleManager.setMultiple(false);
        } else if (requestCode == BaseFragment.REQUEST_CODE) {
            realNameCallback(intent);
        } else if (requestCode == 200) {
            if (intent != null && (resultData = intent.getStringExtra("result")) != null) {
                callback(resultData, true);
            }
       /* } else if (resultCode == -1 && requestCode == BaseFragment.GET_SN_REQUEST_CODE) {
            if (this.hardwareConnectHelper != null) {
                this.hardwareConnectHelper.getSN(intent);
            }
        } else if (resultCode == -1 && requestCode == BaseFragment.WRITE_CARD_REQUEST_CODE) {
            if (this.hardwareConnectHelper != null) {
                this.hardwareConnectHelper.writeCard(intent);
            }*/
        } else if (resultCode == -1 && requestCode == BaseFragment.NO_PAPER_SUCC) {
            IData params = new DataMap();
            params.put("resultCode", "0000");
            callback(params.toString());
        } else if (resultCode == -1 && requestCode == 18071) {
            callback(FileSelectorUtil.getFilePath(this.context, intent));
        } else if (resultCode == -1 && requestCode == 18072) {
            try {
                String path = FileSelectorUtil.getFilePath(this.context, intent);
                Bitmap bitmap = MobileGraphics.compressImage(ImageUtil.file2Bitmap(new File(path)), 200.0d, 80);
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                if (height > width) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(270.0f);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                }
                String str = ImageUtil.bitmapEncodeToBase64String(bitmap).replace("+", "%2B").replace("=", "%3D").replaceAll("[\\s*\t\n\r]", "");
                //G3Logger.debug("选择本地图片压缩成功 str：" + str);
                IData params2 = new DataMap();
                params2.put("picture", str);
                params2.put(Constant.MobileWebCacheDB.URL_COLUMN, path);
                callback(params2.toString());
            } catch (Exception e) {
                e.printStackTrace();
//                G3Logger.debug(e);
//                G3Logger.debug("FILE_SELECT_IMG_COMPRESS_REQUEST 图片压缩失败");
            }
        } else if (resultCode == -1 && requestCode == TAKE_PHOTO) {
            try {
                try {
                    Bitmap bitmap2 = MobileGraphics.compressImage(ImageUtil.file2Bitmap(new File(this.photoFullPath)), 200.0d, 80);
                    MobileGraphics.savePicToLocal(bitmap2, this.photoFullPath);
                    int height2 = bitmap2.getHeight();
                    int width2 = bitmap2.getWidth();
                    if (height2 > width2) {
                        Matrix matrix2 = new Matrix();
                        matrix2.postRotate(270.0f);
                        bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, width2, height2, matrix2, true);
                    }
                    IData params3 = new DataMap();
                    params3.put("picture", ImageUtil.bitmapEncodeToBase64String(bitmap2).replace("+", "%2B").replace("=", "%3D").replaceAll("[\\s*\t\n\r]", ""));
                    params3.put(Constant.MobileWebCacheDB.URL_COLUMN, this.photoFullPath);
                    callback(params3.toString());
                } catch (Exception e2) {
                   // G3Logger.debug("获取拍照图片出错:" + e2.getStackTrace().toString());
                    UIUtil.toast((Context) this.context, (Object) "获取拍照图片出错，请重试！");
                }
            } catch (Exception var10) {
                HintHelper.alert(this.context, var10.getMessage());
            }
        } else if (resultCode == -1 && requestCode == TAKE_PHOTO_200K) {
           // G3Logger.debug("onActivityResult TAKE_PHOTO_200K!!!");
            try {
                Bitmap bitmap3 = MobileGraphics.compressImage(ImageUtil.file2Bitmap(new File(this.photoFullPath)), 200.0d, 80);
                MobileGraphics.savePicToLocal(bitmap3, this.photoFullPath);
                callback(this.photoFullPath);
                bitmap3.recycle();
            } catch (Exception var102) {
                HintHelper.alert(this.context, var102.getMessage());
            }
        }
    }

    public void getAppsConfig(JSONArray params) throws Exception {
        App app = (App) this.context.getApplication();
      /* String config = app.getAppsConfig();
        if (StringUtil.isBlank(config)) {
            logout((JSONArray) null);
            return;
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(Global.SESSION_ID, (Object) app.getSessionId());
        jsonObj.put("apps_config", (Object) config);
      //  jsonObj.put("account_type", (Object) app.getAccountChannelType());
        callback(jsonObj.toJSONString());*/
    }

    public void saveCustomerPhoneNumAndName(JSONArray params) throws Exception {
        App app = (App) this.context.getApplication();
        String customerPhoneNum = params.getString(0);
        String customerFullName = params.getString(1);
       // app.setNum(customerPhoneNum);
      //  app.setUserFullName(customerFullName);
    }

    public void saveStaffRegionId(JSONArray params) throws Exception {
        //((App) this.context.getApplication()).setStaffRegionId(params.getString(0));
    }

    public void setIsCollectedChange(JSONArray params) throws JSONException {
        String isCollectedChange = params.getString(0);
        App app = (App) this.context.getApplication();
        if (com.wade.mobile.util.Constant.TRUE.equalsIgnoreCase(isCollectedChange)) {
           // app.setIsCollectedChange(true);
        } else {
          //  app.setIsCollectedChange(false);
        }
    }

    public void getIsCollectedChange(JSONArray params) {
        //callback(((App) this.context.getApplication()).getIsCollectedChange().toString());
    }

    public void logout(JSONArray params) throws Exception {
       // G3Logger.debug("AppManagePlugin.logout");
        MultipleManager.setMultiple(false);
        if (this.context != null) {
           // G3Logger.debug("AppManagePlugin: startActivity-LoginFragmentActivity");
            this.context.startActivity(new Intent(this.context, LoginFragmentActivity.class));
            this.context.finish();
           // G3Logger.debug("AppManagePlugin: SubAppActivity.finish");
        }
        /*if (Main2Activity.instance != null) {
            Main2Activity.instance.finish();
           // G3Logger.debug("MainActivity: SubAppActivity.finish");
        }*/
    }

    public void getOperatorInfo(JSONArray params) throws Exception {
        App app = (App) this.context.getApplication();
      /*  String account = app.getAccount();
        String accountType = app.getAccountChannelType();*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        JSONObject jsonObj = new JSONObject();
      /*  if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL.equals(accountType)) {
            jsonObj.put("account", (Object) account);
            jsonObj.put("username", (Object) "");
            jsonObj.put("password", (Object) "");
            jsonObj.put("accountType", (Object) accountType);
            jsonObj.put("num", (Object) app.getNum());
            jsonObj.put("sessionId", (Object) app.getSessionId());
            jsonObj.put("currentMenus", (Object) app.getCurrentMenus());
            jsonObj.put("operator", (Object) getOperatorInfo(app));
        } else if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(accountType)) {
            String username = PreferenceHelper.get("usernameEmpty", "");
            String password = prefs.getString("passwordEmpty", "");
            jsonObj.put("account", (Object) account);
            jsonObj.put("username", (Object) username);
            jsonObj.put("password", (Object) password);
            jsonObj.put("accountType", (Object) accountType);
            jsonObj.put("bossAcount", (Object) app.getPartnerBossAccount());
        } else if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_PARTNER_CHANNEL.equals(accountType)) {
            jsonObj.put("accountType", (Object) accountType);
            String userName = PreferenceHelper.get("partnerUserName", "");
            if (Constant.PartnerChannlRole.ADMIN.getType().equals(app.getPartnerAccountType()) || Constant.PartnerChannlRole.MANAGER.getType().equals(app.getPartnerAccountType())) {
                jsonObj.put("account", (Object) userName);
            }
            if (Constant.PartnerChannlRole.PARTNER_SUB_ACCOUNT.getType().equals(app.getPartnerAccountType()) || Constant.PartnerChannlRole.PARTNER.getType().equals(app.getPartnerAccountType())) {
                jsonObj.put("managerAccount", (Object) app.getPartnerBossAccount());
                jsonObj.put("phoneNumber", (Object) userName);
            }
            jsonObj.put("partnerAccountType", (Object) app.getPartnerAccountType());
        }*/
       // G3Logger.debug("callback: " + jsonObj.toString());
        callback(jsonObj.toJSONString());
    }

    private JSONObject getOperatorInfo(App app) {
        if (app == null) {
            app = (App) this.context.getApplication();
        }
       JSONObject operator = new JSONObject();
      /*   operator.put("fullName", (Object) app.getUserFullName());
        operator.put("phoneNum", (Object) "");
        operator.put("orgName", (Object) app.getTingName());
        operator.put("operatorId", (Object) app.getAccount());*/
        return operator;
    }

    public void setOrderMarketingInfo(JSONArray params) throws Exception {
        OrderMarketingDBUtil.ProductDao mDao = new OrderMarketingDBUtil.ProductDaoImpl(this.context);
        if (params.length() != 0) {
            String beans = params.getString(0);
            if (!StringUtil.isBlank(beans)) {
                com.alibaba.fastjson.JSONArray array = (com.alibaba.fastjson.JSONArray) JSON.parse(beans);
                if (array.size() > 0) {
                    List<OrderMarketingProduct> list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        OrderMarketingProduct product = new OrderMarketingProduct();
                        JSONObject json = array.getJSONObject(i);
                        product.setCode(json.getString("PRODUCT_CODE"));
                        product.setDesc(json.getString("PRODUCT_DESC"));
                        product.setId(json.getString("PRODUCT_ID"));
                        product.setName(json.getString("PRODUCT_NAME"));
                        product.setType(json.getString("PRODUCT_TYPE"));
                        product.setCategory(json.getString("type"));
                        list.add(product);
                    }
                    mDao.clear();
                    mDao.addAll(list);
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
                    pref.edit().putString(Global.PrefKey.LAST_REFRESH, new SimpleDateFormat(Global.DATE_FORMAT_2, Locale.CHINA).format(new Date())).commit();
                }
            }
        }
    }

    public void getOrderMarketingInfo(JSONArray params) throws Exception {
        OrderMarketingDBUtil.ProductDao mDao = new OrderMarketingDBUtil.ProductDaoImpl(this.context);
        if (new SimpleDateFormat(Global.DATE_FORMAT_2, Locale.CHINA).format(new Date()).equals(PreferenceManager.getDefaultSharedPreferences(this.context).getString(Global.PrefKey.LAST_REFRESH, ""))) {
            List<OrderMarketingProduct> list = mDao.getByCondition("", "");
            List<OrderMarketingProduct> collectList = mDao.getCollections();
            if (list.size() == 0) {
                callback("");
                return;
            }
            DatasetList paramsList = new DatasetList();
            for (int i = 0; i < list.size(); i++) {
                IData paramsMap = new DataMap();
                OrderMarketingProduct product = list.get(i);
                paramsMap.put("ERR_MSG", "");
                paramsMap.put("BRAND_ID", Global.SERVICE_PASSWORD);
                paramsMap.put("PRODUCT_ID", product.getId());
                paramsMap.put("NOTES", "");
                paramsMap.put("PRODUCT_CODE", product.getCode());
                paramsMap.put("PRODUCT_DESC", product.getDesc());
                paramsMap.put("CAN_SEL", "");
                paramsMap.put("type", product.getCategory());
                paramsMap.put("PRODUCT_TYPE", product.getType());
                paramsMap.put("PRODUCT_NAME", product.getName());
                if (collectList.contains(product)) {
                    paramsMap.put("BRAND_ID", "1");
                } else {
                    paramsMap.put("BRAND_ID", Global.SERVICE_PASSWORD);
                }
                paramsList.add(paramsMap);
            }
            callback(paramsList.toString());
            return;
        }
        callback("");
    }

    public void getOrderMarketingCollectionInfo(JSONArray params) throws Exception {
        OrderMarketingDBUtil.ProductDao mDao = new OrderMarketingDBUtil.ProductDaoImpl(this.context);
        if (new SimpleDateFormat(Global.DATE_FORMAT_2, Locale.CHINA).format(new Date()).equals(PreferenceManager.getDefaultSharedPreferences(this.context).getString(Global.PrefKey.LAST_REFRESH, ""))) {
            List<OrderMarketingProduct> list = mDao.getCollections();
            List<OrderMarketingProduct> collectList = mDao.getCollections();
            if (list.size() == 0) {
                callback("");
                return;
            }
            DatasetList paramsList = new DatasetList();
            for (int i = 0; i < list.size(); i++) {
                IData paramsMap = new DataMap();
                OrderMarketingProduct product = list.get(i);
                paramsMap.put("ERR_MSG", "");
                paramsMap.put("BRAND_ID", Global.SERVICE_PASSWORD);
                paramsMap.put("PRODUCT_ID", product.getId());
                paramsMap.put("NOTES", "");
                paramsMap.put("PRODUCT_CODE", product.getCode());
                paramsMap.put("PRODUCT_DESC", product.getDesc());
                paramsMap.put("CAN_SEL", "");
                paramsMap.put("type", product.getCategory());
                paramsMap.put("PRODUCT_TYPE", product.getType());
                paramsMap.put("PRODUCT_NAME", product.getName());
                if (collectList.contains(product)) {
                    paramsMap.put("BRAND_ID", "1");
                } else {
                    paramsMap.put("BRAND_ID", Global.SERVICE_PASSWORD);
                }
                paramsList.add(paramsMap);
            }
            callback(paramsList.toString());
            return;
        }
        callback("");
    }

    public void changeMarketingInfoFlag(JSONArray params) throws Exception {
        String productId = params.getString(0);
        String flag = params.getString(1);
        String type = params.getString(2);
        OrderMarketingProduct product = new OrderMarketingProduct();
        product.setId(productId);
        product.setCategory(type);
        OrderMarketingDBUtil.ProductDao mDao = new OrderMarketingDBUtil.ProductDaoImpl(this.context);
        if ("1".equals(flag)) {
            mDao.collect(product);
        } else if (Global.SERVICE_PASSWORD.equals(flag)) {
            mDao.deleteFromCollections(product);
        }
    }

    public void getLocation(JSONArray params) throws Exception {
      /*  LocService.LocStart(this.context, new IListener() {
            public void OnListener(Object item) {
                Location location = (Location) item;
                double locx = location.getLongitude() + 0.00511d;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("locx", (Object) Double.valueOf(locx));
                jsonObj.put("locy", (Object) Double.valueOf(location.getLatitude() - 0.0015d));
                AppManagePlugin.this.callback(jsonObj.toJSONString());
            }
        });*/
    }

    public void saveParamsToApp(JSONArray jsonAry) throws Exception {
     //   ((App) this.context.getApplication()).getParams().putAll((HashMap) JSONObject.parseObject(jsonAry.getString(0), HashMap.class));
    }

    public void getParamsFromApp(JSONArray jsonAry) throws Exception {
    //    callback(((App) this.context.getApplication()).getParams().get(jsonAry.getString(0)).toString());
    }

    public void isOperatorHasMenuAuthority(JSONArray jsonAry) throws Exception {
       // G3Logger.debug("isOperatorHasMenuAuthority:" + jsonAry.toString());
        System.out.println("AppManagePlugin-isOperatorHasMenuAuthority:0:"+jsonAry.toString());
        boolean result = false;
        App app = (App) this.context.getApplication();
       // String type = app.getAccountChannelType();
        String str = jsonAry.getString(0);
       // G3Logger.debug("str:" + str);
        String menuCode = new DataMap(str).getString("menuCode");
       // String subType = app.getPartnerAccountType();
        /*if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(type)) {
            if (Constant.PartnerChannlRole.PARTNER.getType().equals(subType)) {
                if (com.ai.cmcchina.crm.constant.Constant.PARTNER_APP_ID.equals(menuCode)) {
                    result = true;
                }
            } else if ("G3YYT_11_16_22".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_16_03_24".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_17_03_31".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_11_16_99".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_17_10_17".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_17_12_01".equals(menuCode)) {
                result = true;
            } else if ("目标客户查询".equals(menuCode)) {
                result = true;
            } else if ("G3YYT_17_10_18".equals(menuCode)) {
                result = true;
            } else if (com.ai.cmcchina.crm.constant.Constant.PARTNER_APP_ID.equals(menuCode) && com.wade.mobile.util.Constant.TRUE.equals(PreferenceHelper.get("societyAuthority", ""))) {
                result = true;
            }
        } else if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL.equals(type)) {
            if (StringUtil.isNotBlank(str)) {
                result = ("预警信息".equals(menuCode) || "集团欠费".equals(menuCode)) ? true : (com.ai.cmcchina.crm.constant.Constant.CRM_APP_ID.equals(menuCode) || com.ai.cmcchina.crm.constant.Constant.ESOP_APP_ID.equals(menuCode)) ? true : "G3YYT_17_10_17".equals(menuCode) ? true : "目标客户查询".equals(menuCode) ? true : "G3YYT_17_10_18".equals(menuCode) ? true : isHasMenuAuthority(getOperatorCurrentMenus(), menuCode);
            }
        } else if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_PARTNER_CHANNEL.equals(type)) {
        }*/
        callback(String.valueOf(result));
    }

    private boolean isHasMenuAuthority(com.alibaba.fastjson.JSONArray menus, String menuCode) {
        int size = menus.size();
        for (int i = 0; i < size; i++) {
            String icon = menus.getJSONObject(i).getString(Constant.WadeMobileActivity.ICON);
            if (icon != null && icon.contains(menuCode)) {
                return true;
            }
        }
        return false;
    }

    private com.alibaba.fastjson.JSONArray getOperatorCurrentMenus() {
        return ((App) this.context.getApplication()).getCurrentMenus();
    }

    /* renamed from: com.ai.cmcchina.multiple.func.AppManagePlugin$NativeAppConfig */
    static class NativeAppConfig {
        private String className;
        private String downloadUrl;
        private String packageName;

        public NativeAppConfig(String packageName2, String className2, String downloadUrl2) {
            this.packageName = packageName2;
            this.className = className2;
            this.downloadUrl = downloadUrl2;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packageName2) {
            this.packageName = packageName2;
        }

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String className2) {
            this.className = className2;
        }

        public String getDownloadUrl() {
            return this.downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl2) {
            this.downloadUrl = downloadUrl2;
        }
    }

    public void openRealNameAuthClient(JSONArray params) throws Exception {
        IData info = new DataMap(params.getString(0));
        String billId = info.getString("BILL_ID");
        String OPTRID = info.getString("OPTRID");
        OpenRealNameAuthClientUtil.call10085(this, this.context, info.getString("TRANSACTIONID"), info.getString("SIGNATURE"), info.getString("CHANNELID"), billId, "371", OPTRID, info.getString("nodeCode"));
    }

    private void realNameCallback(Intent data) {
        IData params = new DataMap();
        try {
            boolean result = data.getBooleanExtra("result", false);
            String idCardJson = data.getStringExtra("idCard");
            if (result) {
                if (JSON.parseObject(idCardJson) == null) {
                    params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY, "9999");
                    params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY, "身份证信息错误");
                } else {
                    params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY, "0000");
                    params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY, "完成实名认证");
                    params.put("CardInfo", idCardJson);
                }
                callback(params.toString());
            }
            params.put("resultCode", "9999");
            params.put("resultMsg", "验证失败");
            callback(params.toString());
        } catch (Exception e) {
            params.put("resultCode", "9999");
            params.put("resultMsg", "实名认证失败");
        }
    }

    public void openWindow(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        Intent intent = new Intent(this.context, MultipleCustomWindowActivity.class);
        intent.putExtra("pageAction", pageAction);
        intent.putExtra("data", data);
        startActivityForResult(intent, 200);
    }

    public void httpRequestGet(JSONArray jsonAry) throws JSONException {
        String returnMsg;
        String returnCode;
        String result;
       // G3Logger.debug("httpRequestGet:" + jsonAry.toString());
        IData data = new DataMap(jsonAry.getString(0));
        String svcNum = data.getString("svcNum");
        HashMap<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("Client-Type", getEncryptFlag());
//        hashMap.put("Timestamp", getCurrentTimestamp());
        hashMap.put("Content-Type", "application/x-www-form-urlencoded");
        hashMap.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.1.2; vivo X9Plus Build/N2G47H)");
        hashMap.put("Host", "112.53.127.41:8088");
        hashMap.put("Connection", "Keep-Alive");
        hashMap.put("Accept-Encoding","gzip");
        try {
            result = URLDecoder.decode(HttpTool.httpRequest("http://112.53.127.41:8088/hnmccClientWap/hzs/api/v1.do" + "?mobile=" + svcNum + "&channel=1&regionnum=" + data.getString("regionId"), hashMap, "GET", "UTF-8","AppManagePlugin-httpRequestGet"), "UTF-8");
            returnMsg = "发送请求成功";
            returnCode = "0000";
        } catch (Exception e) {
           // G3Logger.debug(e);
            returnMsg = "发送请求失败，请稍后重试";
            returnCode = "9999";
            result = "";
        }
        IData params = new DataMap();
        params.put("result", result);
        params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY, returnCode);
        params.put(com.ai.cmcchina.crm.constant.Constant.RETURN_MESSAGE_KEY, returnMsg);
        callback(params.toString());
    }

    public void getSimCardSNInfo(JSONArray params) throws Exception {
       /* String readType = new DataMap(params.getString(0)).getString("readType");
        if (this.hardwareConnectHelper == null) {
            this.hardwareConnectHelper = new HardwareConnectHelper(this, this.context);
        }
        this.hardwareConnectHelper.initReadCard(new HardwareConnectHelper.ReadCardCallbackInterface() {
            public void callbackReadCard(Object result) {
                IData params = new DataMap();
                params.put("sn", (String) result);
                AppManagePlugin.this.callback(params.toString());
            }
        });
        this.hardwareConnectHelper.getSimCardSNInfoByType(readType);*/
    }

    public void writeSimCard(JSONArray params) throws Exception {
     /*  // G3Logger.debug("writeSimCard:" + params.toString());
        IData info = new DataMap(params.getString(0));
        String writeCardData = info.getString("writeCardData");
        String writeType = info.getString("writeType");
        if (this.hardwareConnectHelper == null) {
            this.hardwareConnectHelper = new HardwareConnectHelper(this, this.context);
        }
        this.hardwareConnectHelper.initWriteCard(writeCardData, true, new HardwareConnectHelper.WriteCardCallbackInterface() {
            public void callbackWriteCard(Object result) {
                IData params = new DataMap();
                if (Boolean.parseBoolean(result.toString())) {
                    params.put("resultCode", "0000");
                } else {
                    params.put("resultCode", "9999");
                }
                HardwareConnectHelper unused = AppManagePlugin.this.hardwareConnectHelper = null;
                AppManagePlugin.this.callback(params.toString());
            }
        });
        this.hardwareConnectHelper.writeSimCardByType(writeType);*/
    }

    public void openElectronitWorkSheet(JSONArray params) throws Exception {
       /* IData info = new DataMap(params.getString(0));
        Intent intent = new Intent(this.context, ElectronicSignatureActivity.class);
        intent.putExtra("serialNo", info.getString("serialNo"));
        intent.putExtra("telephone", info.getString("telephone"));
        intent.putExtra("name", info.getString("name"));
        intent.putExtra("idCard", info.getString("idCard"));
        intent.putExtra("content", info.getString("content"));
        intent.putExtra("opCode", info.getString("opCode"));
        intent.putExtra("regionId", info.getString("regionId"));
        intent.putExtra("noPaperType", info.getString("noPaperType"));
        intent.putExtra(Constant.MobileWebCacheDB.URL_COLUMN, info.getString(Constant.MobileWebCacheDB.URL_COLUMN));
        intent.putExtra(Consts.C3148J.address, info.getString(Consts.C3148J.address));
        intent.putExtra("noPaperBean", info.getString("noPaperBean"));
        startActivityForResult(intent, BaseFragment.NO_PAPER_SUCC);*/
    }

    public void encryptPassword(JSONArray params) throws Exception {
        callback(EncryptInterface.desEncryptData(JSONObject.parseObject(params.getString(0)).getString("password")));
    }

    public void shareDownloadUrl(JSONArray params) throws Exception {
        JSONObject json = JSONObject.parseObject(params.getString(0));
        String url = json.getString(Constant.MobileWebCacheDB.URL_COLUMN);
        String shareType = json.getString("shareType");
        if (Global.SERVICE_PASSWORD.equals(shareType)) {
            wrapShareInfo(0, url, json);
        } else if ("1".equals(shareType)) {
            wrapShareInfo(1, url, json);
        }
    }

    private void wrapShareInfo(int wxscenesession, String shareUrl, JSONObject json) {
     /*   WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        setTitleAndDesc(msg, shareUrl, json);
        msg.thumbData = bmpToByteArray(BitmapFactory.decodeResource(this.context.getResources(), C0848R.C0850drawable.icon), false);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = wxscenesession;
        IWXAPI api = WXAPIFactory.createWXAPI(this.context, "wx8b169c17a6a29e5e", false);
        api.registerApp("wx8b169c17a6a29e5e");
        api.sendReq(req);*/
    }

   /* private void setTitleAndDesc(WXMediaMessage msg, String sharUrl, JSONObject json) {
        try {
            msg.title = json.getString(C3935a.f11799b);
            msg.description = json.getString("desc");
        } catch (Exception e) {
            msg.title = "和助手下载链接";
        }
    }*/

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        int i;
        int j;
        byte[] arrayOfByte;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), (Paint) null);
            if (needRecycle) {
                bmp.recycle();
            }
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                break;
            } catch (Exception e) {
               // G3Logger.debug(e);
                i = bmp.getHeight();
                j = bmp.getHeight();
            }
        }
        return arrayOfByte;
    }

    private static String buildTransaction(String type) {
        return type == null ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void openURLByWebView(JSONArray params) throws Exception {
        JSONObject json = JSONObject.parseObject(params.getString(0));
        String url = json.getString(Constant.MobileWebCacheDB.URL_COLUMN);
        String title = json.getString("title");
        Intent intent = new Intent(this.context, WebViewActivity.class);
        intent.putExtra(Constant.MobileWebCacheDB.URL_COLUMN, url);
        intent.putExtra("title", title);
        this.context.startActivity(intent);
    }

    public void selectLocalFile(JSONArray params) throws Exception {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        startActivityForResult(intent, FileSelectorUtil.FILE_SELECT_REQUEST);
    }

    public void getPicFromLibrary(JSONArray params) throws Exception {
        Intent intentToPickPic = new Intent("android.intent.action.PICK", (Uri) null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, FileSelectorUtil.FILE_SELECT_IMG_COMPRESS_REQUEST);
    }

    public void upLoadFile(JSONArray params) throws Exception {
        String[] files;
        IData dataMap = new DataMap(params.getString(0));
        Map<String, String> hashMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            hashMap.put(entry.getKey(), (String) entry.getValue());
        }
        String FILE_PATH = dataMap.getString(Constant.DownloadFileActivity.FILE_PATH);
        if (!TextUtils.isEmpty(FILE_PATH)) {
            files = FILE_PATH.split(com.wade.mobile.util.Constant.PARAMS_SQE);
        } else {
            files = new String[0];
        }
        new FileUploadHandler(this.context, hashMap, files, false) {
            public void onSuccess(String result) {
                IData dataMap = new DataMap();
                dataMap.put("resultFlag", true);
                dataMap.put("result", result);
                AppManagePlugin.this.callback(dataMap.toString());
            }

            public void onError(String result) {
                IData dataMap = new DataMap();
                dataMap.put("resultFlag", false);
                dataMap.put("result", result);
                AppManagePlugin.this.callback(dataMap.toString());
            }
        }.start();
    }

    public void takePhotoReturnBase64String(JSONArray jsonAry) throws Exception {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PHOTO);
    }

    public void photosToBase64(JSONArray jsonAry) throws Exception {
        final String photoUrlsString = jsonAry.getString(0);
        new Thread(new Runnable() {
            public void run() {
                String[] photoUrls = new String[0];
                if (photoUrls != null) {
                    photoUrls = photoUrlsString.split(com.wade.mobile.util.Constant.PARAMS_SQE);
                }
                IData dataMap = new DataMap();
                dataMap.put("length", photoUrls.length + "");
                for (int i = 0; i < photoUrls.length; i++) {
                    dataMap.put("photo" + i, ImageUtil.file2EncodeToBase64String(photoUrls[i]).replace("+", "%2B").replace("=", "%3D").replaceAll("[\\s*\t\n\r]", ""));
                }
                AppManagePlugin.this.callback(dataMap.toString());
            }
        }).start();
    }

    public void uploadIdCard(JSONArray jsonAry) throws Exception {
        String photoName = MobileAppInfo.getInstance(this.context).getAppPath() + "-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + ".jpg";
        String photoDir = DirectionUtil.getInstance(this.context).getImageDirection(true);
        File photoDirFile = new File(photoDir);
        if (!photoDirFile.exists()) {
            photoDirFile.mkdirs();
        }
        File photoFile = new File(FileUtil.connectFilePath(photoDir, photoName));
        this.photoFullPath = photoFile.getAbsolutePath();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(photoFile));
       // G3Logger.debug("重写框架的部分代码，压缩到200k>>> photoFullPath = " + this.photoFullPath);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void closeWindow(JSONArray param) throws Exception {
        int resultState = 1;
        String resultData = param.getString(0);
        if (isNull(resultData)) {
            resultData = null;
        }
        if (!isNull(param.getString(1))) {
            resultState = param.getInt(1);
        }
        if (this.context instanceof CustomWindowActivity) {
            ((CustomWindowActivity) this.context).closeWindow(resultData, resultState);
        /*} else if (this.context instanceof OpenBusPageActivity) {
            ((OpenBusPageActivity) this.context).closeWindow(resultData, resultState);*/
        } else {
            HintHelper.alert(this.context, "无窗口可以关闭!");
        }
    }

    public void backManagerMapView(JSONArray param) throws Exception {
        String resultData = param.getString(0);
        if (isNull(resultData)) {
            resultData = null;
        }
        if (this.context instanceof CustomWindowActivity) {
            ((CustomWindowActivity) this.context).closeWindow(resultData, 1);
        /*} else if (this.context instanceof OpenBusPageActivity) {
            ((OpenBusPageActivity) this.context).closeWindow(resultData, 1);*/
        } else {
            HintHelper.alert(this.context, "无窗口可以关闭!");
        }
    }

    public void getPhoto(JSONArray param) throws Exception {
       // G3Logger.debug("重写框架的部分代码，压缩到200k>>>");
        String photoName = MobileAppInfo.getInstance(this.context).getAppPath() + "-" + new SimpleDateFormat("yyyyMMdd-hhmmss").format(new Date()) + ".jpg";
        String photoDir = DirectionUtil.getInstance(this.context).getImageDirection(true);
        File photoDirFile = new File(photoDir);
        if (!photoDirFile.exists()) {
            photoDirFile.mkdirs();
        }
        File photoFile = new File(FileUtil.connectFilePath(photoDir, photoName));
        this.photoFullPath = photoFile.getAbsolutePath();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(photoFile));
       // G3Logger.debug("重写框架的部分代码，压缩到200k>>> photoFullPath = " + this.photoFullPath);
        startActivityForResult(intent, TAKE_PHOTO_200K);
    }

    public void addWaterMark(JSONArray params) throws Exception {
        final String content = params.getString(0);
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    FrameLayout frameLayout = (FrameLayout) ((WadeMobileActivity) AppManagePlugin.this.context).getFlipperLayout().getParent();
                    if (frameLayout.getChildCount() > 1 && (frameLayout.getChildAt(frameLayout.getChildCount() - 1) instanceof View)) {
                        frameLayout.removeViewAt(frameLayout.getChildCount() - 1);
                    }
                  // Watermark.getInstance().show(AppManagePlugin.this.context, frameLayout, content);
                } catch (Exception e) {
                    e.printStackTrace();
                   // G3Logger.debug("添加水印:只能是IPU页面添加(添加View必须在UI线程)报错！！！！");
                   // G3Logger.debug(e);
                }
            }
        });
    }

    public void openApp(JSONArray params) throws Exception {
        String tip;
        IData dataMap = new DataMap(params.getString(0));
        String packageName = dataMap.getString("packageName");
        String className = dataMap.getString("className");
        String downloadUrl = dataMap.getString("downloadUrl");
        String appName = dataMap.getString("appName");
        String scheme = dataMap.getString("scheme-A");
        if (DeviceUtil.checkAppInstalled(this.context, packageName)) {
            if (StringUtil.isNotBlank(scheme)) {
                DeviceUtil.openApp(this.context, scheme, appName);
            } else {
                DeviceUtil.openApp(this.context, packageName, className, appName);
            }
        } else if (StringUtil.isBlank(downloadUrl)) {
            if (StringUtil.isNotBlank(appName)) {
                tip = "应用【" + appName + "】未安装，请先安装应用！";
            } else {
                tip = "应用未安装，请先安装应用！";
            }
            UIUtil.toast((Context) this.context, (Object) tip);
        } else {
            DeviceUtil.openDownLoadURL(this.context, downloadUrl);
        }
    }

    public void checkUpgrade(JSONArray params) throws Exception {
        //Beta.checkUpgrade();
    }
}