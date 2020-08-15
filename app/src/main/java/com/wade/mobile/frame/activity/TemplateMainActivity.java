package com.wade.mobile.frame.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.alibaba.fastjson.JSONObject;
import com.wade.mobile.app.AppRecord;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.app.MobileOperation;
import com.wade.mobile.app.SimpleUpdate;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.frame.WadeMobileActivity;
import com.wade.mobile.frame.WadeWebView;
import com.wade.mobile.frame.client.WadeWebViewClient;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.event.impl.TemplateWebViewEvent;
import com.wade.mobile.frame.template.ResVersionManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.frame.template.TemplateWebView;
import com.wade.mobile.ui.build.dialog.progressdialog.SimpleProgressDialog;
import com.wade.mobile.ui.comp.dialog.ConfirmDialog;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.ui.helper.LayoutHelper;
import com.wade.mobile.ui.layout.ConstantParams;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.assets.AssetsUtil;
import com.wade.mobile.util.cipher.MD5;
import com.wade.mobile.util.http.HttpTool;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class TemplateMainActivity extends WadeMobileActivity {
    private static Class<?> clazz;
    protected FlipperLayout flipperLayout;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TemplateMainActivity.this.updateClient();
                    return;
                case 1:
                    TemplateMainActivity.this.updateResource();
                    return;
                case 2:
                    if (LoadingDialogStyle.HORIZONTAL.equals(TemplateMainActivity.this.getLoadingDialogStyle())) {
                        TemplateMainActivity.this.updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }
                    return;
                case 3:
                    if (TemplateMainActivity.this.updateResProgressDialog != null) {
                        TemplateMainActivity.this.updateResProgressDialog.dismiss();
                        return;
                    }
                    return;
                case 4:
                    try {
                        TemplateMainActivity.this.initWebview();
                        return;
                    } catch (Exception e) {
                        TemplateMainActivity.this.error(e);
                        return;
                    }
                case 5:
                    TemplateMainActivity.this.setContentView(TemplateMainActivity.this.mainLayout);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String isForceUpdate;
    /* access modifiers changed from: private */
    public long loadingTime = Long.parseLong(MobileConfig.getInstance().getConfigValue("loading_time", "2000"));
    private String resourceVersion;
    /* access modifiers changed from: private */
    public ProgressDialog updateResProgressDialog;

    protected enum LoadingDialogStyle {
        SPINNER,
        HORIZONTAL
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBasePath();
        try {
            loadingPage();
            update();
        } catch (Exception e) {
            error(e);
        }
        Log.d(TAG, "TemplateMainActivity-onCreate: 0");
       // verify();
    }

    /* access modifiers changed from: protected */
    public LoadingDialogStyle getLoadingDialogStyle() {
        return LoadingDialogStyle.HORIZONTAL;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() == 0) {
        }
        return super.onKeyDown(keyCode, event);
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
    public boolean setLoadingPage() {
        String welcome = MobileConfig.getInstance().getLoadingPage();
        System.out.println("TemplateMainActivity-setLoadingPage:0:"+welcome);
        if (welcome == null) {
            return false;
        }
        WadeWebView templateWebView = new WadeWebView(this);
        templateWebView.loadUrl(welcome);
        templateWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setContentView(templateWebView);
        return true;
    }

    /* access modifiers changed from: protected */
    public void loadingPage() {
        if (setLoadingPage()) {
            Log.d(TAG, "loadingPage: 0");
            new MobileThread("LoadingPage") {
                /* access modifiers changed from: protected */
                
                public void execute() throws Exception {
                    Thread.sleep(TemplateMainActivity.this.loadingTime);
                }

                /* access modifiers changed from: protected */
                public void error(Exception e) {
                    TemplateMainActivity.this.error(e);
                }
            }.start();
        }
    }

    private void update() throws Exception {
        new MobileThread("Update") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                long start = System.currentTimeMillis();
                String versions = TemplateMainActivity.this.getVersion();//
                JSONObject jsonObject = JSONObject.parseObject(versions);
                if (TemplateMainActivity.this.isUpdateClient(jsonObject.getString(Constant.ServerConfig.CLIENT_VERSION))) {
                    String unused = TemplateMainActivity.this.isForceUpdate = jsonObject.getString(Constant.ServerConfig.IS_FORCE_UPDATE);
                    TemplateMainActivity.this.handler.sendEmptyMessage(0);
                    return;
                }
                System.out.println("0001");
                if (AppRecord.isFirst(TemplateMainActivity.this)) {
                    try {
                        AssetsUtil.copyAssetsDir(TemplateMainActivity.this, MobileAppInfo.getInstance(TemplateMainActivity.this).getAppPath(), MobileAppInfo.getSdcardAppPath(TemplateMainActivity.this));
                        AppRecord.dirtyFirst(TemplateMainActivity.this);
                    } catch (Exception e) {
                        error(e);
                    }
                }
                System.out.println("0002");
                String resKey = TemplateMainActivity.this.getResKey();
                System.out.println("0003:"+resKey);
                if (resKey != null) {
                    TemplateManager.initResKey(resKey);
                }
                System.out.println("0004"+resKey);
                Map<String, ?> remoteResVersions = ResVersionManager.getRemoteResVersions();
                System.out.println("0005"+remoteResVersions.toString());
                if (ResVersionManager.isUpdateResource(TemplateMainActivity.this, remoteResVersions)) {
                    TemplateMainActivity.this.handler.sendEmptyMessage(1);//升级资源
                    return;
                }


                long period = System.currentTimeMillis() - start;
                if (period < TemplateMainActivity.this.loadingTime) {
                    Thread.sleep(TemplateMainActivity.this.loadingTime - period);
                }
                TemplateMainActivity.this.handler.sendEmptyMessage(4);
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateMainActivity.this.error(e);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public ViewGroup createContentView() {
        System.out.println("createContentView0001");
        this.mainLayout = LayoutHelper.createFrameLayout(this);
        View mainView = createMainView();
        this.flipperLayout = (FlipperLayout) mainView;
        this.mainLayout.addView(mainView);
        return this.mainLayout;
    }

    /* access modifiers changed from: protected */
    public View createMainView() {
        System.out.println("createMainView0001");
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

    /* access modifiers changed from: protected */
    public void initBasePath() {
        String basePath =MobileAppInfo.getSdcardAppPath(this)+ File.separator;
        System.out.println("initBasePath0001"+basePath);
        TemplateManager.initBasePath(basePath);
    }

    /* access modifiers changed from: protected */
    public void error(Exception e) {
        this.handler.sendEmptyMessage(3);
        if (e instanceof SocketTimeoutException) {
            getWadeMobileClient().alert(Messages.CONN_SERVER_FAILED, Constant.Function.close, new Object[]{false});
            return;
        }
        HintHelper.alert(this, "启动错误:" + e.getMessage());
        MobileLog.e(this.TAG, e.getMessage(), e);
    }


    /* access modifiers changed from: private */
    public void initWebview() {
        System.out.println("initWebview0001");
        TemplateWebView webview = new TemplateWebView(this) {
            /* access modifiers changed from: protected */
            public void initialize() {
                setWebViewClient(new WadeWebViewClient(this.wademobile, new TemplateWebViewEvent(this.wademobile) {
                    public void loadingFinished(WebView view, String url) {
                        TemplateMainActivity.this.handler.sendEmptyMessage(5);
                        this.wademobile.getFlipperLayout().showNextView();
                    }
                }));
            }
        };
        webview.setLayoutParams(ConstantParams.getFillParams(LinearLayout.LayoutParams.class));
        getWebviewSetting().setWebViewStyle(webview);
        this.flipperLayout.addNextView(webview);
        new MobileThread("initActivity") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                TemplateMainActivity.this.initActivity();
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateMainActivity.this.error(e);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        Log.d(TAG, "initActivity: 0");
        getWadeMobileClient().execute("openPage", new Object[]{ServerConfig.getInstance().getValue("indexPage"), "null", false});
        System.out.println("initActivity0002");
    }

    /* access modifiers changed from: protected */
    public String getVersion() throws Exception {
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.Version.VERSION_ACTION);
        String url =MobileConfig.getInstance().getRequestUrl();
       // System.out.println("TemplateMainActivity-getVersion:"+postParam.toString());
        String param=HttpTool.urlEncode(HttpTool.toQueryString(postParam), MobileConfig.getInstance().getEncode());

        String reqStr = HttpTool.httpRequest(url,param, "POST","TemplateMainActivity-getVersion0");
        return reqStr;
    }
    /* access modifiers changed from: protected */
    public String getResKey() throws Exception {
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.MobileSecurity.RES_KEY_ACTION);
        MobileSecurity.init(this);
        postParam.put(Constant.Server.KEY, MobileSecurity.getDesKey());

        String url =MobileConfig.getInstance().getRequestUrl();
      //  System.out.println("TemplateMainActivity-getResKey:1"+url);
        String param= HttpTool.toQueryStringWithEncode(postParam);
      //  System.out.println("TemplateMainActivity-getResKey:2"+param);
        String req=HttpTool.httpRequest(url, param, "POST","TemplateMainActivity-getResKey");
       // System.out.println("TemplateMainActivity-getResKey:3"+req);
         req=MobileSecurity.responseDecrypt(req);
      //  System.out.println("TemplateMainActivity-getResKey:4:"+req);
        JSONObject jsonObject = JSONObject.parseObject(req);
        String reqStr =jsonObject.getString("KEY");
        return reqStr;

    }

    /* access modifiers changed from: protected */
    public boolean isUpdateClient(String clientVersion) throws PackageManager.NameNotFoundException {
        return !clientVersion.equals(MobileAppInfo.getInstance(this).getVersionName());
    }

    /* access modifiers changed from: protected */
    public boolean isSkipUpdateRes() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void updateClient() {
        new ConfirmDialog(this, "版本更新", "客户端发现新版本,是否更新") {
            /* access modifiers changed from: protected */
            public void okEvent() {
                super.okEvent();
                new SimpleUpdate(TemplateMainActivity.this, MobileConfig.getInstance().getUpdateUrl()).update();
            }

            /* access modifiers changed from: protected */
            public void cancelEvent() {
                super.cancelEvent();
                if (Constant.TRUE.equals(TemplateMainActivity.this.isForceUpdate)) {
                    MobileOperation.exitApp();
                }
            }
        }.show();
    }

    /* access modifiers changed from: protected */
    public void updateResource() {
        if (isHintWithUpdateRes()) {
            new ConfirmDialog(this, "资源更新", "远端发现新资源,是否更新") {
                /* access modifiers changed from: protected */
                public void okEvent() {
                    super.okEvent();
                    TemplateMainActivity.this.updateRes();
                }

                /* access modifiers changed from: protected */
                public void cancelEvent() {
                    super.cancelEvent();
                    if (TemplateMainActivity.this.isSkipUpdateRes()) {
                        if (AppRecord.isFirst(TemplateMainActivity.this)) {
                            MobileOperation.exitApp();
                        }
                        TemplateMainActivity.this.handler.sendEmptyMessage(3);
                        TemplateMainActivity.this.handler.sendEmptyMessage(4);
                        return;
                    }
                    MobileOperation.exitApp();
                }
            }.show();
        } else {
            updateRes();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isHintWithUpdateRes() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getHintTitleWithUpdateRes() {
        return "资源更新";
    }

    /* access modifiers changed from: protected */
    public String getHintInfoWithUpdateRes() {
        return "远端发现新资源，是否更新";
    }

    /* access modifiers changed from: protected */
    public String getProgressTitleUpdateRes() {
        return Messages.RES_INIT;
    }

    /* access modifiers changed from: private */
    public void updateRes() {
        this.updateResProgressDialog = createUpdateResProgressDialog();
        this.updateResProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                HintHelper.tip((Activity) TemplateMainActivity.this, "应用退出,请重新启动");
                MobileOperation.exitApp();
            }
        });
        this.updateResProgressDialog.show();
        new MobileThread("updateResource") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                TemplateManager.resetDownloadPercent();
                TemplateManager.downloadResource(TemplateMainActivity.this.handler, TemplateMainActivity.this);
                TemplateMainActivity.this.handler.sendEmptyMessage(3);
                TemplateMainActivity.this.handler.sendEmptyMessage(4);
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateMainActivity.this.error(e);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public ProgressDialog createUpdateResProgressDialog() {
        SimpleProgressDialog simpleProgressDialog = (SimpleProgressDialog) new SimpleProgressDialog(this).setMessage(getProgressTitleUpdateRes());
        if (LoadingDialogStyle.HORIZONTAL.equals(getLoadingDialogStyle())) {
            simpleProgressDialog.setProgressStyle(1);
            simpleProgressDialog.getProgressDialog().setMax(ResVersionManager.updateCount);
            simpleProgressDialog.getProgressDialog().getWindow().setGravity(17);
        }
        return simpleProgressDialog.build();
    }

    private void verify() {
        try {
            if (System.currentTimeMillis() % 19 == 0) {
                Context context = getApplicationContext();
                File baseOutPath = new File(context.getFilesDir(), CpuArchitecture.LIBS);
                File dexPath = new File(baseOutPath, "libenv.so.jar");
                MobileLog.d("yb", "2");
                if (clazz == null) {
                    synchronized (Object.class) {
                        if (clazz == null) {
                            CpuArchitecture.copyAssetsLib(context, "libenv.so", "libenv.so.jar");
                            if (!"691b7f3d6b524266f64b977cef9d07bd".equals(MD5.hexDigestByFile(dexPath))) {
                                throw new RuntimeException("libenv");
                            }
                            String libFileName = "2" + File.separator + "libDataSafe.so";
                            File file = new File(baseOutPath, libFileName);
                            String dexOutputPath = baseOutPath.getAbsolutePath();
                            CpuArchitecture.copyAssetsLib(context, "libDataSafe.so", libFileName);
                            String[] md5 = {"7b587b512c39e9b4378645a228771e21", "603a68b849920a17c1f9d137a4cb8881", "741c2ed28ee04f2bd2e6afec296378ad", "3b0677ae21eeb28fa1e6f7324d404d75", "a23ff5a419bf07e316808a131d4f5329", "a54ae2a3e9ecbff3a43e0d4c6531d996", "53e2e8898327628c404e93b084d1ed6f"};
                            String clientMd5 = MD5.hexDigestByFile(file);
                            if (md5[0].equals(clientMd5) || md5[1].equals(clientMd5) || md5[2].equals(clientMd5) || md5[3].equals(clientMd5) || md5[4].equals(clientMd5) || md5[5].equals(clientMd5) || md5[6].equals(clientMd5)) {
                                clazz = new DexClassLoader(dexPath.getAbsolutePath(), dexOutputPath, file.getParent(), context.getClassLoader()).loadClass("com.wade.mobile.safe.DataSafe");
                            } else {
                                throw new RuntimeException("libFile");
                            }
                        }
                    }
                }
                Object newInstance = clazz.newInstance();
                Method method = clazz.getMethod("decodeLicense", new Class[]{String.class, String.class});
                String[] k = MobileConfig.getInstance().getConfigValue("license").split("@@");
                String[] licenseData = method.invoke(newInstance, new Object[]{k[0], k[1]}).toString().split("@@");
                String appName = licenseData[0];
                String packageName = licenseData[1];
                Date date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(licenseData[2]);
                String clientAppName = context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
                String clientPackageName = context.getPackageName();
                Date clientDate = new Date();
                if (!appName.equals(clientAppName) || !packageName.equals(clientPackageName) || !clientDate.before(date)) {
                    MobileLog.e("LicenseVerifyError", "Permissions overtime, please re-authorization! ! ! !");
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            MobileLog.e("LicenseVerifyError", "LicenseVerifyErrorException! ! ! !");
            e.printStackTrace();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }
}