package com.heclient.heapp.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MobileRefleck {
    private static final Map<Class<?>, Class<?>> primitiveClazz = new HashMap<Class<?>, Class<?>>();

    static {
        primitiveClazz.put(Integer.class, int.class);
        primitiveClazz.put(Byte.class, byte.class);
        primitiveClazz.put(Character.class, char.class);
        primitiveClazz.put(Short.class, short.class);
        primitiveClazz.put(Long.class, long.class);
        primitiveClazz.put(Float.class, float.class);
        primitiveClazz.put(Double.class, double.class);
        primitiveClazz.put(Boolean.class, boolean.class);
    }

    public static Object getByArray(Object paramObject, int paramInt) {
        return Array.get(paramObject, paramInt);
    }

    private static Field getField(Class<?> paramClass, String paramString) {
        Field field = null;
        try {
            Field field1 = paramClass.getDeclaredField(paramString);
            field = field1;
        } catch (NoSuchFieldException noSuchFieldException) {}
        if (field != null)
            field.setAccessible(true);
        return field;
    }

    public static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass) {
        Method method =null;
        try {
            Method method1 = paramClass.getDeclaredMethod(paramString, paramArrayOfClass);
            method = method1;
        } catch (NoSuchMethodException noSuchMethodException) {}
        if (method != null)
            method.setAccessible(true);
        return method;
    }

    private static Class<?>[] getParamType(Class<?>[] paramArrayOfClass) {
        if (paramArrayOfClass == null)
            return null;
        byte b = 0;
        int i = paramArrayOfClass.length;
        while (true) {
            Class<?>[] arrayOfClass = paramArrayOfClass;
            if (b < i) {
                if (primitiveClazz.containsKey(paramArrayOfClass[b]))
                    paramArrayOfClass[b] = primitiveClazz.get(paramArrayOfClass[b]);
                b++;
                continue;
            }
            return arrayOfClass;
        }
    }

    public static Object getProperty(Object paramObject, String paramString) throws Exception {
        return getField(paramObject.getClass(), paramString).get(paramObject);
    }

    public static Object getStaticProperty(Class<?> paramClass, String paramString) throws Exception {
        return getField(paramClass, paramString).get(paramClass);
    }

    public static Object getStaticProperty(String paramString1, String paramString2) throws Exception {
        return getStaticProperty(Class.forName(paramString1), paramString2);
    }

    private static Object invoke(Class<?> paramClass, Object paramObject, String paramString, Object[] paramArrayOfObject) throws Exception {
        Class[] arrayOfClass = null;
        if (paramArrayOfObject != null) {
            Class[] arrayOfClass1 = new Class[paramArrayOfObject.length];
            byte b = 0;
            int i = paramArrayOfObject.length;
            while (true) {
                arrayOfClass = arrayOfClass1;
                if (b < i) {
                    arrayOfClass1[b] = paramArrayOfObject[b].getClass();
                    b++;
                    continue;
                }
                break;
            }
        }
        return invoke(paramClass, paramObject, paramString, paramArrayOfObject, arrayOfClass);
    }

    private static Object invoke(Class<?> paramClass, Object paramObject, String paramString, Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass) throws Exception {
        return getMethod(paramClass, paramString, getParamType(paramArrayOfClass)).invoke(paramObject, paramArrayOfObject);
    }

    public static Object invokeMethod(Class<?> paramClass, Object paramObject, String paramString, Object... paramVarArgs) throws Exception {
        return invoke(paramClass, paramObject, paramString, paramVarArgs);
    }

    public static Object invokeMethod(Class<?> paramClass, Object paramObject, String paramString, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        return invoke(paramClass, paramObject, paramString, paramArrayOfObject, paramVarArgs);
    }

    public static Object invokeMethod(Object paramObject, String paramString, Object... paramVarArgs) throws Exception {
        return invoke(paramObject.getClass(), paramObject, paramString, paramVarArgs);
    }

    public static Object invokeMethod(Object paramObject, String paramString, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        return invoke(paramObject.getClass(), paramObject, paramString, paramArrayOfObject, paramVarArgs);
    }

    public static Object invokeStaticMethod(Class<?> paramClass, String paramString, Object... paramVarArgs) throws Exception {
        return invoke(paramClass, null, paramString, paramVarArgs);
    }

    public static Object invokeStaticMethod(Class<?> paramClass, String paramString, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        return invoke(paramClass, null, paramString, paramArrayOfObject, paramVarArgs);
    }

    public static Object invokeStaticMethod(String paramString1, String paramString2, Object... paramVarArgs) throws Exception {
        return invoke(Class.forName(paramString1), null, paramString2, paramVarArgs);
    }

    public static Object invokeStaticMethod(String paramString1, String paramString2, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        return invoke(Class.forName(paramString1), null, paramString2, paramArrayOfObject, paramVarArgs);
    }

    public static boolean isInstance(Object paramObject, Class<?> paramClass) {
        return paramClass.isInstance(paramObject);
    }

    public static boolean isInstance(Object paramObject, String paramString) throws ClassNotFoundException {
        return Class.forName(paramString).isInstance(paramObject);
    }

    public static Object newInstance(Class<?> paramClass, Object... paramVarArgs) throws Exception {
        Class[] arrayOfClass = new Class[paramVarArgs.length];
        byte b = 0;
        int i = paramVarArgs.length;
        while (b < i) {
            arrayOfClass[b] = paramVarArgs[b].getClass();
            b++;
        }
        return newInstance(paramClass, paramVarArgs, arrayOfClass);
    }

    public static Object newInstance(Class<?> paramClass, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        Constructor<?> constructor = paramClass.getDeclaredConstructor(getParamType(paramVarArgs));
        constructor.setAccessible(true);
        return constructor.newInstance(paramArrayOfObject);
    }

    public static Object newInstance(String paramString) throws Exception {
        return Class.forName(paramString).newInstance();
    }

    public static Object newInstance(String paramString, Object... paramVarArgs) throws Exception {
        return newInstance(Class.forName(paramString), paramVarArgs);
    }

    public static Object newInstance(String paramString, Object[] paramArrayOfObject, Class<?>... paramVarArgs) throws Exception {
        return newInstance(Class.forName(paramString), paramArrayOfObject, paramVarArgs);
    }

    public static void setProperty(Object paramObject1, String paramString, Object paramObject2) throws IllegalArgumentException, IllegalAccessException {
        getField(paramObject1.getClass(), paramString).set(paramObject1, paramObject2);
    }

    public static void setStaticProperty(String paramString1, String paramString2, Object paramObject) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        Class<?> clazz = Class.forName(paramString1);
        getField(clazz, paramString2).set(clazz, paramObject);
    }
}
