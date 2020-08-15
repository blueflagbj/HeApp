package com.ai.cmcchina.crm.util;

public class FormUtil {
    public static final String IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
    public static final String REGEX_NUMBER = "^[1-9]\\d*$";
    public static final String REGEX_TELEPHONE = "^(1([3-9][0-9]))\\d{8}$";

    public static boolean isTelephone(CharSequence resource) {
        return resource.toString().matches("^(1([3-9][0-9]))\\d{8}$");
    }

    public static boolean isNumber(CharSequence resource) {
        return resource.toString().matches("^[1-9]\\d*$");
    }

    public static boolean isIDCard(CharSequence resource) {
        return resource.toString().matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
    }
}