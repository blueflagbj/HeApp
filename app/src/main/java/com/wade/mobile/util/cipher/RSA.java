package com.wade.mobile.util.cipher;

import com.wade.mobile.util.Base64;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RSA extends AbstractCipher {
    private static final String ENCRYPT_PADDING_TYPE = "RSA/ECB/PKCS1Padding";
    private static final String ENCRYPT_TYPE = "RSA";
    private static final String encode = "UTF-8";

    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            return (RSAPublicKey) KeyFactory.getInstance(ENCRYPT_TYPE).generatePublic(new X509EncodedKeySpec(Base64.decodeByte(publicKeyStr)));
        } catch (NoSuchAlgorithmException e) {
            throw new MobileException("无此算法");
        } catch (InvalidKeySpecException e2) {
            throw new MobileException("公钥非法");
        } catch (NullPointerException e3) {
            throw new MobileException("公钥数据为空");
        }
    }

    public static RSAPublicKey loadPublicKey(InputStream is) throws Exception {
        try {
            RSAPublicKey publicKey = (RSAPublicKey) CertificateFactory.getInstance("X.509").generateCertificate(is).getPublicKey();
            if (is != null) {
                is.close();
            }
            return publicKey;
        } catch (NullPointerException e) {
            throw new MobileException("公钥输入流为空");
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    public static String encrypt(String plainText, RSAPublicKey publicKey) throws Exception {

        return Base64.encode(doFinalEncrypt(publicKey, plainText.getBytes("UTF-8"), ENCRYPT_PADDING_TYPE));
    }

}
