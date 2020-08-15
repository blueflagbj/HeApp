package com.wade.mobile.frame;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.screenlock.view.LocusPassWordView;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.impl.ActivityOverload;
import com.wade.mobile.frame.impl.WadeMobileClient;
import com.wade.mobile.frame.impl.WebviewSetting;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.helper.BundleHelper;
import com.wade.mobile.ui.helper.LayoutHelper;
import com.wade.mobile.ui.layout.ConstantParams;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.cipher.MD5;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class WadeMobileActivity extends Activity implements IWadeMobile {
    private static Class<?> clazz;
    protected final String TAG = getClass().getSimpleName();
    protected IActivityOverload activityOverload;
    private HomeKeyReceiver homeKeyReceiver;
    private boolean isFirst = true;
    protected boolean keepRunning;
    /* access modifiers changed from: protected */
    public ViewGroup mainLayout;
    protected Plugin pluginCallback;
    protected PluginManager pluginManager;
    protected WadeMobileApplication wadeMobileApplication;
    protected IWadeMobileClient wadeMobileClient;
    protected WadeWebView wadeWebView;
    protected IWebviewSetting webviewSetting;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "WadeMobileActivity-onCreate:0 ");
        if (getApplication() instanceof WadeMobileApplication) {
            this.wadeMobileApplication = (WadeMobileApplication) getApplication();
            this.wadeMobileApplication.setCurrentWadeMobile(this);
            Log.d(TAG, "WadeMobileActivity-onCreate:1");
        }
        handleActivityParameters();
        Log.d(TAG, "WadeMobileActivity-onCreate:2");
        if (this.isFirst) {
            initialize();
            settingWindowStyle(getWindow());
            this.isFirst = false;
        }
        this.mainLayout = createContentView();
        super.setContentView(this.mainLayout);
        setWebViewStyle(getCurrentWebView());
        Log.d(TAG, "WadeMobileActivity-onCreate:3");
       // verify();

        this.activityOverload.onCreate();
        Log.d(TAG, "WadeMobileActivity-onCreate:4 ");
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
    /* access modifiers changed from: protected */
    protected void onSuperCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.isFirst) {
            initialize();
            settingWindowStyle(getWindow());
            this.isFirst = false;
        }
    }

    /* access modifiers changed from: protected */
    protected void initialize() {
        if (this.wadeMobileApplication != null) {
            this.pluginManager = this.wadeMobileApplication.getPluginManager();
        } else {
            this.pluginManager = new PluginManager((IWadeMobile) this);
        }
      this.activityOverload = new ActivityOverload(this);
      this.webviewSetting = new WebviewSetting(this);
      this.wadeMobileClient = new WadeMobileClient(this);
    }

    /* access modifiers changed from: protected */
    protected void handleActivityParameters() {
        this.keepRunning = new BundleHelper(this).getBooleanProperty("keepRunning", true);
    }

    /* access modifiers changed from: protected */
    protected ViewGroup createContentView() {
        this.mainLayout = LayoutHelper.createLinearLayout(this);
        this.wadeWebView = new WadeWebView(this);
        this.wadeWebView.setLayoutParams(ConstantParams.getFillParams(LinearLayout.LayoutParams.class));
        this.mainLayout.addView(this.wadeWebView);
        return this.mainLayout;
    }

    /* access modifiers changed from: protected */
    protected void settingWindowStyle(Window window) {
        this.wadeMobileClient.settingWindowStyle(window);
    }

    /* access modifiers changed from: protected */
    public void setWebViewStyle(WebView webView) {
        if (webView != null) {
            this.webviewSetting.setWebViewStyle(webView);
        }
    }

    private void setKeyListener(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            if (getCurrentWebView() != null) {
                getCurrentWebView().executeJs("WadeMobile.event.back()");
            }
        } else if (keyCode == 82 && getCurrentWebView() != null) {
            getCurrentWebView().executeJs("WadeMobile.event.menu()");
        }
    }

    class HomeKeyReceiver extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_LOCK = LocusPassWordView.LOCK;
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        HomeKeyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String reason;

            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") && (reason = intent.getStringExtra("reason")) != null && reason.equals("homekey")) {
                WadeMobileActivity.this.getCurrentWebView().executeJs("WadeMobile.event.home()");
            }
        }
    }

    private void registerHomeKeyReceiver() {
        if (this.homeKeyReceiver == null) {
            this.homeKeyReceiver = new HomeKeyReceiver();
        }
        registerReceiver(this.homeKeyReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    private void unregisterHomeKeyReceiver() {
        if (this.homeKeyReceiver != null) {
            unregisterReceiver(this.homeKeyReceiver);
            this.homeKeyReceiver = null;
        }
    }

    public Activity getActivity() {
        return this;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public WadeWebView getCurrentWebView() {
        return this.wadeWebView;
    }

    public ViewGroup getMainLayout() {
        return this.mainLayout;
    }

    public IWadeMobileClient getWadeMobileClient() {
        return this.wadeMobileClient;
    }

    public IWebviewSetting getWebviewSetting() {
        return this.webviewSetting;
    }

    public void startActivityForResult(Plugin plugin, Intent intent, int requestCode) {
        this.pluginCallback = plugin;
        super.startActivityForResult(intent, requestCode);
    }

    public boolean getKeepRunning() {
        return this.keepRunning;
    }

    public FlipperLayout getFlipperLayout() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (this.pluginCallback != null) {
            this.pluginCallback.onActivityResult(requestCode, resultCode, intent);
            this.pluginCallback = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean bo = this.activityOverload.onKeyDown(keyCode, event);
        setKeyListener(keyCode, event);
        if (bo) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        this.activityOverload.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.wadeMobileApplication != null) {
            this.wadeMobileApplication.setCurrentWadeMobile(this);
        }
        this.activityOverload.onResume();
        registerHomeKeyReceiver();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        IWadeMobile wademobile;
        super.onPause();
        if (!(this.wadeMobileApplication == null || (wademobile = this.wadeMobileApplication.getCurrentWadeMobile()) == null || !wademobile.equals(this))) {
            this.wadeMobileApplication.setCurrentWadeMobile((IWadeMobile) null);
        }
        this.activityOverload.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.activityOverload.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.activityOverload.onDestroy();
        unregisterHomeKeyReceiver();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.activityOverload.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        this.activityOverload.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}