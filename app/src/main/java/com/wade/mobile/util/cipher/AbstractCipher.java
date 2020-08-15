package com.wade.mobile.util.cipher;

import java.security.Key;
import java.security.Provider;
import com.sun.crypto.provider.DESCipher;
import javax.crypto.Cipher;

public abstract class AbstractCipher {
    protected static final String DES = "DES";
    protected static final String encode = "UTF-8";

    protected static byte[] doFinalDecrypt(Key paramKey, byte[] paramArrayOfbyte, String paramString) throws Exception {
        Cipher cipher = Cipher.getInstance(paramString);
        cipher.init(Cipher.DECRYPT_MODE, paramKey);
        return cipher.doFinal(paramArrayOfbyte);
    }
    protected static byte[] doFinalDecrypt(Key paramKey, byte[] paramArrayOfbyte, String paramString, Provider paramProvider) throws Exception {
        Cipher cipher = Cipher.getInstance(paramString, paramProvider);
        cipher.init(Cipher.DECRYPT_MODE, paramKey);
        return cipher.doFinal(paramArrayOfbyte);
    }

    protected static byte[] doFinalEncrypt(Key paramKey, byte[] paramArrayOfbyte, String paramString) throws Exception {
        Cipher cipher = Cipher.getInstance(paramString);
        cipher.init(Cipher.ENCRYPT_MODE, paramKey);
        return cipher.doFinal(paramArrayOfbyte);
    }

    protected static byte[] doFinalEncrypt(Key paramKey, byte[] paramArrayOfbyte, String paramString, Provider paramProvider) throws Exception {
        Cipher cipher = Cipher.getInstance(paramString, paramProvider);
        cipher.init(Cipher.ENCRYPT_MODE, paramKey);
        return cipher.doFinal(paramArrayOfbyte);
    }

}
