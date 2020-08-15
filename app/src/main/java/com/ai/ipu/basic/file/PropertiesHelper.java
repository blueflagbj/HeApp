package com.ai.ipu.basic.file;

import com.ai.ipu.basic.util.IpuLog;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/* renamed from: com.ai.ipu.basic.file.PropertiesHelper */
public class PropertiesHelper extends Properties {
    private static final long serialVersionUID = 785763976621477589L;

    public PropertiesHelper(String path) throws IOException {
        load(path);
    }

    public PropertiesHelper(InputStream in) throws IOException {
        try {
            super.load(in);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    IpuLog.error("PropertiesHelper error", e);
                    throw e;
                }
            }
        } catch (IOException e2) {
            IpuLog.error("PropertiesHelper error", e2);
            throw e2;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    IpuLog.error("PropertiesHelper error", e3);
                    throw e3;
                }
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0029  */
    public void load(String path) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(PropertiesHelper.class.getClassLoader().getResource(path).getPath());
            super.load(stream);
        } catch (IOException e) {
            IpuLog.error("PropertiesHelper error", e);
            throw e;
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