package com.wade.mobile.common.contacts.util;

import com.wade.mobile.common.contacts.helper.ContactsRecord;
import com.wade.mobile.common.contacts.helper.HashList;
import com.wade.mobile.common.contacts.helper.ICategorizer;
import com.wade.mobile.common.contacts.helper.LanguageComparator;
import com.wade.mobile.common.contacts.helper.TypeBarView;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PinyinTool {
    public static String getFirstChar(String value) {
        String first;
        char firstChar = value.charAt(0);
        String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);
        if (print == null) {
            if (firstChar >= 'a' && firstChar <= 'z') {
                firstChar = (char) (firstChar - ' ');
            }
            if (firstChar < 'A' || firstChar > 'Z') {
                first = TypeBarView.TYPE_NOTYPE;
            } else {
                first = String.valueOf(firstChar);
            }
        } else {
            first = String.valueOf((char) (print[0].charAt(0) - ' '));
        }
        if (first == null) {
            return TypeBarView.TYPE_NOTYPE;
        }
        return first;
    }

    public static HashList<String, ContactsRecord> categories(List<ContactsRecord> itemList, List<ContactsRecord> noneTypeItemList) {
        HashList<String, ContactsRecord> hashList = new HashList<>(new ICategorizer<String, ContactsRecord>() {
            public String getType(ContactsRecord record) {
                return PinyinTool.getFirstChar(record.getValue());
            }
        });
        if (itemList != null) {
            for (ContactsRecord record : itemList) {
                hashList.add(record);
            }
        }
        int length = hashList.typeSize();
        for (int i = 0; i < length; i++) {
            Collections.sort(hashList.getValues(i), new LanguageComparator());
        }
        if (noneTypeItemList != null) {
            for (ContactsRecord record2 : noneTypeItemList) {
                hashList.add(TypeBarView.TYPE_NONE, record2);
            }
        }
        hashList.sortType(new Comparator<String>() {
            public int compare(String str1, String str2) {
                char c1 = str1.charAt(0);
                char c2 = str2.charAt(0);
                if (TypeBarView.TYPE_NONE.charAt(0) == c1 || TypeBarView.TYPE_NOTYPE.charAt(0) == c2) {
                    return -1;
                }
                if (TypeBarView.TYPE_NONE.charAt(0) == c2 || TypeBarView.TYPE_NOTYPE.charAt(0) == c1) {
                    return 1;
                }
                return c1 - c2;
            }
        });
        return hashList;
    }
}