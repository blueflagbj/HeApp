package com.ai.cmcchina.crm.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.ai.cmcchina.crm.bean.Host;
import com.ai.cmcchina.multiple.util.FileUtils;
import com.wade.mobile.util.Constant;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Global {
    public static final String APP_MD5;
    public static final String BROADBAND = "broadband";
    public static final String COMPLETE_DATE = "complete_date";
    public static final String COMPLETE_VILLAGE_LIST = "complete_village_list";
    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_3 = "yyyy-MM";
    public static final String DISTRICT_NAME = "district_name";
    public static final String GIS_HOST;

    /* renamed from: ID */
    public static final String f943ID = "id";
    public static final String NAME = "name";
    public static final String OPEN_PAGE_WAY = "open_page_way";
    public static final String REAL_NAME_AUTH_DOWNLOAD_URL = "/down/apk/smrz_nfc.apk";
    public static final String REAL_NAME_AUTH_HOST = "http://smrz.realnameonline.cn:30202";
    public static final String REGEX_TELEPHONE = "^(1([3-9][0-9]))\\d{8}$";
    public static final String SERVICE_PASSWORD = "0";
    public static final String SESSION_ID = "session_id";
    public static final int SESSION_LOOP_TIME = 480000;
    public static final String SMS_CODE = "1";
    @SuppressLint({"SimpleDateFormat"})
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /* renamed from: com.ai.cmcchina.crm.util.Global$AlipayBillOrderCode */
    public static class AlipayBillOrderCode {
        public static final String EXECUTE_ERROR_CODE = "9999";
        public static final String EXECUTE_SUCCESS_NULL_CODE = "0000";
        public static final String REMOTE_TIME_OUT_CODE = "9988";
        public static final String SERVER_ERROR_CODE = "-1";
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$ErrorCode */
    public static class ErrorCode {
        public static final String OTHER_CODE = "g3_9999";
        public static final String SEVER_DATA_ERROR = "g3_500";
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$MainViceCard */
    public static class MainViceCard {
        public static final String CARD_TYPE = "card_type";
        public static final String CARE_CARD = "care_card";
        public static final String CONCENTRIC_CARD = "concentric_card";
        public static final String CONCENTRIC_CARD_OTHER = "concentric_card_other";
        public static final String FAMILY_CARD = "family_card";
        public static final String ICE_CARD = "ice_card";
        public static final String MAIN_CARD_NUM = "main_card_num";
        public static final String MAIN_VICE_CARD_BUSI_INFO = "main_vice_card_busiInfo";
        public static final String VICE_CARD_NUM = "vice_card_num";
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$Msg */
    public static class Msg {
        public static final int ADD_NEW_PAGE_AT = 123;
        public static final int ALERT = 121;
        public static final int CHECK_UPDATE = 109;
        public static final int CHECK_UPDATE_FINISHED = 102;
        public static final int CLICK = 125;
        public static final int CLIENT = 108;
        public static final int CONFIRM = 124;
        public static final int CONFIRM_UPDATE = 111;
        public static final int DELAYED = 104;
        public static final int DOWNLOAD_BREAK = 116;
        public static final int DOWNLOAD_COMPLETE = 114;
        public static final int EXIT = 127;
        public static final int FRAGMENT_ADDED = 120;
        public static final int HOME_DATA_ADD = 118;
        public static final int HOME_DATA_DELETE = 119;
        public static final int INIT_PROGRESS = 112;
        public static final int INSTALL = 115;
        public static final int NETWORK_DISABLE = 128;
        public static final int NETWORK_UNAVAILABLE = 105;
        public static final int NEW_VERSION = 101;
        public static final int READY_FOR_UPDATE = 110;
        public static final int SESSION_TERMINATED = 107;
        public static final int SESSION_THREAD = 106;
        public static final int SHOW_FAVORITE_MANAGEMENT = 122;
        public static final int SHOW_UPGRADE2PAD = 126;
        public static final int TOAST = 103;
        public static final int UPDATE_PROGRESS = 113;
        public static final int WEB_BACK = 117;
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$PrefDefault */
    public static class PrefDefault {
        public static final String MAP_RADIUS = "2";
        public static final int PAGE_SIZE = 20;
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$PrefKey */
    public static class PrefKey {
        public static final String LAST_REFRESH = "last_refresh";
        public static final String PAGE_SIZE = "pageSize";
        public static final String PASSWORD = "login_password";
        public static final String REMEMBER_PASSWORD = "rem_pwd";
        public static final String STAFF_NUMBER = "login_staff_number";
        public static final String USER_NAME = "user_name";
        public static final String USER_TELEPHONE = "user_telephone";
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$ReturnCode */
    public static class ReturnCode {
        public static final String SUCCESS = "0000";
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$UserStatus */
    public static class UserStatus {
        public static final String NORMAL = "正常";
    }

    static {
        Host host = new Host();
        APP_MD5 = host.getMD5();
        GIS_HOST = host.getGisHost();
    }

    public static String object2Base64(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(baos).writeObject(obj);
            return Base64.encodeToString(baos.toByteArray(), 0);
        } catch (IOException e) {
            Log.e(TAG, "object2Base64: ",e );
            return null;
        }
    }

    public static Object base64ToObject(String base64Str) {
        if (base64Str != null && !"".equals(base64Str)) {
            try {
                return new ObjectInputStream(new ByteArrayInputStream(Base64.decode(base64Str, 0))).readObject();
            } catch (StreamCorruptedException e) {
                Log.e(TAG, "base64ToObject: ",e );
            } catch (IOException e2) {
                Log.e(TAG, "base64ToObject: ",e2);
            } catch (ClassNotFoundException e3) {
                Log.e(TAG, "base64ToObject: ",e3 );
            }
        }
        return null;
    }

    public static String code2City(String code) {
        if ("A".equalsIgnoreCase(code)) {
            return "郑州";
        }
        if ("F".equalsIgnoreCase(code)) {
            return "鹤壁";
        }
        if ("K".equalsIgnoreCase(code)) {
            return "许昌";
        }
        if ("L".equalsIgnoreCase(code)) {
            return "漯河";
        }
        if ("G".equalsIgnoreCase(code)) {
            return "新乡";
        }
        if ("B".equalsIgnoreCase(code)) {
            return "开封";
        }
        if ("C".equalsIgnoreCase(code)) {
            return "洛阳";
        }
        if ("Q".equalsIgnoreCase(code)) {
            return "驻马店";
        }
        if ("E".equalsIgnoreCase(code)) {
            return "安阳";
        }
        if ("H".equalsIgnoreCase(code)) {
            return "焦作";
        }
        if ("R".equalsIgnoreCase(code)) {
            return "南阳";
        }
        if ("D".equalsIgnoreCase(code)) {
            return "平顶山";
        }
        if ("N".equalsIgnoreCase(code)) {
            return "商丘";
        }
        if ("S".equalsIgnoreCase(code)) {
            return "信阳";
        }
        if ("J".equalsIgnoreCase(code)) {
            return "濮阳";
        }
        if ("P".equalsIgnoreCase(code)) {
            return "周口";
        }
        if ("M".equalsIgnoreCase(code)) {
            return "三门峡";
        }
        if ("U".equalsIgnoreCase(code)) {
            return "济源";
        }
        return "";
    }

    public static String getZoneCode(String regionId) {
        if ("A".equalsIgnoreCase(regionId)) {
            return "371";
        }
        if ("F".equalsIgnoreCase(regionId)) {
            return "392";
        }
        if ("K".equalsIgnoreCase(regionId)) {
            return "374";
        }
        if ("L".equalsIgnoreCase(regionId)) {
            return "395";
        }
        if ("G".equalsIgnoreCase(regionId)) {
            return "373";
        }
        if ("B".equalsIgnoreCase(regionId)) {
            return "378";
        }
        if ("C".equalsIgnoreCase(regionId)) {
            return "379";
        }
        if ("Q".equalsIgnoreCase(regionId)) {
            return "396";
        }
        if ("E".equalsIgnoreCase(regionId)) {
            return "372";
        }
        if ("H".equalsIgnoreCase(regionId)) {
            return "391";
        }
        if ("R".equalsIgnoreCase(regionId)) {
            return "377";
        }
        if ("D".equalsIgnoreCase(regionId)) {
            return "375";
        }
        if ("N".equalsIgnoreCase(regionId)) {
            return "370";
        }
        if ("S".equalsIgnoreCase(regionId)) {
            return "376";
        }
        if ("J".equalsIgnoreCase(regionId)) {
            return "393";
        }
        if ("P".equalsIgnoreCase(regionId)) {
            return "394";
        }
        if ("M".equalsIgnoreCase(regionId)) {
            return "398";
        }
        if ("U".equalsIgnoreCase(regionId)) {
            return "391";
        }
        return "";
    }

    public static String fileInAssets2String(Context context, String fileName) {
        try {
            return new String(readStream(context.getAssets().open(fileName)));
        } catch (IOException e) {
            Log.e(TAG, "fileInAssets2String: ",e);
            return "";
        }
    }

    public static byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            int len = inStream.read(buffer);
            if (len != -1) {
                outSteam.write(buffer, 0, len);
            } else {
                outSteam.close();
                inStream.close();
                return outSteam.toByteArray();
            }
        }
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$MoneyUtil */
    public static class MoneyUtil {
        private static final String[] DUNIT = {"角", "分", "厘"};
        private static final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
        private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

        public static String toChinese(String str) {
            String integerStr;
            String decimalStr;
            String str2 = str.replaceAll(Constant.PARAMS_SQE, "");
            if (str2.indexOf(FileUtils.HIDDEN_PREFIX) > 0) {
                integerStr = str2.substring(0, str2.indexOf(FileUtils.HIDDEN_PREFIX));
                decimalStr = str2.substring(str2.indexOf(FileUtils.HIDDEN_PREFIX) + 1);
            } else if (str2.indexOf(FileUtils.HIDDEN_PREFIX) == 0) {
                integerStr = "";
                decimalStr = str2.substring(1);
            } else {
                integerStr = str2;
                decimalStr = "";
            }
            if (!integerStr.equals("")) {
                integerStr = Long.toString(Long.parseLong(integerStr));
                if (integerStr.equals(Global.SERVICE_PASSWORD)) {
                    integerStr = "";
                }
            }
            if (integerStr.length() > IUNIT.length) {
                return str2;
            }
            return getChineseInteger(toArray(integerStr), isMust5(integerStr)) + getChineseDecimal(toArray(decimalStr));
        }

        private static int[] toArray(String number) {
            int[] array = new int[number.length()];
            for (int i = 0; i < number.length(); i++) {
                array[i] = Integer.parseInt(number.substring(i, i + 1));
            }
            return array;
        }

        private static String getChineseInteger(int[] integers, boolean isMust5) {
            StringBuffer chineseInteger = new StringBuffer("");
            int length = integers.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    String key = "";
                    if (integers[i] == 0) {
                        if (length - i == 13) {
                            key = IUNIT[4];
                        } else if (length - i == 9) {
                            key = IUNIT[8];
                        } else if (length - i == 5 && isMust5) {
                            key = IUNIT[4];
                        } else if (length - i == 1) {
                            key = IUNIT[0];
                        }
                        if (length - i > 1 && integers[i + 1] != 0) {
                            key = key + NUMBERS[0];
                        }
                    }
                    if (integers[i] != 0) {
                        key = NUMBERS[integers[i]] + IUNIT[(length - i) - 1];
                    }
                    chineseInteger.append(key);
                }
            } else {
                chineseInteger.append(NUMBERS[0] + IUNIT[0]);
            }
            return chineseInteger.toString();
        }

        private static String getChineseDecimal(int[] decimals) {
            StringBuffer chineseDecimal = new StringBuffer("");
            int i = 0;
            while (i < decimals.length && i != 3) {
                chineseDecimal.append(decimals[i] == 0 ? "" : NUMBERS[decimals[i]] + DUNIT[i]);
                i++;
            }
            return chineseDecimal.toString();
        }

        private static boolean isMust5(String integerStr) {
            String subInteger;
            int length = integerStr.length();
            if (length <= 4) {
                return false;
            }
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            if (Integer.parseInt(subInteger) > 0) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: com.ai.cmcchina.crm.util.Global$Broadband */
    public static class Broadband {
        public static final String BASIC_PRODUCT = "1";
        public static final String BASIC_PRODUCT_FLAG = "basic_product";
        public static final String MARKET_PRODUCT = "2";
        public static final String MARKET_PRODUCT_FLAG = "market_product";
        public static final String MARKET_PRODUCT_LIST_FLAG = "market_product_list";
        public static final String MODEM_COST_INFO_FLAG = "modem_cost_info";
        public static final String MODEM_PRODUCT = "2";
        public static final String MODEM_PRODUCT_FLAG = "modem_product";
        public static final String MORE_PEOPLE_PRODUCT_FLAG = "more_people_product";
        public static final Map<String, String> PRODUCT_TYPE = new HashMap();
        public static final String SELECTED_COST_FLAG = "selected_cost_flag";
        public static final String SET_TOP_BOX_COST_FLAG = "set_top_box_cost_info";
        public static final String SET_TOP_BOX_FLAG = "set_top_box_product";
        public static final String TV_PRODUCT_FLAG = "tv_product";
        public static final String TV_PRODUCT_VALUE_ADD_FLAG = "tv_value_add_product";
        public static final String USER_BROADBAND_INFO_FLAG = "user_broadband_info";
        public static final String VALUE_ADD_PRODUCT = "value_add_product";

        static {
            PRODUCT_TYPE.put("OFFER_PLAN", "基本策划");
            PRODUCT_TYPE.put("OFFER_PLAN_HOME", "基本策划(家园卡)");
            PRODUCT_TYPE.put("OFFER_COMPOSITE", "组合策划");
            PRODUCT_TYPE.put("OFFER_APPORT", "分摊预存");
            PRODUCT_TYPE.put("OFFER_RENT", "专项月租");
            PRODUCT_TYPE.put("OFFER_PROMOTION", "增值产品");
            PRODUCT_TYPE.put("OFFER_PLOY", "营销活动");
            PRODUCT_TYPE.put("OFFER_FAMILY", "全球通");
            PRODUCT_TYPE.put("OFFER_OTHER", "其他增值产品");
            PRODUCT_TYPE.put("OFFER_PBOSS", "PBOSS策划");
            PRODUCT_TYPE.put("OFFER_REMIND", "话费提醒");
        }
    }

    public static int getAge(String strDate) {
        try {
            Date birthDay = sdf.parse(strDate);
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDay);
            if (birth.after(now)) {
                return 0;
            }else{
                int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                    return age - 1;
                }
                return age;
            }
        } catch (Exception e) {
            return 0;
        }
    }

}
