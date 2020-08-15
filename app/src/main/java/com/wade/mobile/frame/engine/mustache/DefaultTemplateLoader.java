package com.wade.mobile.frame.engine.mustache;


import com.samskivert.mustache.Mustache;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

public class DefaultTemplateLoader implements Mustache.TemplateLoader {
    private Map<?, ?> data;
    private String templateRoot;

    public DefaultTemplateLoader(Map<?, ?> data2, String templateRoot2) {
        this.data = data2;
        this.templateRoot = templateRoot2;
    }

    public Reader getTemplate(String partial) throws Exception {
        if (partial == null) {
            return new StringReader("");
        }
        if (partial.endsWith(".html")) {
            String partialPath = FileUtil.connectFilePath(this.templateRoot, partial);
            InputStream is = null;
            try {
                is = TemplateManager.getBasePath().startsWith("assets") ? DefaultTemplateLoader.class.getClassLoader().getResourceAsStream(partialPath) : new FileInputStream(partialPath);
            } catch (Exception e) {
                if (is != null) {
                    is.close();
                }
            }
            Reader reader = new InputStreamReader(is, MobileConfig.getInstance().getEncode());
            if (Constant.TRUE.equals(ServerConfig.getInstance().getValue(Constant.ServerConfig.FILE_ENCRYPT))) {
                return new StringReader(MobileSecurity.decryptReader(reader));
            }
            return reader;
        }
        return new StringReader(this.data.get(partial) == null ? "" : this.data.get(partial).toString());
    }
}