package com.wade.mobile.common.contacts.helper;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;


public class LanguageComparator implements Comparator<ContactsRecord> {
    public int compare(ContactsRecord lhs, ContactsRecord rhs) {
        return compare(lhs.getValue(), rhs.getValue());
    }

    public int compare(String ostr1, String ostr2) {
        int i = 0;
        while (i < ostr1.length() && i < ostr2.length()) {
            int codePoint1 = ostr1.charAt(i);
            int codePoint2 = ostr2.charAt(i);
            if (codePoint1 != codePoint2) {
                if (Character.isSupplementaryCodePoint(codePoint1) || Character.isSupplementaryCodePoint(codePoint2)) {
                    return codePoint1 - codePoint2;
                }
                String pinyin1 = pinyin((char) codePoint1);
                String pinyin2 = pinyin((char) codePoint2);
                if (pinyin1 == null || pinyin2 == null) {
                    return codePoint1 - codePoint2;
                }
                if (!pinyin1.equals(pinyin2)) {
                    return pinyin1.compareTo(pinyin2);
                }
            } else if (Character.isSupplementaryCodePoint(codePoint1) || Character.isSupplementaryCodePoint(codePoint2)) {
                i++;
            }
            i++;
        }
        return ostr1.length() - ostr2.length();
    }

    private String pinyin(char c) {
        String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyins == null) {
            return null;
        }
        return pinyins[0];
    }
}