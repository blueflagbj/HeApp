package com.wade.mobile.util.xml;

import com.ai.ipu.mobile.safe.MobileSecurity;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.util.cipher.MobileException;
import com.yanzhenjie.kalle.util.IOUtils;

import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MobileXML {
    private Map<?, ?> config;
    private boolean isDecrypt;

    public MobileXML(String path) {
        this.config = parse(path);
    }

    public MobileXML(String path, boolean isDecrypt2) {
/*        System.out.println("MobileXML-MobileXML:0:"+path);
        System.out.println("MobileXML-MobileXML:1:"+isDecrypt2);*/
        this.isDecrypt = isDecrypt2;
        this.config = parse(path);
      //  System.out.println("MobileXML-MobileXML:6:"+this.config.toString());
    }

    public Map<?, ?> getConfig() {
        return this.config;
    }

    private Map<String, List<Map<String, String>>> parse(String path) {
        Map map =null;
        try {
          //  System.out.println("parse:0:"+path);
            map =  parseSecond(path, parseFirst(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       // System.out.println("parse:6:"+path);
        return map;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0065 A[SYNTHETIC, Splitter:B:22:0x0065] */
    private Map<String, Set<String>> parseFirst(String path) throws FileNotFoundException {
      //  System.out.println("parseFirst:0:"+path);
        InputStream inStream = Objects.requireNonNull(MobileXML.class.getClassLoader()).getResourceAsStream(path);
       // System.out.println("parseFirst:1:"+path);
        if (inStream == null) {
           // System.out.println("parseFirst:1-1:"+path);
            inStream = new FileInputStream(new File(path));
           // System.out.println("parseFirst:1-2:"+inStream.toString());
        }
        if (this.isDecrypt) {
            try {
              //  System.out.println("parseFirst:2:"+ MobileConfig.getInstance().getEncode());
                Reader cipherReader =new InputStreamReader(inStream,"UTF-8");
              //  System.out.println("parseFirst:2-1:");
                String decrp=MobileSecurity.decryptReader(cipherReader);
              //  System.out.println("parseFirst:2-2:"+decrp);
                inStream = new ByteArrayInputStream(decrp.getBytes("UTF-8"));
                BufferedWriter out = null;
                File file =new File(path);
                try {
                 out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getCanonicalPath() + "_readMsg.txt",true)));
                    out.newLine();
                    out.write(decrp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                // inStream = new ByteArrayInputStream(MobileSecurity.decryptReader(new InputStreamReader(inStream, MobileConfig.getInstance().getEncode())).getBytes(MobileConfig.getInstance().getEncode()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SAXParser saxParser = null;
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        MobileAttrXMLHandler attrHandler = new MobileAttrXMLHandler();
        try {
            saxParser.parse(inStream, attrHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Map<String, Set<String>> attrs = attrHandler.getAttrs();
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
       // System.out.println("parseFirst:6:"+attrs.toString());
        return attrs;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005d A[SYNTHETIC, Splitter:B:22:0x005d] */
    private Map<String, List<Map<String, String>>> parseSecond(String path, Map<String, Set<String>> attrs) {
       // System.out.println("parseSecond:0:"+path);
        InputStream inStream = Objects.requireNonNull(MobileXML.class.getClassLoader()).getResourceAsStream(path);

        if (inStream == null) {
            try {
                inStream = new FileInputStream(new File(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (this.isDecrypt) {
            try {
                inStream = new ByteArrayInputStream(MobileSecurity.decryptReader(new InputStreamReader(inStream)).getBytes(MobileConfig.getInstance().getEncode()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SAXParser saxParser = null;
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        MobileXMLHandler handler = new MobileXMLHandler(attrs);
        try {
            saxParser.parse(inStream, handler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Map<String, List<Map<String, String>>> config2 = handler.getConfig();
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
       // System.out.println("parseSecond:6:"+config2.toString());
        return config2;

    }

    public static void main(String[] args) {
        for (Map.Entry entry : new MobileXML("test.xml").getConfig().entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}