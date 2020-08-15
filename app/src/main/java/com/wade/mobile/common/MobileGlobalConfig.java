package com.wade.mobile.common;


import com.ai.ipu.basic.file.PropertiesHelper;
import com.wade.mobile.util.Constant;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MobileGlobalConfig {
    public static final String LOG_LEVEL = "log.level";
    private static PropertiesHelper proHelper;

    static {
        InputStream stream = PropertiesHelper.class.getClassLoader().getResourceAsStream(Constant.IPU_GLOBAL_CONFIG);
        if (stream == null) {
            try {
                stream = new FileInputStream(new File(Constant.IPU_GLOBAL_CONFIG));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            proHelper = new PropertiesHelper(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
    }

    private MobileGlobalConfig() {
    }

    public static String getValue(String key) {
        if (proHelper == null) {
            return null;
        }
        return proHelper.getProperty(key);
    }
}