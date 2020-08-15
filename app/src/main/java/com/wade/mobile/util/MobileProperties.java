package com.wade.mobile.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MobileProperties extends Properties {
    private static final long serialVersionUID = 5250040804288728227L;

    public MobileProperties() {
    }

    public MobileProperties(String path) throws IOException {
        load(path);
    }

    public MobileProperties(InputStream in) {
        try {
            super.load(in);
            System.out.println("0005321");
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void load(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        try {
            super.load(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public Map<String, ?> getProMap() {
        Map<String, Object> map = new HashMap<>();
        for (Entry<Object, Object> entry : entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue());
        }
        return map;
    }
}