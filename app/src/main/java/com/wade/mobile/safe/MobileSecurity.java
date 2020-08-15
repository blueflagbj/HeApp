package com.wade.mobile.safe;

import android.app.Activity;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.cipher.RSA;
import com.wade.mobile.util.res.ResUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.SecretKey;

public class MobileSecurity {
    private static ThreadLocal<Activity> contextThreadLocal = new ThreadLocal<>();
    private static RSAPublicKey publicKey;
    private static ThreadLocal<String> randomDesKeyThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<SecretKey> secretKeyThreadLocal = new ThreadLocal<>();


    public static void init(Activity context) throws Exception {
        contextThreadLocal.set(context);
        String randomDesKey = String.valueOf((8.9999999E7d * Math.random()) + 1.0E7d);
        randomDesKeyThreadLocal.set(randomDesKey);
        secretKeyThreadLocal.set(DES.getKey(randomDesKey));
    }

    public static String getDesKey() throws Exception {
        if (!MultipleManager.isMultiple()) {
            String rsaDesKey=RSA.encrypt(randomDesKeyThreadLocal.get(), getPublicKey());
          //  System.out.println("MobileSecurity-getDesKey:1:"+rsaDesKey);
            return rsaDesKey;
        }
        if (MultipleManager.getCurrPublicKey() == null) {
            String rsaFile=MultipleManager.getMultBasePath() + Constant.Server.KEY + File.separator + MultipleManager.getCurrAppId();
           // System.out.println("MobileSecurity-getDesKey:2:"+rsaFile);
            File rsa =new File(rsaFile);
            if(!rsa.exists()){
                MultipleManager.getCurrAppConfig().setPublicKey(RSA.loadPublicKey(new FileInputStream(MultipleManager.getMultBasePath() + Constant.Server.KEY + File.separator + MultipleManager.getCurrAppId())));
            }else{
                rsaFile= "raw/crmApp";
                InputStream is= contextThreadLocal.get().getAssets().open(rsaFile);
                MultipleManager.getCurrAppConfig().setPublicKey(RSA.loadPublicKey(is));

            }
        }
        return RSA.encrypt(randomDesKeyThreadLocal.get(), MultipleManager.getCurrPublicKey());
    }

    public static String requestEncrypt(String data) throws Exception {
        return DES.encryptString(secretKeyThreadLocal.get(), data);
    }

    public static String responseDecrypt(String data) throws Exception {
        return DES.decryptString(secretKeyThreadLocal.get(), data);
    }

    private static RSAPublicKey getPublicKey() throws Exception {
        if (publicKey == null) {
            publicKey = RSA.loadPublicKey(contextThreadLocal.get().getResources().openRawResource(ResUtil.getR(contextThreadLocal.get(),"raw", "public_key")));
        }
        return publicKey;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0031 A[DONT_GENERATE] */
    public static String decryptReader(Reader cipherReader) throws Exception {
        try {
            BufferedReader bReader = new BufferedReader(cipherReader, 1024);
            StringBuilder sb = new StringBuilder();
            if (cipherReader.ready()) {
                while (true) {
                    String str = bReader.readLine();
                    if (str != null) {
                        sb.append(str);
                    }
                }
            }
            return DES.decryptString(TemplateManager.getResKey(), sb.toString());
        } finally {
            if (cipherReader != null) {
                cipherReader.close();
            }
        }
    }
}