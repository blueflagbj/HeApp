package com.wade.mobile.util.cipher;

import android.util.Log;

import com.wade.mobile.util.Base64;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DES extends AbstractCipher {
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String EQUAL_SIGN = "=";
    public static final String HTTP_DEFUALT_PROXY = "10.0.0.172";
    public static final String PARAMETERS_SEPARATOR = "&";
    public static final String PATHS_SEPARATOR = "/";
    public static final String URL_AND_PARA_SEPARATOR = "?";

    public static SecretKey getKey(@NotNull String key) throws Exception {
        return SecretKeyFactory.getInstance(DES).generateSecret(new DESKeySpec(key.getBytes()));
    }

    public static String decryptString(Key paramKey, String paramString) throws Exception {
        byte[] psBt=Base64.decodeByte(paramString);
       // Log.d(TAG, "Des-decryptString:0: "+ psBt.toString());
        return new String(doFinalDecrypt(paramKey, Base64.decodeByte(paramString),DES), encode);
    }

    public static String encryptString(Key paramKey, String paramString) throws Exception {
        return Base64.encode(doFinalEncrypt(paramKey, paramString.getBytes("UTF-8"), "DES"));
    }
    public static String decryptUrl(String url, SecretKey key) throws Exception {
        if (url.indexOf(URL_AND_PARA_SEPARATOR) < 0) {
            return url;
        }
        String param = url.substring(url.indexOf(URL_AND_PARA_SEPARATOR) + 1);
        String baseUrl = url.substring(0, url.indexOf(URL_AND_PARA_SEPARATOR));
        StringBuffer buff = new StringBuffer();
        buff.append(baseUrl).append(URL_AND_PARA_SEPARATOR);
        for (String arg : param.split(PARAMETERS_SEPARATOR)) {
            String[] tmp = arg.split(EQUAL_SIGN);
            if (tmp.length == 2) {
                buff.append(tmp[0]).append(EQUAL_SIGN).append(decryptString(key, tmp[1]));
                buff.append(PARAMETERS_SEPARATOR);
            }
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }
    public static String encryptUrl(String url, SecretKey key) throws Exception {
        if (url.indexOf(URL_AND_PARA_SEPARATOR) < 0) {
            return url;
        }
        String param = url.substring(url.indexOf(URL_AND_PARA_SEPARATOR) + 1);
        String baseUrl = url.substring(0, url.indexOf(URL_AND_PARA_SEPARATOR));
        StringBuffer buff = new StringBuffer();
        buff.append(baseUrl).append(URL_AND_PARA_SEPARATOR);
        for (String arg : param.split(PARAMETERS_SEPARATOR)) {
            String[] tmp = arg.split(EQUAL_SIGN);
            if (tmp.length == 2) {
                buff.append(tmp[0]).append(EQUAL_SIGN).append(URLEncoder.encode(encryptString(key, tmp[1])));
                buff.append(PARAMETERS_SEPARATOR);
            }
        }
        buff.setLength(buff.length() - 1);
        return buff.toString();
    }



}
