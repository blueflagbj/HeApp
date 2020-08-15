package com.wade.mobile.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messages {
    public static String ACTION_NOT_SPECIFY;
    public static String CAMERA_CANCELL;
    public static String CAMERA_NOT_COMPLETE;
    public static String CHECK_CONFIG_VIEWMENU;
    public static String CLEAN_CACHE_SUCCESS;
    public static String COMPRESS_ERROR;
    public static String CONFIRM_CLOSE;
    public static String CONN_SERVER_FAILED;
    public static String DIALOG_LOADING;
    public static String DIALOG_TITLE_HINT;
    public static String DOWNLOAD_RES;
    public static String DOWNLOAD_SUCCESS;
    public static String EXCEPTION_CDMA;
    public static String EXCEPTION_CHECK_VERSION;
    public static String EXCEPTION_CONN;
    public static String EXCEPTION_DOWNLOAD;
    public static String EXCEPTION_FILE_SIZE;
    public static String EXCEPTION_GSM;
    public static String EXCEPTION_LOAD_CONFIG;
    public static String EXCEPTION_NETWORK;
    public static String EXCEPTION_PARAM;
    public static String FILE_NOT_EXIST;
    public static String FILE_STREAM_NULL;
    public static String FILE_WRITE_SUCCESS;
    public static String FOUND_NEW_VERSION;
    public static String GALLERY_CANCELL;
    public static String GALLERY_NOT_COMPLETE;
    public static String INSTALL_APK;
    public static String NETWORK_CONFIG;
    public static String NETWORK_CONNECTING;
    public static String NETWORK_ROAM;
    public static String NETWORK_UNSTABLE;
    public static String NO_INFO;
    public static String NO_MATCH_TYPE;
    public static String NO_REGISTER_EVENT;
    public static String NO_TEMPLATE;
    public static String NO_WIFI;
    public static String RES_INIT;
    public static String SDCARD_NOT_EXIST;
    public static String SMS_SUCCESS;
    public static String THREAD_STOP;
    public static String TRY_AGAIN;
    public static String UPDATE_APK;
    public static String UPDATE_CONFIG_URL;
    public static String UPLOAD_CONFIG;
    public static String UPLOAD_SUCCESS;
    private static Pattern pattern;

    static {
        // NLS.initializeMessages("com/wade/mobile/util/Messages", Messages.class.getName());
         NLS.initializeMessages(Messages.class.getSimpleName(), Messages.class.getName());
//com.wade.mobile.util.Messages
    }

    public static final String bind(String s, String... binds) {
        if (pattern == null) {

            pattern = Pattern.compile("%v", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);//34
        }
        if (binds == null || binds.length <= 0) {
            return s;
        }
        StringBuffer buff = new StringBuffer();
        Matcher m = pattern.matcher(s);
        int len = binds.length;
        for (int i = 0; i < len && m.find(); i++) {
            m.appendReplacement(buff, binds[i]);
        }
        m.appendTail(buff);
        return buff.toString();
    }
}