package com.wade.mobile.frame.engine.impl;


import com.samskivert.mustache.Mustache;
import com.wade.mobile.common.MobileCache;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerConfig;
import com.wade.mobile.frame.engine.AbstractTemplateEngine;
import com.wade.mobile.frame.engine.mustache.DefaultTemplateLoader;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.FileUtil;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class MustacheTemplateEngine extends AbstractTemplateEngine {
    private static final String DELIMS = "{% %}";
    private static final String TEMPLATE_HTML = "TEMPLATE_HTML";
    private String basePath;
    private String templateName;

    static {
        MobileCache.getInstance().put(TEMPLATE_HTML, new HashMap<>());
    }

    public MustacheTemplateEngine(String templateName2, String basePath2) throws Exception {
        this.templateName = templateName2;
        this.basePath = basePath2;
    }

    /* access modifiers changed from: protected */
    public void render(Map<?, ?> data, Writer out) throws Exception {

        InputStream in = null;
        try {
            String templatePath = FileUtil.connectFilePath(this.basePath, this.templateName);
            String originalHtml = null;
            System.out.println("MustacheTemplate-render0:"+templatePath);
            if (true) {
                if (this.basePath.startsWith("assets")) {
                    in = MustacheTemplateEngine.class.getClassLoader().getResourceAsStream(templatePath);
                    System.out.println("MustacheTemplate-render1-0:");
                } else {
                    in = new FileInputStream(templatePath);
                    System.out.println("MustacheTemplate-render1-1:");
                }

                String encode = MobileConfig.getInstance().getEncode();
                System.out.println("MustacheTemplate-render3:"+encode);
                if (Constant.TRUE.equals(ServerConfig.getInstance().getValue(Constant.ServerConfig.FILE_ENCRYPT))) {
                    System.out.println("MustacheTemplate-render4-0:");
                    originalHtml = MobileSecurity.decryptReader(new InputStreamReader(in, encode));
                    System.out.println("MustacheTemplate-render4-0:"+originalHtml);
                } else {
                    System.out.println("MustacheTemplate-render4-1:");
                    originalHtml = readStringByReader(new InputStreamReader(in, encode));
                    System.out.println("MustacheTemplate-render4-1:"+originalHtml);
                }
            }
            System.out.println("MustacheTemplate-render5:");
            out.append(Mustache.compiler().defaultValue("").withLoader(new DefaultTemplateLoader(data, this.basePath)).withDelims(DELIMS).compile((Reader) new StringReader(originalHtml)).execute(data));

        } finally {
            if (in != null) {
                in.close();
            }
        }
        System.out.println("MustacheTemplate-render6:");
    }


    /* JADX WARNING: Removed duplicated region for block: B:13:0x0029 A[DONT_GENERATE] */
    private String readStringByReader(Reader cipherReader) throws Exception {

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bReader = new BufferedReader(cipherReader, 8192);
            System.out.println("MustacheTemplate-render4-1-1:readStringByReader");
            if (cipherReader.ready()) {
                System.out.println("MustacheTemplate-render4-1-2:readStringByReader");
/*                while (true) {
                    String str = bReader.readLine();
                    System.out.println("MustacheTemplate-render4-1-3:readStringByReader:"+str);
                    if (str != null) {
                        sb.append(str);
                    }
                }*/
                char[] theChars = new char[128];

                int charsRead = bReader.read(theChars, 0, theChars.length);
                while(charsRead != -1) {
                  //  System.out.println(MessageFormat.format("MustacheTemplate-render4-1-3:readStringByReader:{0}", new String(theChars, 0, charsRead)));
                    charsRead = bReader.read(theChars, 0, theChars.length);
                    sb.append( new String(theChars, 0, charsRead));
                }
            }
            System.out.println("MustacheTemplate-render4-1-1:readStringByReader" + sb.toString());

        }catch (Exception e){
            System.out.println("MustacheTemplate-render4-1-3:readStringByReader" + e.toString());
        } finally {
            if (cipherReader != null) {
                cipherReader.close();
            }
        }
        return sb.toString();
    }

}