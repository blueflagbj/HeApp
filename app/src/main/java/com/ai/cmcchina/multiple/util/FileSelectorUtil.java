package com.ai.cmcchina.multiple.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.wade.mobile.util.Constant;
import java.io.File;


/* renamed from: com.ai.cmcchina.multiple.util.FileSelectorUtil */
public class FileSelectorUtil {
    public static final int FILE_SELECT_IMG_COMPRESS_REQUEST = 18072;
    public static final int FILE_SELECT_REQUEST = 18071;
    private static final String TAG = "FileSelectorUtil>>>";

    public static String getFilePath(Context context, Intent data) {
        String path;
       // G3Logger.debug("getFilePath>>>");
        try {
            Uri uri = data.getData();
            if (Build.VERSION.SDK_INT > 19) {
                path = getPath(context, uri);
            } else {
                path = getRealPathFromURI(uri, context);
            }
        } catch (Exception e) {
//            G3Logger.debug("getFilePath>>>Exception");
//            G3Logger.debug(e);
            e.printStackTrace();
            path = "";
        }
       // G3Logger.debug("getFilePath>>>path = " + path);
        return path;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        String res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        cursor.close();
        return res;
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) throws Exception {
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, (String) null, (String[]) null);
            }
            if (Constant.TYPE_FILE.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() +  "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            String id = DocumentsContract.getDocumentId(uri);
            String docId = DocumentsContract.getDocumentId(uri);
            if (docId.startsWith("raw:")) {
                return docId.replaceFirst("raw:", "");
            }
            String[] contentUriPrefixes = {"content://downloads/public_downloads", "content://downloads/my_downloads", "content://downloads/all_downloads"};
            int length = contentUriPrefixes.length;
            int i = 0;
            while (i < length) {
                try {
                    String path = getDataColumn(context, ContentUris.withAppendedId(Uri.parse(contentUriPrefixes[i]), Long.valueOf(id).longValue()), (String) null, (String[]) null);
                    if (path != null) {
                       // G3Logger.debug("isDownloadsDocument(uri)>>>获取地址成功---" + path);
                        return path;
                    }
                    i++;
                } catch (Exception e) {
//                    G3Logger.debug("isDownloadsDocument(uri)>>>getDataColumn 获取失败！！！");
//                    G3Logger.debug(e);
                }
            }
          //  G3Logger.debug("isDownloadsDocument(uri)>>>FileUtils获取地址");
            File file = FileUtils.generateFileName(FileUtils.getFileName(context, uri), FileUtils.getDocumentCacheDir(context));
            String destinationPath = null;
            if (file != null) {
                destinationPath = file.getAbsolutePath();
                FileUtils.saveFileFromUri(context, uri, destinationPath);
            }
           // G3Logger.debug("isDownloadsDocument(uri)--FileUtils获取地址>>>" + destinationPath);
            return destinationPath;
        } else if (isMediaDocument(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String type = split2[0];
            Uri contentUri = null;
            if (Constant.TYPE_IMAGE.equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if (Constant.TYPE_VIDEO.equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if (Constant.TYPE_AUDIO.equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, contentUri, "_id=?", new String[]{split2[1]});
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) throws Exception {
        // G3Logger.debug("FileSelectorUtil>>>getDataColumn>>>");
        String returnStr=null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, (String) null);
            if (cursor == null) {
                returnStr=null;
            } else if(!cursor.moveToFirst()){
            returnStr=null;
            }else {
               returnStr = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            }

        } catch (Exception e) {
//            G3Logger.debug("FileSelectorUtil>>>getDataColumn>>Exception");
//            G3Logger.debug(e);
           e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            return returnStr;
        }

    }

    public static boolean isExternalStorageDocument(Uri uri) throws Exception {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) throws Exception {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) throws Exception {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}