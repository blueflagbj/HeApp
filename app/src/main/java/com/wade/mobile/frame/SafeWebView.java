package com.wade.mobile.frame;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebView;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.util.Constant;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class SafeWebView extends WebView {
    public static final String JS_ARGS_KEY = "args";
    public static final String JS_ARG_PREFIX = "arg";
    public static final String JS_FUNC_KEY = "func";
    public static final String JS_INTERFACE_KEY = "inf";
    public static final String JS_PROMPT_MSG_HEADER = "_$$_SafelyJsInterface:_";
    private static final HashSet<String> allFilterMethodsSet = new HashSet<>(8, 1.0f);
    private HashMap<String, Object> allJsInterfacesMap;
    private String jsStringCache;
    private boolean safeInject;

    static {
        allFilterMethodsSet.add("getClass");
        allFilterMethodsSet.add("hashCode");
        allFilterMethodsSet.add("notify");
        allFilterMethodsSet.add("notifyAll");
        allFilterMethodsSet.add("equals");
        allFilterMethodsSet.add("toString");
        allFilterMethodsSet.add("wait");
    }

    public SafeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.allJsInterfacesMap = new HashMap<>();
        this.safeInject = Boolean.valueOf(MobileConfig.getInstance().getConfigValue("safe_inject", Constant.TRUE)).booleanValue();
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842885);
    }

    public SafeWebView(Context context) {
        this(context, (AttributeSet) null);
    }

    public void addJavascriptInterface(Object object, String name) {
        if (object != null && !TextUtils.isEmpty(name)) {
            if (!isSafeInject()) {
                super.addJavascriptInterface(object, name);
                return;
            }
            this.allJsInterfacesMap.put(name, object);
            this.jsStringCache = null;
        }
    }

    public void removeJavascriptInterface(String name) {
        if (!isSafeInject()) {
            super.removeJavascriptInterface(name);
            return;
        }
        this.allJsInterfacesMap.remove(name);
        this.jsStringCache = null;
    }

    public String getJavascriptInterfaceString() {
        if (this.jsStringCache == null) {
            this.jsStringCache = createJsInterfacesString();
        }
        return this.jsStringCache;
    }

    /* access modifiers changed from: protected */
    public String createJsInterfacesString() {
        if (this.allJsInterfacesMap.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<script>(function(){");
        for (Map.Entry<String, Object> entry : this.allJsInterfacesMap.entrySet()) {
            createJsMethod(entry.getKey(), entry.getValue(), sb);
        }
        sb.append("})();</script>");
        return sb.toString();
    }

    private void createJsMethod(String interfaceName, Object obj, StringBuilder sb) {
        Class<?> cls = obj.getClass();
        sb.append(String.format("\twindow.%s = { ", new Object[]{interfaceName}));
        for (Method method : cls.getMethods()) {
            if (method.getAnnotation(JavascriptInterface.class) != null) {
                String methodName = method.getName();
                if (!allFilterMethodsSet.contains(methodName)) {
                    sb.append(String.format("\t%s:function(", new Object[]{methodName}));
                    int argCount = method.getParameterTypes().length;
                    if (argCount > 0) {
                        int maxCount = argCount - 1;
                        for (int i = 0; i < maxCount; i++) {
                            sb.append(JS_ARG_PREFIX).append(i).append(Constant.PARAMS_SQE);
                        }
                        sb.append(JS_ARG_PREFIX).append(argCount - 1);
                    }
                    sb.append(") {");
                    if (method.getReturnType() != Void.TYPE) {
                        sb.append("            return ");
                    }
                    sb.append(String.format("prompt('%s'+JSON.stringify({", new Object[]{JS_PROMPT_MSG_HEADER}));
                    sb.append(String.format("%s:'%s',", new Object[]{JS_INTERFACE_KEY, interfaceName}));
                    sb.append(String.format("%s:'%s',", new Object[]{JS_FUNC_KEY, methodName}));
                    sb.append(String.format("%s:[", new Object[]{JS_ARGS_KEY}));
                    if (argCount > 0) {
                        int max = argCount - 1;
                        for (int i2 = 0; i2 < max; i2++) {
                            sb.append(String.format("%s%s,", new Object[]{JS_ARG_PREFIX, Integer.valueOf(i2)}));
                        }
                        sb.append(JS_ARG_PREFIX).append(max);
                    }
                    sb.append("]})");
                    sb.append(");");
                    sb.append("}, ");
                }
            }
        }
        sb.setLength(sb.length() - 1);
        sb.append("};");
        sb.append(String.format("console.log('inject %s success');", new Object[]{obj.getClass().getSimpleName()}));
    }

    public void removeSearchBoxJavaBridge() {
        if (gtHoneyComb() && !gtJellyBeanMR1()) {
            super.removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }

    public static boolean gtHoneyComb() {
        return MobileAppInfo.getOsVersionNumber() >= 11;
    }

    public static boolean gtJellyBeanMR1() {
        return MobileAppInfo.getOsVersionNumber() >= 17;
    }

    public void setSafeInject(boolean safeInject2) {
        this.safeInject = safeInject2;
    }

    public boolean isSafeInject() {
        return this.safeInject && !gtJellyBeanMR1();
    }

    public boolean handleJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        int count;
        try {
            JSONObject jsonObj = new JSONObject(message.substring(JS_PROMPT_MSG_HEADER.length()));
            String interfaceName = jsonObj.getString(JS_INTERFACE_KEY);
            String methodName = jsonObj.getString(JS_FUNC_KEY);
            if (allFilterMethodsSet.contains(methodName)) {
                throw new MobileException(methodName + "方法不允许被调用");
            }
            JSONArray argsArray = jsonObj.getJSONArray(JS_ARGS_KEY);
            Object[] args = null;
            if (argsArray != null && (count = argsArray.length()) > 0) {
                args = new Object[count];
                for (int i = 0; i < count; i++) {
                    args[i] = argsArray.get(i);
                }
            }
            return invokeJSInterfaceMethod(result, interfaceName, methodName, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName, Object[] args) {
        Object obj = this.allJsInterfacesMap.get(interfaceName);
        if (obj == null) {
            result.cancel();
            return false;
        }
        try {
            if ("PluginManager".equals(interfaceName) && "exec".equals(methodName)) {
                ((PluginManager) obj).exec(args[0].toString(), args[1].toString(), args[2].toString());
            }
            result.cancel();
            return true;
        } catch (Exception e) {
            throw new MobileException((Throwable) e);
        }
    }
}