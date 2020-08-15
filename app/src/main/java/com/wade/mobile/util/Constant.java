package com.wade.mobile.util;

import java.io.File;

public class Constant {
    public static final String ATTR_CLASS = "class";
    public static final String ATTR_ENCRYPT = "encrypt";
    public static final String ATTR_ID = "id";
    public static final String ATTR_METHOD = "method";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_TEXT = "text";
    public static final String ATTR_VALUE = "value";
    public static final String ATTR_VISIBLE = "visible";
    public static final String DRAWABLE = "drawable";
    public static final String FALSE = "false";
    public static final String FILE_SEPARATOR = File.separator;
    public static final String GBK = "GBK";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String IPU_GLOBAL_CONFIG = "assets/ipu.global.properties";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String LAYOUT = "layout";
    public static final String LINE_SEPARATOR = System.getProperties().getProperty("line.separator");
    public static final String MINE_TYPE_HTML = "text/html";
    public static final String PARAMS_SQE = ",";
    public static final String PATH_AUDIO = "audios";
    public static final String PATH_FILE = "files";
    public static final String PATH_IMAGE = "images";
    public static final String PATH_VIDEO = "videos";
    public static final String SEPARATOR_UNDERLINE = "_";
    public static final String START_FILE = "file://";
    public static final String START_HTTP = "http://";
    public static final String START_HTTPS = "https://";
    public static final String TRUE = "true";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String UTF_8 = "UTF-8";
    public static final String APP_LOGIN_TYPE = "gesture_password_login_type";
    public static final String CRM_APP_ID = "raw/crmApp";
    public static final String ERRDESC = "ERRDESC";
    public static final String ESOP_APP = "ESOP";
    public static final String ESOP_APP_ID = "esopApp";
    public static final String FromWhichSubsystem = "FromWhichSubsystem";
    public static final String GESTURE_PASSWORD = "gesture_password";
    public static final String GESTURE_PASSWORD_TABLE = "gesture_password_table";
    public static final String LOGIN_TYPE_PARTNER_CHANNEL = "loginTypePartnerChannle";
    public static final String LOGIN_TYPE_SELF_SUPPORT_CHANNEL = "loginTypeSelfSupportChannel";
    public static final String LOGIN_TYPE_SOCIETY_CHANNEL = "loginTypeSocietyChannel";
    public static final String PARTNER_APP_ID = "marketingApp";
    public static final String RETURN_CODE_KEY = "returnCode";
    public static final String RETURN_MESSAGE_KEY = "returnMsg";
    public static final String SESSION_ID = "SESSION_ID";


    public static final class Broadcast {
        public static final String EXIT_APP_ACTION = "EXIT_APP_ACTION";
    }

    public static final class Direction {
        public static final int SANDBOX = 0;
        public static final int SDCARD = 1;
    }

    public static final class DownloadFileActivity {
        public static final String DOWNLOAD_PARAM = "DOWNLOAD_PARAM";
        public static final String FILE_PATH = "FILE_PATH";
    }

    public static final class Function {
        public static final String checkNetWork = "checkNetWork";
        public static final String close = "close";
        public static final String loadingStop = "loadingStop";
        public static final String openNetWorkView = "openNetWorkView";
        public static final String openUrl = "openUrl";
    }

    public static final class MobileCache {
        public static final String APP_RECORD = "APP_RECORD";
        public static final String DYNAMIC_PLUGIN_VERSION = "DYNAMIC_PLUGIN_VERSION";
        public static final String LOCAL_RES_VERSION = "LOCAL_RES_VERSION";
        public static final String SERVER_CONFIG = "SERVER_CONFIG";
        public static final String SERVER_DATA_CONFIG = "SERVER_DATA_CONFIG";
        public static final String SERVER_PAGE_CONFIG = "SERVER_PAGE_CONFIG";
        public static final String WADE_MOBILE_ACTION = "WADE_MOBILE_ACTION";
        public static final String WADE_MOBILE_CONFIG = "WADE_MOBILE_CONFIG";
        public static final String WADE_MOBILE_STORAGE = "WADE_MOBILE_STORAGE";
    }

    public static final class MobileSecurity {
        public static final String RES_KEY_ACTION = "getResKey";
    }

    public static final class MobileWebCacheDB {
        public static final String CACHE_TABLE = "cache";
        public static final String DB_NAME = "webviewCache.db";
        public static final String FILEPATH_COLUMN = "filepath";
        public static final String LASTMODIFY_COLUMN = "lastmodify";
        public static final String SELECT_CACHE_SQL = "select * from cache where url = ?";
        public static final String URL_COLUMN = "url";
    }

    public static final class Plugin {
        public static final String NULL = "null";
    }

    public static final class Server {
        public static final String ACTION = "action";
        public static final String DATA = "data";
        public static final String IS_APP = "isApp";
        public static final String KEY = "key";
        public static final String MOBILE = "Mobile";
        public static final String RES_RELATION_CONFIG = "res.relation.properties";
        public static final String RES_VERSION_CONFIG = "res.version.properties";
    }

    public static final class ServerConfig {
        public static final String CLIENT_VERSION = "clientVersion";
        public static final String FILE_ENCRYPT = "fileEncrypt";
        public static final String IS_FORCE_UPDATE = "isForceUpdate";
        public static final String IS_USE_TAG = "isUseTag";
        public static final String PRODUCT_MODE = "productMode";
        public static final String RESOURCE_VERSION = "resourceVersion";
    }

    public static final class Version {
        public static final String VERSION_ACTION = "getVersion";
    }

    public static final class WadeMobileActivity {
        public static final String ICON = "icon";
    }
    /* renamed from: com.ai.cmcchina.crm.constant.Constant$ReturnCode */
    public static class ReturnCode {
        public static final String FAIL = "9999";
        public static final String SUCCESS = "0000";
    }

    /* renamed from: com.ai.cmcchina.crm.constant.Constant$AlipayBusiType */
    public enum AlipayBusiType {
        OPEN_ACCOUNT("1"),
        RENEW_CARD("2"),
        BROADBAND("3"),
        SOCIAL_CHANNEL_OPEN_ACCOUNT("4"),
        SOCIAL_CHANNEL_BROADBAND("5"),
        MARKETING("6"),
        MOVE_BROADBAND("7"),
        SUBSCRIBE_HD_TV("8");

        private String type;

        private AlipayBusiType(String type2) {
            this.type = type2;
        }

        public String getType() {
            return this.type;
        }
    }

    /* renamed from: com.ai.cmcchina.crm.constant.Constant$PartnerChannlRole */
    public enum PartnerChannlRole {
        ADMIN("admin"),
        MANAGER("manager"),
        PARTNER("partner"),
        PARTNER_SUB_ACCOUNT("partnerSubAccount");

        private String type;

        private PartnerChannlRole(String type2) {
            this.type = type2;
        }

        public String getType() {
            return this.type;
        }
    }
}
