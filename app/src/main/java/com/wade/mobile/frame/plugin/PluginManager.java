package com.wade.mobile.frame.plugin;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.webkit.JavascriptInterface;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeMobileApplication;
import com.wade.mobile.frame.config.MobileActionConfig;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.MobileRefleck;
import com.wade.mobile.util.Utility;
import com.wade.mobile.util.cipher.MD5;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;

public class PluginManager {
    private static Class<?> clazz;
    private final String TAG = PluginManager.class.getSimpleName();
    private Handler handler;
    private final HashMap<String, Method> methods = new HashMap<>();
    private final HashMap<Class, Plugin> plugins = new HashMap<>();
    HandlerThread thread = new HandlerThread("PluginManager-Sub-HandlerThread");
    private IWadeMobile wademobile;
    private WadeMobileApplication wmApplication;

    public PluginManager(IWadeMobile wademobile2) {
        this.thread.start();
        this.handler = new Handler(this.thread.getLooper());
        this.wademobile = wademobile2;
    }

    public PluginManager(WadeMobileApplication wmApplication2) {
        this.wmApplication = wmApplication2;
    }

    /* access modifiers changed from: private */
    public Activity getContext() {
        if (this.wmApplication != null) {
            this.wademobile = this.wmApplication.getCurrentWadeMobile();
        }
        return this.wademobile.getActivity();
    }

    @JavascriptInterface
    public void exec(final String action, final String callback, final String params) {
        this.handler.post(new Runnable() {
            public void run() {
                try {
                    System.out.println("PluginManager-exec:0:"+params);
                    PluginManager.this.execute(action, callback, new JSONArray(params));
                } catch (Exception e) {
                    PluginManager.this.execute(Constant.Function.loadingStop, (JSONArray) null);
                    final String error = Utility.getBottomMessage(e);
                    PluginManager.this.getContext().runOnUiThread(new Runnable() {
                        public void run() {
                            PluginManager.this.error(callback, error);
                        }
                    });
                }
            }
        });
    }

    public void execute(String action, JSONArray params) {
        try {
            execute(action, (String) null, params);
        } catch (Exception e) {
            execute(Constant.Function.loadingStop, (JSONArray) null);
            HintHelper.alert(getContext(), e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void execute(String action, String callback, JSONArray params) throws Exception {
        try {
            System.out.println("PluginManagerExecute0001"+params.toString());
            System.out.println("PluginManagerExecute0002"+action);
            System.out.println("PluginManagerExecute0003"+callback);
            String className = MobileActionConfig.getActionClass(action.toUpperCase());
            System.out.println("PluginManagerExecute0004"+className);
            if (className == null) {
                Utility.error(action + Messages.ACTION_NOT_SPECIFY);
            }
            Class clazz2 = Class.forName(className);
            Plugin plugin = getPlugin(clazz2);
            plugin.setCallback(callback);
            String methodName = MobileActionConfig.getActionMethod(action.toUpperCase());
            System.out.println("PluginManagerExecute0005"+methodName);
            Method method = getMethod(clazz2, methodName);
            System.out.println("PluginManagerExecute0005-1"+methodName);
            if (method == null) {
                Utility.error(methodName + Messages.ACTION_NOT_SPECIFY);
            }
            synchronized (method) {
                System.out.println("PluginManagerExecute0005-2："+method.getName());
                method.invoke(plugin, new Object[]{params});
            }
           // verify(getContext());
            System.out.println("PluginManagerExecute0006");
        } catch (Exception e) {
            MobileLog.e(this.TAG, e.getMessage(), e);
            throw e;
        }
    }

    public <Type extends Plugin> Type getPlugin(Class<Type> clazz2) throws Exception {
        Type plugin;
        if (!Plugin.class.isAssignableFrom(clazz2)) {
            Utility.error(clazz2.getSimpleName() + " is need to inherit Plugin");
        }
        Type plugin2 = (Type) this.plugins.get(clazz2);
        if (plugin2 != null) {
            if (this.wmApplication != null) {
                IWadeMobile wademobile2 = this.wmApplication.getCurrentWadeMobile();
                plugin2.setWademobile(wademobile2);
                plugin2.setContext(wademobile2.getActivity());
            }
            Type type = plugin2;
            return plugin2;
        }
        synchronized (this.plugins) {
            plugin = (Type) clazz2.getConstructor(new Class[]{IWadeMobile.class}).newInstance(new Object[]{getContext()});
            this.plugins.put(clazz2, plugin);
        }
        Type type2 = plugin;
        return plugin;
    }

    private Method getMethod(Class clazz2, String methodName) throws Exception {
        Method method;
        String methodKey = clazz2.getName() + methodName;
        System.out.println("PluginManager-getMethod:0:"+methodKey);
        Method method2 = this.methods.get(methodKey);
        if (method2 != null) {
            Method method3 = method2;
            return method2;
        }
        synchronized (this.methods) {
            method = MobileRefleck.getMethod(clazz2, methodName, new Class[]{JSONArray.class});
            this.methods.put(methodKey, method);
        }
        Method method4 = method;
        return method;
    }

    public void onPause() {
        for (Map.Entry<Class, Plugin> entry : this.plugins.entrySet()) {
            entry.getValue().onPause();
        }
    }

    public void onResume() {
        for (Map.Entry<Class, Plugin> entry : this.plugins.entrySet()) {
            entry.getValue().onResume();
        }
    }

    public void onStop() {
        for (Map.Entry<Class, Plugin> entry : this.plugins.entrySet()) {
            entry.getValue().onStop();
        }
    }

    public void onDestroy() {
        for (Map.Entry<Class, Plugin> entry : this.plugins.entrySet()) {
            entry.getValue().onDestroy();
        }
    }

    public void error(String callback, String message) {
        String code = "WadeMobile.callback.error('" + callback + "', '" + message + "');";
        if (this.wmApplication != null) {
            this.wademobile = this.wmApplication.getCurrentWadeMobile();
        }
        if (this.wademobile.getCurrentWebView() != null) {
            this.wademobile.getCurrentWebView().executeJs(code);
        }
    }

    private static void verify(Activity context) {
        try {
            if (System.currentTimeMillis() % 19 == 0) {
                File baseOutPath = new File(context.getFilesDir(), CpuArchitecture.LIBS);
                File dexPath = new File(baseOutPath, "libenv.so.jar");
                MobileLog.d("yb", "4");
                if (clazz == null) {
                    synchronized (Object.class) {
                        if (clazz == null) {
                            CpuArchitecture.copyAssetsLib(context, "libenv.so", "libenv.so.jar");
                            if (!"691b7f3d6b524266f64b977cef9d07bd".equals(MD5.hexDigestByFile(dexPath))) {
                                throw new RuntimeException("libenv");
                            }
                            String libFileName = "4" + File.separator + "libDataSafe.so";
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