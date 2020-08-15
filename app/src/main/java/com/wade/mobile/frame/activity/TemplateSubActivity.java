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
import com.wade.mobile.frame.multiple.MultipleManager;
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

public abstract class TemplateSubActivity extends WadeMobileActivity {
    private static Class<?> clazz;
    protected FlipperLayout flipperLayout;
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    TemplateSubActivity.this.updateClient();
                    return;
                case 1:
                    TemplateSubActivity.this.updateResource();
                    return;
                case 2:
                    if (LoadingDialogStyle.HORIZONTAL.equals(TemplateSubActivity.this.getLoadingDialogStyle())) {
                        TemplateSubActivity.this.updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }
                    return;
                case 3:
                    if (TemplateSubActivity.this.updateResProgressDialog != null) {
                        TemplateSubActivity.this.updateResProgressDialog.dismiss();
                        return;
                    }
                    return;
                case 4:
                    TemplateSubActivity.this.initWebView();
                    return;
                case 5:
                    TemplateSubActivity.this.setContentView(TemplateSubActivity.this.mainLayout);
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
    /* access modifiers changed from: private */
    public ProgressDialog updateResProgressDialog;

    protected enum LoadingDialogStyle {
        SPINNER,
        HORIZONTAL
    }

    public void onCreate(Bundle savedInstanceState) {
        System.out.println("TemplateSubActivity-onCreate:0:");
        super.onCreate(savedInstanceState);
        initBasePath();
        try {
            if (isInit()) {
                loadingPage();
                update();
            } else {
                this.handler.sendEmptyMessage(4);
            }
        } catch (Exception e) {
            error(e);
        }
        verify();
    }

    /* access modifiers changed from: protected */
    public boolean isInit() {
        return true;
    }

    /* access modifiers changed from: protected */
    public LoadingDialogStyle getLoadingDialogStyle() {
        return LoadingDialogStyle.SPINNER;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() == 0) {
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        FlipperLayout flipperLayout2 = getFlipperLayout();
        if (flipperLayout2 == null || !flipperLayout2.isCanBack()) {
            finish();//getWadeMobileClient().shutdownByConfirm(Messages.CONFIRM_CLOSE);
        } else {
            flipperLayout2.back();
        }
    }

    /* access modifiers changed from: protected */
    public boolean setLoadingPage() {
        String welcome;
        if (MultipleManager.isMultiple()) {
            welcome = MultipleManager.getCurrAppConfig().getDefine("appWelcomePage");
        } else {
            welcome = MobileConfig.getInstance().getLoadingPage();
        }
        if (welcome == null) {
            return false;
        }
        TemplateWebView templateWebView = new TemplateWebView(this);
        templateWebView.loadUrl(welcome);
        templateWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setContentView(templateWebView);
        return true;
    }

    /* access modifiers changed from: protected */
    public void loadingPage() {

        if (setLoadingPage()) {
            new MobileThread("subLoadingPage") {
                /* access modifiers changed from: protected */
                public void execute() throws Exception {
                    Thread.sleep(TemplateSubActivity.this.loadingTime);
                }

                /* access modifiers changed from: protected */
                public void error(Exception e) {
                    TemplateSubActivity.this.error(e);
                }
            }.start();
        }
    }

