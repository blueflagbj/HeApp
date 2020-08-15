package com.ai.ipu.mobile.safe;

import android.app.Activity;

import com.heclient.heapp.R;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.multiple.MultipleManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.cipher.RSA;
import com.wade.mobile.util.http.HttpTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

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
            return RSA.encrypt(randomDesKeyThreadLocal.get(), getPublicKey());
        }
        if (MultipleManager.getCurrPublicKey() == null) {
            MultipleManager.getCurrAppConfig().setPublicKey(RSA.loadPublicKey(new FileInputStream(MultipleManager.getMultBasePath() + Constant.Server.KEY + File.separator + MultipleManager.getCurrAppId())));
        }
        return RSA.encrypt(randomDesKeyThreadLocal.get(), MultipleManager.getCurrPublicKey());
    }

    public static String requestEncrypt(String data) throws Exception {
        return DES.encryptString(secretKeyThreadLocal.get(), data);
    }

    public static String responseDecrypt(String data) throws Exception {
        return DES.decryptString(secretKeyThreadLocal.get(), data);
    }
/*修改获取public key位置*/
    private static RSAPublicKey getPublicKey() throws Exception {
        if (publicKey == null) {
            publicKey = RSA.loadPublicKey(contextThreadLocal.get().getResources().openRawResource(R.raw.public_key));
        }
        return publicKey;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0031 A[DONT_GENERATE] */
    public static String decryptReader(Reader cipherReader) throws Exception {
        try {
            BufferedReader bReader = new BufferedReader(cipherReader, 1024);
            StringBuilder sb = new StringBuilder();
            if (cipherReader.ready()) {
                String str;
                while ((str = bReader.readLine()) != null)
                    sb.append(str);
                /*char[] theChars = new char[128];
                int charsRead = bReader.read(theChars, 0, theChars.length);
                while(charsRead != -1) {
                    //  System.out.println(MessageFormat.format("MustacheTemplate-render4-1-3:readStringByReader:{0}", new String(theChars, 0, charsRead)));
                    charsRead = bReader.read(theChars, 0, theChars.length);
                    sb.append( new String(theChars, 0, charsRead));
                }*/
            }
            String plainText = DES.decryptString(TemplateManager.getResKey(), sb.toString());
            return plainText;
        } finally {
            if (cipherReader != null) {
                cipherReader.close();
            }
        }
    }

}
