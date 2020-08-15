package com.wade.mobile.util.cipher;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static MD5 MD5 = null;
    private static int bigFileSize = 5120;
    private static byte[] buffer = new byte[512];
    private static char[] hexchar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* renamed from: md */
    private MessageDigest f9615md;

    private MD5() {
        try {
            this.f9615md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new MobileException((Throwable) e);
        }
    }

    private static MD5 getInstance() {
        if (MD5 == null) {
            MD5 = new MD5();
        }
        return MD5;
    }

    public static String hexDigest(byte[] bytes) {
        MessageDigest md = getInstance().f9615md;
        md.update(bytes);
        return byteToHexString(md.digest());
    }

    public static String hexDigest(String str) {
        return hexDigest(str.getBytes());
    }

    public static String hexDigestByFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return hexDigestByFile(file);
    }

    public static String hexDigestByFile(File file) throws Exception {
        FileInputStream fis = null;
        MessageDigest md = getInstance().f9615md;
        String byteToHexString="";

            fis = new FileInputStream(file);
            if (file.length() < ((long) bigFileSize)) {
                while (true) {
                    int length = fis.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    md.update(buffer, 0, length);
                }
            } else {
                md.update(fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
            }
            byteToHexString = byteToHexString(md.digest());

         return byteToHexString;
    }

    private static String byteToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder(digest.length * 2);
        int i = 0;
        while (i < digest.length) {
            sb.append(hexchar[(digest[i] & -16) >>> 4]);
            sb.append(hexchar[digest[i] & 15]);
            i++;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
           // System.out.println(hexDigestByFile(new File("d:/a.txt")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}