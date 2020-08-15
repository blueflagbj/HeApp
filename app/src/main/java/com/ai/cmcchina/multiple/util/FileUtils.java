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

import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.wade.mobile.util.Constant;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


public class FileUtils {
    public static final String AUTHORITY = "YOUR_AUTHORITY.provider";
    private static final boolean DEBUG = false;
    public static final String DOCUMENTS_DIR = "documents";
    public static final String HIDDEN_PREFIX = ".";
    static final String TAG = "FileUtils--";

    private FileUtils() {
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }
        int dot = uri.lastIndexOf(HIDDEN_PREFIX);
        if (dot >= 0) {
            return uri.substring(dot);
        }
        return "";
    }

    public static boolean isLocal(String url) {
        return url != null && !url.startsWith(Constant.START_HTTP) && !url.startsWith(Constant.START_HTTPS);
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File getPathWithoutFilename(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return file;
        }
        String filename = file.getName();
        String filepath = file.getAbsolutePath();
        String pathwithoutname = filepath.substring(0, filepath.length() - filename.length());
        if (pathwithoutname.endsWith("/")) {
            pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
        }
        return new File(pathwithoutname);
    }

    public static String getMimeType(File file) {
        String extension = getExtension(file.getName());
        if (extension.length() > 0) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }
        return "application/octet-stream";
    }

    public static String getMimeType(Context context, String url) {
        String type = context.getContentResolver().getType(Uri.parse(url));
        if (type == null) {
            return "application/octet-stream";
        }
        return type;
    }

    public static boolean isLocalStorageDocument(Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getPath(Context context, Uri uri) {
        String absolutePath = getLocalPath(context, uri);
        return absolutePath != null ? absolutePath : uri.toString();
    }

    @SuppressLint({"NewApi"})
    private static String getLocalPath(Context context, Uri uri) {
        Log.d(TAG, "getLocalPath: FileUtils-- File -Authority: " + uri.getAuthority() + ", Fragment: " + uri.getFragment() + ", Port: " + uri.getPort() + ", Query: " + uri.getQuery() + ", Scheme: " + uri.getScheme() + ", Host: " + uri.getHost() + ", Segments: " + uri.getPathSegments().toString());
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, (String) null, (String[]) null);
            } else if (Constant.TYPE_FILE.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isLocalStorageDocument(uri)) {
            return DocumentsContract.getDocumentId(uri);
        } else {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(null) +"/" + split[1];
                }
                if ("home".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(null) + "/documents/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }
                String[] contentUriPrefixesToTry = {"content://downloads/public_downloads", "content://downloads/my_downloads"};
                int length = contentUriPrefixesToTry.length;
                int i = 0;
                while (i < length) {
                    try {
                        String path = getDataColumn(context, ContentUris.withAppendedId(Uri.parse(contentUriPrefixesToTry[i]), Long.valueOf(id).longValue()), (String) null, (String[]) null);
                        if (path != null) {
                            return path;
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                File file = generateFileName(getFileName(context, uri), getDocumentCacheDir(context));
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }
                return destinationPath;
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String type2 = split2[0];
                Uri contentUri = null;
                if (Constant.TYPE_IMAGE.equals(type2)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (Constant.TYPE_VIDEO.equals(type2)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (Constant.TYPE_AUDIO.equals(type2)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, contentUri, "_id=?", new String[]{split2[1]});
            }
        }
        return null;
    }

    public static String getReadableFileSize(int size) {
        DecimalFormat dec = new DecimalFormat("###.#");
        float fileSize = 0.0f;
        String suffix = " KB";
        if (size > 1024) {
            fileSize = (float) (size / 1024);
            if (fileSize > 1024.0f) {
                fileSize /= 1024.0f;
                if (fileSize > 1024.0f) {
                    fileSize /= 1024.0f;
                    suffix = " GB";
                } else {
                    suffix = " MB";
                }
            }
        }
        return String.valueOf(dec.format((double) fileSize) + suffix);
    }

    public static Intent createGetContentIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        return intent;
    }

    public static Intent getViewIntent(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, file);
        Intent intent = new Intent("android.intent.action.VIEW");
        String url = file.toString();
        if (url.contains(".doc") || url.contains(".docx")) {
            intent.setDataAndType(uri, "application/msword");
        } else if (url.contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.contains(".ppt") || url.contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.contains(".xls") || url.contains(".xlsx")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.contains(".zip") || url.contains(".rar")) {
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.contains(".rtf")) {
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.contains(".wav") || url.contains(".mp3")) {
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.contains(".gif")) {
            intent.setDataAndType(uri, "image/gif");
        } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.contains(".txt")) {
            intent.setDataAndType(uri, "text/plain");
        } else if (url.contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

/*    public static File getDownloadsDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }*/

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);
        return dir;
    }

    private static void logDir(File dir) {
        Log.d(TAG, "FileUtils--Dir=" + dir);

        File[] files = dir.listFiles();
        int length = files.length;
        for (int i = 0; i < length; i++) {
            Log.d(TAG, "FileUtils--File=" + files[i].getPath());
        }
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }
        File file = new File(directory, name);
        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf(46);
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }
            int index = 0;
            while (file.exists()) {
                index++;
                file = new File(directory, fileName + '(' + index + ')' + extension);
            }
        }
        try {
            if (!file.createNewFile()) {
                return null;
            }
            logDir(directory);
            return file;
        } catch (IOException e) {
            Log.d(TAG, "FileUtils--generateFileName IOException");
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }

    public static File createTempImageFile(Context context, String fileName) throws IOException {
        return File.createTempFile(fileName, ".jpg", new File(context.getCacheDir(), DOCUMENTS_DIR));
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        if (context.getContentResolver().getType(uri) != null || context == null) {
            Cursor returnCursor = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
            if (returnCursor == null) {
                return null;
            }
            int nameIndex = returnCursor.getColumnIndex("_display_name");
            returnCursor.moveToFirst();
            String filename = returnCursor.getString(nameIndex);
            returnCursor.close();
            return filename;
        }
        String path = getPath(context, uri);
        if (path == null) {
            return getName(uri.toString());
        }
        return new File(path).getName();
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(47) + 1);
    }

}
