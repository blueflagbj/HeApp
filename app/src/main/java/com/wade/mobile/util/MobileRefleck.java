package com.wade.mobile.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MobileRefleck {
    private static final Map<Class<?>, Class<?>> primitiveClazz = new HashMap();

    static {
        primitiveClazz.put(Integer.class, Integer.TYPE);
        primitiveClazz.put(Byte.class, Byte.TYPE);
        primitiveClazz.put(Character.class, Character.TYPE);
        primitiveClazz.put(Short.class, Short.TYPE);
        primitiveClazz.put(Long.class, Long.TYPE);
        primitiveClazz.put(Float.class, Float.TYPE);
        primitiveClazz.put(Double.class, Double.TYPE);
        primitiveClazz.put(Boolean.class, Boolean.TYPE);
    }

    private static Class<?>[] getParamType(Class<?>[] clazz) {
        if (clazz == null) {
            return null;
        }
        int len = clazz.length;
        for (int i = 0; i < len; i++) {
            if (primitiveClazz.containsKey(clazz[i])) {
                clazz[i] = primitiveClazz.get(clazz[i]);
            }
        }
        return clazz;
    }

    public static Object newInstance(String className) throws Exception {
        return Class.forName(className).newInstance();
    }

    public static Object newInstance(String className, Object... args) throws Exception {
        return newInstance(Class.forName(className), args);
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws Exception {
        Class<?>[] argsClass = new Class[args.length];
        int j = args.length;
        for (int i = 0; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
        return newInstance(clazz, args, argsClass);
    }

    public static Object newInstance(String className, Object[] args, Class<?>... argsClass) throws Exception {
        return newInstance(Class.forName(className), args, argsClass);
    }

    public static Object newInstance(Class<?> clazz, Object[] args, Class<?>... argsClass) throws Exception {
        Constructor<?> cons = clazz.getDeclaredConstructor(getParamType(argsClass));
        cons.setAccessible(true);
        return cons.newInstance(args);
    }

    public static boolean isInstance(Object obj, String className) throws ClassNotFoundException {
        return Class.forName(className).isInstance(obj);
    }

    public static boolean isInstance(Object obj, Class<?> clazz) {
        return clazz.isInstance(obj);
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() == null) {
                return null;
            }
            field = getField(clazz.getSuperclass(), fieldName);
        }
        if (field != null) {
            field.setAccessible(true);
        }
        Field field2 = field;
        return field;
    }

    public static Object getProperty(Object owner, String fieldName) throws Exception {
        return getField(owner.getClass(), fieldName).get(owner);
    }

    public static Object getStaticProperty(String className, String fieldName) throws Exception {
        return getStaticProperty(Class.forName(className), fieldName);
    }

    public static Object getStaticProperty(Class<?> clazz, String fieldName) throws Exception {
        return getField(clazz, fieldName).get(clazz);
    }

    public static void setProperty(Object owner, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        getField(owner.getClass(), fieldName).set(owner, value);
    }

    public static void setStaticProperty(String className, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        getField(clazz, fieldName).set(clazz, value);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] classes) {
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() == null) {
                return null;
            }
            method = getMethod(clazz.getSuperclass(), methodName, classes);
        }
        if (method != null) {
            method.setAccessible(true);
        }
        Method method2 = method;
        return method;
    }

    private static Object invoke(Class<?> clazz, Object owner, String methodName, Object[] args, Class<?>[] argsClass) throws Exception {
        return getMethod(clazz, methodName, getParamType(argsClass)).invoke(owner, args);
    }

    private static Object invoke(Class<?> clazz, Object owner, String methodName, Object[] args) throws Exception {
        Class<?>[] argsClass = null;
        if (args != null) {
            argsClass = new Class[args.length];
            int j = args.length;
            for (int i = 0; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
        }
        return invoke(clazz, owner, methodName, args, argsClass);
    }

    public static Object invokeMethod(Object owner, String methodName, Object... args) throws Exception {
        return invoke(owner.getClass(), owner, methodName, args);
    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args, Class<?>... argsClass) throws Exception {
        return invoke(owner.getClass(), owner, methodName, args, argsClass);
    }

    public static Object invokeMethod(Class<?> clazz, Object owner, String methodName, Object... args) throws Exception {
        return invoke(clazz, owner, methodName, args);
    }

    public static Object invokeMethod(Class<?> clazz, Object owner, String methodName, Object[] args, Class<?>... argsClass) throws Exception {
        return invoke(clazz, owner, methodName, args, argsClass);
    }

    public static Object invokeStaticMethod(String className, String methodName, Object... args) throws Exception {
        return invoke(Class.forName(className), (Object) null, methodName, args);
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args, Class<?>... argsClass) throws Exception {
        return invoke(Class.forName(className), (Object) null, methodName, args, argsClass);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        return invoke(clazz, (Object) null, methodName, args);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object[] args, Class<?>... argsClass) throws Exception {
        return invoke(clazz, (Object) null, methodName, args, argsClass);
    }

    public static Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }
}