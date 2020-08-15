package com.ai.cmcchina.crm.constant;


/* renamed from: com.ai.cmcchina.crm.constant.Constant */
public final class Constant {
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