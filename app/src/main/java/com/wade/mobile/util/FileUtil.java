package com.wade.mobile.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.ai.cmcchina.multiple.util.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FileUtil {
    public static final String[][] MIME_MapTable;

    static {
        String[] arrayOfString1 = { ".avi", "video/x-msvideo" };
        String[] arrayOfString2 = { ".c", "text/plain" };
        String[] arrayOfString3 = { ".jar", "application/java-archive" };
        String[] arrayOfString4 = { ".log", "text/plain" };
        String[] arrayOfString5 = { ".m4b", "audio/mp4a-latm" };
        String[] arrayOfString6 = { ".m4u", "video/vnd.mpegurl" };
        String[] arrayOfString7 = { ".mp3", "audio/x-mpeg" };
        String[] arrayOfString8 = { ".mpeg", "video/mpeg" };
        String[] arrayOfString9 = { ".mpga", "audio/mpeg" };
        String[] arrayOfString10 = { ".pdf", "application/pdf" };
        String[] arrayOfString11 = { ".ppt", "application/vnd.ms-powerpoint" };
        String[] arrayOfString12 = { ".rmvb", "audio/x-pn-realaudio" };
        String[] arrayOfString13 = { ".tar", "application/x-tar" };
        String[] arrayOfString14 = { ".zip", "application/zip" };
        MIME_MapTable = new String[][] {
                { "", "*/*" }, { ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" }, { ".asf", "video/x-ms-asf" }, arrayOfString1, { ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" }, arrayOfString2, { ".class", "application/octet-stream" }, { ".conf", "text/plain" },
                { ".cpp", "text/plain" }, { ".doc", "application/msword" }, { ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" }, arrayOfString3,
                { ".java", "text/plain" }, { ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, arrayOfString4, { ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, arrayOfString5, { ".m4p", "audio/mp4a-latm" }, arrayOfString6,
                { ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, arrayOfString7, { ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" }, arrayOfString8, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
                arrayOfString9, { ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, arrayOfString10, { ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" }, arrayOfString11, { ".prop", "text/plain" }, { ".rar", "application/x-rar-compressed" }, { ".rc", "text/plain" },
                arrayOfString12, { ".rtf", "application/rtf" }, { ".sh", "text/plain" }, arrayOfString13, { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" },
                { ".xml", "text/plain" }, { ".z", "application/x-compress" }, arrayOfString14 };
    }

    public static void writeFile(String content, OutputStream out) throws Exception {
        writeFile(new ByteArrayInputStream(content.getBytes()), out);
    }

    public static void writeFile(String content, String filePath) throws Exception {
        writeFile(content, filePath, false);
    }

    public static void writeFile(InputStream in, String filePath) throws Exception {
        writeFile(in, filePath, false);
    }

    public static void writeFile(String content, String filePath, boolean append) throws Exception {
        writeFile(new ByteArrayInputStream(content.getBytes()), filePath, append);
    }

    public static void writeFile(InputStream in, String filePath, boolean append) throws Exception {
        writeFile(in, new FileOutputStream(filePath, append));
    }

    public static void writeFile(InputStream in, OutputStream out) throws Exception {
        BufferedOutputStream buffOut = null;
        BufferedInputStream buffIn =new BufferedInputStream(in);
        try{
            buffOut = new BufferedOutputStream(out);
            byte[] bytes = new byte[8192];
            int bytesRead = 0;
            while ((bytesRead =buffIn.read(bytes)) != -1) {
                buffOut.write(bytes, 0,bytesRead);
            }
        } finally {
            if (buffIn != null) {
                buffIn.close();
            }
            if (buffOut != null) {
                buffOut.close();
            }
        }
    }

    public static void writeFile(InputStreamReader reader, OutputStreamWriter writer) throws Exception {
        BufferedReader buffIn = null;
        BufferedWriter buffOut = null;
        try {
            buffIn = new BufferedReader(reader);
            buffOut = new BufferedWriter(writer);
            String readStr ="";
            while( (readStr=buffIn.readLine())!=null)
            {
                buffOut.write(readStr);
            }

        } finally {

            if (buffIn != null) {
            }
            if (buffOut != null) {
            }
        }
    }

    public static String readFile(String fileName) throws Exception {
        return readFile((InputStream) new FileInputStream(fileName));
    }

    public static String readFile(InputStream in) throws Exception {
        return readFile(new InputStreamReader(in));
    }

    public static String readFile(InputStreamReader reader) throws Exception {
        String sb;
        BufferedReader buffIn = null;
        try {
            buffIn = new BufferedReader(reader);
            StringBuilder buff = new StringBuilder();
            String readStr ="";
            while ((readStr=buffIn.readLine())!=null){
                buff.append(readStr);
                buff.append(Constant.LINE_SEPARATOR);
            }
            if (buff.length() < 1) {
                sb = "";
            } else {
                buff.setLength(buff.length() - 1);
                sb = buff.toString();
            }
        } catch (Throwable th2) {

            if (buffIn != null) {
                buffIn.close();
            }
            throw th2;
        }
        return sb;
    }

    public static boolean check(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean createDir(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file == null || !file.exists()) {
            return false;
        }
        return file.delete();
    }

    public static boolean deleteFolder(String path) {
        File folder = new File(path);
        String[] childs = folder.list();
        if (childs != null && childs.length > 0) {
            for (String deleteFolder : childs) {
                if (!deleteFolder(deleteFolder)) {
                    return false;
                }
            }
        }
        return folder.delete();
    }

    public static void openFile(Context context, File f) {
        openFile(context, f, getMIMEType(f));
    }

    public static void openFile(Context context, File f, String type) {
        Intent intent = new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(FileUtils.HIDDEN_PREFIX);
        if (dotIndex < 0) {
            String str = type;
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            String str2 = type;
            return type;
        }
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        String str3 = type;
        return type;
    }

    public static String connectFilePath(String front, String rear) {
        if (front.endsWith(Constant.FILE_SEPARATOR)) {
            if (rear.startsWith(Constant.FILE_SEPARATOR)) {
                return front + rear.substring(1);
            }
            return front + rear;
        } else if (rear.startsWith(Constant.FILE_SEPARATOR)) {
            return front + rear;
        } else {
            return front + Constant.FILE_SEPARATOR + rear;
        }
    }

    public static String connectFilePath(String... paths) {
        if (paths.length < 2) {
            return paths[0];
        }
        StringBuilder result = new StringBuilder();
        String front = paths[0];
        result.append(front);
        for (int i = 1; i < paths.length; i++) {
            String rear = paths[i];
            if (front.endsWith(File.separator)) {
                if (rear.startsWith(File.separator)) {
                    result.append(rear.replace(File.separator, ""));
                } else {
                    result.append(rear);
                }
            } else if (rear.startsWith(File.separator)) {
                result.append(rear);
            } else {
                result.append(File.separator).append(rear);
            }
            front = result.toString();
        }
        return result.toString();
    }
}