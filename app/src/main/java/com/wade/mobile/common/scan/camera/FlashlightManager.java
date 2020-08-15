package com.wade.mobile.common.scan.camera;

import android.os.IBinder;
import com.wade.mobile.common.MobileLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class FlashlightManager {
    private static final String TAG = FlashlightManager.class.getSimpleName();
    private static final Object iHardwareService = getHardwareService();
    private static final Method setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);

    static {
        if (iHardwareService == null) {
            MobileLog.i(TAG, "This device does supports control of a flashlight");
        } else {
            MobileLog.i(TAG, "This device does not support control of a flashlight");
        }
    }

    private FlashlightManager() {
    }

    static void enableFlashlight() {
        setFlashlight(false);
    }

    static void disableFlashlight() {
        setFlashlight(false);
    }

    private static Object getHardwareService() {
        Method getServiceMethod;
        Object hardwareService;
        Class<?> iHardwareServiceStubClass;
        Method asInterfaceMethod;
        Class<?> serviceManagerClass = maybeForName("android.os.ServiceManager");
        if (serviceManagerClass == null || (getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", String.class)) == null || (hardwareService = invoke(getServiceMethod, (Object) null, "hardware")) == null || (iHardwareServiceStubClass = maybeForName("android.os.IHardwareService$Stub")) == null || (asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", IBinder.class)) == null) {
            return null;
        }
        return invoke(asInterfaceMethod, (Object) null, hardwareService);
    }

    private static Method getSetFlashEnabledMethod(Object iHardwareService2) {
        if (iHardwareService2 == null) {
            return null;
        }
        return maybeGetMethod(iHardwareService2.getClass(), "setFlashlightEnabled", Boolean.TYPE);
    }

    private static Class<?> maybeForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (RuntimeException re) {
            MobileLog.w(TAG, "Unexpected error while finding class " + name, re);
            return null;
        }
    }

    private static Method maybeGetMethod(Class<?> clazz, String name, Class<?>... argClasses) {
        try {
            return clazz.getMethod(name, argClasses);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (RuntimeException re) {
            MobileLog.w(TAG, "Unexpected error while finding method " + name, re);
            return null;
        }
    }

    private static Object invoke(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            MobileLog.w(TAG, "Unexpected error while invoking " + method, e);
            return null;
        } catch (InvocationTargetException e2) {
            MobileLog.w(TAG, "Unexpected error while invoking " + method, e2.getCause());
            return null;
        } catch (RuntimeException re) {
            MobileLog.w(TAG, "Unexpected error while invoking " + method, re);
            return null;
        }
    }

    private static void setFlashlight(boolean active) {
        if (iHardwareService != null) {
            invoke(setFlashEnabledMethod, iHardwareService, Boolean.valueOf(active));
        }
    }
}