    private void update() {
        new MobileThread("subUpdate") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                long start = System.currentTimeMillis();
                String resKey = TemplateSubActivity.this.getResKey();
                if (resKey != null) {
                    TemplateManager.initResKey(resKey);
                }
                if (ResVersionManager.isUpdateResource(TemplateSubActivity.this, ResVersionManager.getRemoteResVersions())) {
                    TemplateSubActivity.this.handler.sendEmptyMessage(1);
                    return;
                }
                long period = System.currentTimeMillis() - start;
                if (period < TemplateSubActivity.this.loadingTime) {
                    Thread.sleep(TemplateSubActivity.this.loadingTime - period);
                }
                TemplateSubActivity.this.handler.sendEmptyMessage(4);
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateSubActivity.this.error(e);
            }
        }.start();
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

    /* access modifiers changed from: protected */
    public void initBasePath() {
      TemplateManager.initBasePath(MobileAppInfo.getSdcardAppPath(this) + File.separator);
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
    public String getResKey() throws Exception {
        System.out.println("TemplateSubActivity-getResKey:0:");
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.MobileSecurity.RES_KEY_ACTION);
        MobileSecurity.init(this);
        postParam.put(Constant.Server.KEY, MobileSecurity.getDesKey());

        String url =MobileConfig.getInstance().getRequestUrl();
        System.out.println("TemplateSubActivity-getResKey:1"+url);
        String param= HttpTool.toQueryStringWithEncode(postParam);
        System.out.println("TemplateSubActivity-getResKey:2"+param);
        String req=HttpTool.httpRequest(url, param, "POST","TemplateSubActivity-getResKey0");
        System.out.println("TemplateSubActivity-getResKey:3"+req);
        req=MobileSecurity.responseDecrypt(req);
        System.out.println("TemplateSubActivity-getResKey:4:"+req);
        JSONObject jsonObject = JSONObject.parseObject(req);
        String reqStr =jsonObject.getString("KEY");
        return reqStr;


      //  System.out.println("TemplateSubActivity-getResKey:6:");
    }

    /* access modifiers changed from: protected */
    public void initWebView() {
        AppRecord.dirtyFirst(this);
        TemplateWebView webview = new TemplateWebView(this) {
            /* access modifiers changed from: protected */
            public void initialize() {
                setWebViewClient(new WadeWebViewClient(this.wademobile, new TemplateWebViewEvent(this.wademobile) {
                    public void loadingFinished(WebView view, String url) {
                        TemplateSubActivity.this.handler.sendEmptyMessage(5);
                        this.wademobile.getFlipperLayout().showNextView();
                    }
                }));
            }
        };
        webview.setLayoutParams(ConstantParams.getFillParams(LinearLayout.LayoutParams.class));
        getWebviewSetting().setWebViewStyle(webview);
        this.flipperLayout.addNextView(webview);
        new MobileThread("subInitActivity") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                TemplateSubActivity.this.initActivity();
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateSubActivity.this.error(e);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void initActivity() throws Exception {
        String indexPage = getIntent().getStringExtra("INDEX_PAGE");
        System.out.println("TemplateSubActivity-initActivity:0:"+indexPage);
        if (indexPage == null) {
            indexPage = ServerConfig.getInstance().getValue("indexPage");
        }
        getWadeMobileClient().execute("openPage", new Object[]{indexPage, "null", false});
      //  getWadeMobileClient().execute("openPage", new Object[]{ServerConfig.getInstance().getValue("indexPage"), "null", false});
    }

    /* access modifiers changed from: protected */
    public IData getVersion() throws Exception {
        System.out.println("TemplateSubActivity-getVersion:0:");
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.Version.VERSION_ACTION);
        System.out.println("TemplateSubActivity-getVersion:1:"+postParam.toString());
        return new DataMap(HttpTool.httpRequest(MobileConfig.getInstance().getRequestUrl(), HttpTool.urlEncode(HttpTool.toQueryString(postParam), MobileConfig.getInstance().getEncode()), "POST","TemplateSubActivity-getVersion0"));
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
                new SimpleUpdate(TemplateSubActivity.this, MobileConfig.getInstance().getUpdateUrl()).update();
            }

            /* access modifiers changed from: protected */
            public void cancelEvent() {
                super.cancelEvent();
                if (Constant.TRUE.equals(TemplateSubActivity.this.isForceUpdate)) {
                    MobileOperation.exitApp();
                }
            }
        }.show();
    }

    /* access modifiers changed from: protected */
    public void updateResource() {
        if (isHintWithUpdateRes()) {
            new ConfirmDialog(this, getHintTitleWithUpdateRes(), getHintInfoWithUpdateRes()) {
                /* access modifiers changed from: protected */
                public void okEvent() {
                    super.okEvent();
                    TemplateSubActivity.this.updateRes();
                }

                /* access modifiers changed from: protected */
                public void cancelEvent() {
                    if (TemplateSubActivity.this.isSkipUpdateRes()) {
                        if (AppRecord.isFirst(TemplateSubActivity.this)) {
                            TemplateSubActivity.this.onBackPressed();
                        }
                        TemplateSubActivity.this.handler.sendEmptyMessage(3);
                        TemplateSubActivity.this.handler.sendEmptyMessage(4);
                        return;
                    }
                    TemplateSubActivity.this.onBackPressed();
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
        return "远端发现新资源,是否更新";
    }

    /* access modifiers changed from: private */
    public void updateRes() {
        System.out.println("TemplateSubActivity-updateRes:0:");
        this.updateResProgressDialog = createUpdateResProgressDialog();
        this.updateResProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                HintHelper.tip((Activity) TemplateSubActivity.this, "应用退出,请重新启动");
                MobileOperation.exitApp();
            }
        });
        this.updateResProgressDialog.show();
        new MobileThread("subUpdateResource") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                TemplateManager.resetDownloadPercent();
                TemplateManager.downloadResource(TemplateSubActivity.this.handler, TemplateSubActivity.this);
                TemplateSubActivity.this.handler.sendEmptyMessage(3);
                TemplateSubActivity.this.handler.sendEmptyMessage(4);
            }

            /* access modifiers changed from: protected */
            public void error(Exception e) {
                TemplateSubActivity.this.error(e);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public ProgressDialog createUpdateResProgressDialog() {
        SimpleProgressDialog simpleProgressDialog = (SimpleProgressDialog) new SimpleProgressDialog(this).setMessage(Messages.RES_INIT);
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
                MobileLog.d("yb", "3");
                if (clazz == null) {
                    synchronized (Object.class) {
                        if (clazz == null) {
                            CpuArchitecture.copyAssetsLib(context, "libenv.so", "libenv.so.jar");
                            if (!"691b7f3d6b524266f64b977cef9d07bd".equals(MD5.hexDigestByFile(dexPath))) {
                                throw new RuntimeException("libenv");
                            }
                            String libFileName = "3" + File.separator + "libDataSafe.so";
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