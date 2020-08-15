package com.wade.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Base64 {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private static final String unicode = "Unicode";

    private static int decode(char paramChar) {
        if (paramChar >= 'A' && paramChar <= 'Z')
            return paramChar - 65;
        if (paramChar >= 'a' && paramChar <= 'z')
            return paramChar - 97 + 26;
        if (paramChar >= '0' && paramChar <= '9')
            return paramChar - 48 + 26 + 26;
        switch (paramChar) {
            default:
                throw new RuntimeException("unexpected code: " + paramChar);
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                break;
        }
        return 0;
    }
    private static void decode(String paramString, OutputStream paramOutputStream) throws IOException {
        int i = 0;
        int j = paramString.length();
        while (true) {
            if (i != j) {
                int k = (decode(paramString.charAt(i)) << 18) + (decode(paramString.charAt(i + 1)) << 12) + (decode(paramString.charAt(i + 2)) << 6) + decode(paramString.charAt(i + 3));
                paramOutputStream.write(k >> 16 & 0xFF);
                if (paramString.charAt(i + 2) != '=') {
                    paramOutputStream.write(k >> 8 & 0xFF);
                    if (paramString.charAt(i + 3) != '=') {
                        paramOutputStream.write(k & 0xFF);
                        i += 4;
                        continue;
                    }
                }
            }
            return;
        }
    }
    public static byte[] decodeByte(String paramString) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            decode(paramString, byteArrayOutputStream);
            byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (IOException iOException) {
                System.err.println("Error while decoding BASE64: " + iOException.toString());
            }
            return arrayOfByte;
        } catch (IOException iOException) {
            throw new RuntimeException();
        }
    }
    public static String decode(String paramString) throws UnsupportedEncodingException {
        return new String(decodeByte(paramString), "Unicode");
    }
    public static String encode(byte[] paramArrayOfbyte) {
        int i = paramArrayOfbyte.length;
        StringBuffer stringBuffer = new StringBuffer(paramArrayOfbyte.length * 3 / 2);
        int j;
        for (j = 0; j <= i - 3; j += 3) {
            int k = (paramArrayOfbyte[j] & 0xFF) << 16 | (paramArrayOfbyte[j + 1] & 0xFF) << 8 | paramArrayOfbyte[j + 2] & 0xFF;
            stringBuffer.append(legalChars[k >> 18 & 0x3F]);
            stringBuffer.append(legalChars[k >> 12 & 0x3F]);
            stringBuffer.append(legalChars[k >> 6 & 0x3F]);
            stringBuffer.append(legalChars[k & 0x3F]);
        }
        if (j == 0 + i - 2) {
            j = (paramArrayOfbyte[j] & 0xFF) << 16 | (paramArrayOfbyte[j + 1] & 0xFF) << 8;
            stringBuffer.append(legalChars[j >> 18 & 0x3F]);
            stringBuffer.append(legalChars[j >> 12 & 0x3F]);
            stringBuffer.append(legalChars[j >> 6 & 0x3F]);
            stringBuffer.append("=");
            return stringBuffer.toString();
        }
        if (j == 0 + i - 1) {
            j = (paramArrayOfbyte[j] & 0xFF) << 16;
            stringBuffer.append(legalChars[j >> 18 & 0x3F]);
            stringBuffer.append(legalChars[j >> 12 & 0x3F]);
            stringBuffer.append("==");
        }
        return stringBuffer.toString();
    }
    @org.jetbrains.annotations.NotNull
    public static String encode(String paramString) throws UnsupportedEncodingException {
        return encode(paramString.getBytes("Unicode"));
    }
    public static void main(String[] paramArrayOfString) throws Exception {
        System.out.println("< >  . .  / \\ ' ' & * ^ @ #");
        String str1 = encode("< >  . .  / \\ ' ' & * ^ @ #");
        System.out.println("=====================================");
        String str2 = decode(str1);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println("=========================================");
    }
}
