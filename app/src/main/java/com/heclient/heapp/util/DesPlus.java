package com.heclient.heapp.util;

import android.app.Activity;
import android.util.Log;


import com.sun.crypto.provider.SunJCE;
import com.wade.mobile.util.cipher.DES;
import com.wade.mobile.util.cipher.RSA;

import java.security.Key;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DesPlus {
    private static final boolean isEncrypt = true;
    private static RSAPublicKey publicKey = null;//公钥
    private static String strDefaultKey = "G3YYT";
    private Cipher decryptCipher;
    private Cipher encryptCipher;

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    public DesPlus() throws Exception {
        this(strDefaultKey);
    }
    /**
     * 指定密钥构造方法
     *
     * @param strKey
     * 指定的密钥
     * @throws Exception
     */
    public DesPlus(String strKey) throws Exception {
        this.encryptCipher = null;
        this.decryptCipher = null;
        Security.addProvider(new SunJCE());
        Key key = getKey(strKey.getBytes());
        this.encryptCipher = Cipher.getInstance("DES");
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        this.decryptCipher = Cipher.getInstance("DES");
        this.decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param paramArrayOfbyte
     * 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private Key getKey(byte[] paramArrayOfbyte) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrayOfByte = new byte[8];
        // 将原始字节数组转换为8位
        for (byte b = 0; b < paramArrayOfbyte.length && b < arrayOfByte.length; b++){
            arrayOfByte[b] = (byte)paramArrayOfbyte[b];
        }
        // 生成密钥
        Key key = new SecretKeySpec(arrayOfByte, "DES");
        return key;
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStrToByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB
     * 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     * 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArrToHexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
// 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
// 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArrToHexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn
     * 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     * 本方法不处理任何异常，所有异常全部抛出
     * @author hhayyok@163.com
     */
    public static byte[] hexStrToByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
    /**
     * 加密字节数组
     *
     * @param arrB
     * 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }
    /**
     * 解密字节数组
     *
     * @param arrB
     * 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }
    /**
     * 加密字符串
     *
     * @param strIn
     * 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArrToHexStr(encrypt(strIn.getBytes()));
    }
    /**
     * 解密字符串
     *
     * @param strIn
     * 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStrToByteArr(strIn)),"utf-8");
    }
    public String decrypt(String strIn1,String strIn2) throws Exception {
        return new String(decrypt(hexStrToByteArr(strIn1)),strIn2);
    }

    private static Key getKey() throws Exception {
        byte[] arrBTmp = "G3YYT".getBytes();
        byte[] arrB = new byte[8];
        int i = 0;
        while (i < arrBTmp.length && i < arrB.length) {
            arrB[i] = arrBTmp[i];
            i++;
        }
        return new SecretKeySpec(arrB, "DES");
    }
    /*利用RAS公钥加密随机数，返回密文*/
    public static String encryptyRandomKey(RSAPublicKey publicKey, String randomKey){

        String encRandomKey = null;
        try {
            encRandomKey = RSA.encrypt(randomKey, publicKey);
         //   Log.d(TAG, "encryptyRandomKey: 0"+encRandomKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encRandomKey;
    }
    /*给定密钥进行加密*/
    public static String encryptDataByPublicKey(String str, String randomKey)  {
        String encDataByPublicKey = null;
        try {
          //  Log.d(TAG, "encryptDataByPublicKey: 0:");
            Key key=DES.getKey(randomKey);
          //  Log.d(TAG, "encryptDataByPublicKey: 1:"+key.toString());
            encDataByPublicKey = DES.encryptString(key, str);
         //   Log.d(TAG, "encryptDataByPublicKey: 2:"+encDataByPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encDataByPublicKey;
    }

    public static String decrptyDataByPublicKey(String str, String randomKey){
        String decDataByPublicKey= null;
        try {
            decDataByPublicKey = DES.decryptString(DES.getKey(randomKey), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decDataByPublicKey;
    }


}
