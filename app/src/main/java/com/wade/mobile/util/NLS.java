package com.wade.mobile.util;

import android.annotation.SuppressLint;
import android.os.Build;
import com.wade.mobile.frame.config.MobileConfig;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class NLS {

    public static void initializeMessages(String bundleName, String className) {
/*        System.out.println("NLS001"+bundleName);
        System.out.println("NLS002"+className);
        System.out.println("NLS003"+Locale.getDefault());*/
        String value;

        ResourceBundle messages = ResourceBundle.getBundle(bundleName,Locale.getDefault());
       // System.out.println("NLS004"+className);
        try {
            Class<?> cls = Class.forName(className);
            Field[] fields = cls.getFields();
            int len = fields.length;
            for (int i = 0; i < len; i++) {
                try {
                    //String defaultTunnelName = new String(prop.getString("defaultTunnelName").getBytes("ISO-8859-1"), "UTF-8");
                  //  value = new String(messages.getString(fields[i].getName()).getBytes("UTF-8"), "UTF-8");

                    value = messages.getString(fields[i].getName());
                  //  System.out.println("NLS004"+value);
                } catch (MissingResourceException e) {
                    value = null;
                }
                fields[i].set(cls, value);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}