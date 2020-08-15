package com.wade.mobile.frame.template;


import android.app.Activity;
import android.os.Process;
import com.wade.mobile.app.AppRecord;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeWebView;
import com.wade.mobile.frame.client.WadeWebViewClient;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.engine.ITemplateEngine;
import com.wade.mobile.frame.engine.impl.MustacheTemplateEngine;
import com.wade.mobile.frame.event.impl.TemplateWebViewEvent;
import com.wade.mobile.frame.lua.ILuaMonitor;
import com.wade.mobile.frame.lua.impl.LuaMonitor;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.Utility;
import com.wade.mobile.util.cipher.MD5;
import com.wade.mobile.util.lua.LuaUtil;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateWebView extends WadeWebView {
    private static Class<?> clazz;
    private static boolean hasLoaded = false;
    protected String basePath;
    protected String baseUrl;

    public TemplateWebView(IWadeMobile wademobile) {
        super(wademobile);
        try {
            String luaBasePath = this.context.getApplicationContext().getFilesDir().getCanonicalPath() + File.separator + "lua" + File.separator + CpuArchitecture.getCpuBit();
            if (!MobileAppInfo.getInstance(this.context).getVersionName().equals(AppRecord.getLuaVersion(this.wademobile.getActivity())) || !new File(luaBasePath).exists()) {
               System.out.println("lua file transfer");

                LuaUtil.luaTransfer(this.wademobile.getActivity(), "lua");
                AppRecord.setLuaVersion(this.wademobile.getActivity());
            }
            if (!hasLoaded && Constant.TRUE.equals(ServerConfig.getInstance().getValue(Constant.ServerConfig.IS_USE_TAG))) {
                CpuArchitecture.loadSdcardLib(this.context, false, "libluajava.so");
                hasLoaded = true;
                System.out.println("loading libluajava.so");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.basePath = TemplateManager.getBasePath();
    }

    public TemplateWebView(IWadeMobile wademobile, String basePath2) {
        super(wademobile);
        try {
            String luaBasePath = this.context.getApplicationContext().getFilesDir().getCanonicalPath() + File.separator + "lua" + File.separator + CpuArchitecture.getCpuBit();
            if (!MobileAppInfo.getInstance(this.context).getVersionName().equals(AppRecord.getLuaVersion(this.wademobile.getActivity())) || !new File(luaBasePath).exists()) {
                MobileLog.d(this.TAG, "lua file transfer.");
                LuaUtil.luaTransfer(this.wademobile.getActivity(), "lua");
                AppRecord.setLuaVersion(this.wademobile.getActivity());
            }
            if (!hasLoaded && Constant.TRUE.equals(ServerConfig.getInstance().getValue(Constant.ServerConfig.IS_USE_TAG))) {
                CpuArchitecture.loadSdcardLib(this.context, false, "libluajava.so");
                hasLoaded = true;
                MobileLog.d(this.TAG, "loading libluajava.so");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.basePath = basePath2;
    }

    /* access modifiers changed from: protected */
    public void initialize() {
        setWebViewClient(new WadeWebViewClient(this.wademobile, new TemplateWebViewEvent(this.wademobile)));
    }

    public void loadTemplate(String templatePath, Map data) throws Exception {
        String jsStr;
       // System.out.println("TemplateWebView-loadTemplate0:"+templatePath);
        //verify(this.context);//水印
        StringBuilder templateBuff = new StringBuilder();
        String templateHtml = getTemplateHtml(templatePath, data);
       // System.out.println("TemplateWebView-loadTemplate1:"+templateHtml);
        templateBuff.append(createGlobalJsObject());
        if (isSafeInject() && (jsStr = getJavascriptInterfaceString()) != null) {
            templateBuff.append(jsStr);
        }
        templateBuff.append(templateHtml);
        String html = templateBuff.toString();
        if (this.baseUrl == null) {
            if (this.basePath.startsWith("assets")) {
                this.baseUrl = "file:///android_asset/" + this.basePath.replace("assets/", "");
            } else {
                this.baseUrl = Constant.START_FILE + this.basePath;
            }
        }
       // System.out.println("TemplateWebView-loadTemplate3:"+this.baseUrl);
        final String str = html;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
              //  System.out.println("TemplateWebView-loadTemplate4:");
                TemplateWebView.this.loadDataWithBaseURL(TemplateWebView.this.baseUrl, str, Constant.MINE_TYPE_HTML, MobileConfig.getInstance().getEncode(), (String) null);
            }
        });
    }
    private static void verify(Activity context) {
        try {
            if (System.currentTimeMillis() % 19 == 0) {
                File baseOutPath = new File(context.getFilesDir(), CpuArchitecture.LIBS);
                File dexPath = new File(baseOutPath, "libenv.so.jar");
                MobileLog.d("yb", "5");
                if (clazz == null) {
                    synchronized (Object.class) {
                        if (clazz == null) {
                            CpuArchitecture.copyAssetsLib(context, "libenv.so", "libenv.so.jar");
                            if (!"691b7f3d6b524266f64b977cef9d07bd".equals(MD5.hexDigestByFile(dexPath))) {
                                throw new RuntimeException("libenv");
                            }
                            String libFileName = "5" + File.separator + "libDataSafe.so";
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

    public String getTemplate(String templatePath, Map data) throws Exception {
        return getTemplateHtml(templatePath, data);
    }

    /* access modifiers changed from: protected */
    public String getTemplateHtml(String templatePath, Map data) throws Exception {
        System.out.println("TemplateWebView-getTemplateHtml0:");
        if (data == null) {
            data = new HashMap();
        }
        Map<String, String> global = new HashMap<>();
        global.put(Constant.Server.IS_APP, Constant.TRUE);
        data.put(Constant.Server.MOBILE, global);
        System.out.println("TemplateWebView-getTemplateHtml2:");
        ITemplateEngine template = new MustacheTemplateEngine(templatePath, this.basePath);
        System.out.println("TemplateWebView-getTemplateHtml3:");
        try {
            template.bind(data);
        }catch (Exception e){
            //System.out.println("TemplateWebView-getTemplateHtml4-0:"+e.toString());
            MobileLog.e(TAG, e.getMessage(), e);
        }

        System.out.println("TemplateWebView-getTemplateHtml4:");
        String html = template.getHtml();
        System.out.println("TemplateWebView-getTemplateHtml5:");
        if (Constant.TRUE.equals(ServerConfig.getInstance().getValue(Constant.ServerConfig.IS_USE_TAG))) {
            System.out.println("TemplateWebView-getTemplateHtml6-0:");
            return parseWmTag(template.getHtml());
        }
        System.out.println("TemplateWebView-getTemplateHtml6:");
        return html;
    }

    /* JADX INFO: finally extract failed */
    private String parseWmTag(String html) throws Exception {
        System.out.println("TemplateWebView-parseWmTag0:");
        ILuaMonitor monitor = new LuaMonitor();
        String luaBasePath = this.context.getApplicationContext().getFilesDir().getCanonicalPath() + File.separator + "lua" + File.separator + CpuArchitecture.getCpuBit();
        System.out.println("TemplateWebView-parseWmTag1:"+luaBasePath);
        String str = html;
        try {
            System.out.println("TemplateWebView-parseWmTag2-0:"+luaBasePath + File.separator + "index.lua");
            LuaUtil.loadLuaFile(luaBasePath + File.separator + "index.lua");
            System.out.println("TemplateWebView-parseWmTag2:"+luaBasePath + File.separator + "index.lua");
            LuaUtil.execLua("setMonitor", monitor);
            LuaUtil.execLua("setPackagePath", luaBasePath);
            LuaUtil.execLua("setPackagePath", MobileAppInfo.getSdcardAppPath(this.context) + File.separator + "template" + File.separator + "lua");
            Object result = LuaUtil.execLua("htmlparse", html);
            if (result == null) {
                Utility.error(monitor.getError() + "\n" + monitor.getTrace());
            }
            LuaUtil.close();
            System.out.println("TemplateWebView-parseWmTag6:");
            return result.toString();

        } catch (Throwable th) {
            LuaUtil.close();
            throw th;
        }

    }

    /* access modifiers changed from: protected */
    public String createGlobalJsObject() {
        return "<script>(function(){" + "window.TerminalType = 'a';" + "})();</script>";
    }
}