package com.wade.mobile.func;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.ailk.common.data.impl.DataMap;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.Constant;
import org.json.JSONArray;

public class MobileContactDetail extends Plugin {
    public MobileContactDetail(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void getContacts(JSONArray param) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/contact");
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case 1:
                if (resultCode == -1) {
                    Uri contactData = data.getData();
                    Cursor c = this.context.managedQuery(contactData, (String[]) null, (String) null, (String[]) null, (String) null);
                    String contactId = contactData.getLastPathSegment();
                    DataMap dataMap = new DataMap();
                    if (c.moveToFirst()) {
                        dataMap.put("name", getContactName(c));
                        dataMap.put("phoneNumber", getContactPhoneNumber(c, contactId));
                        dataMap.put("email", getContactEmail(c, contactId));
                        callback(dataMap.toString());
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private String getContactEmail(Cursor c, String contactId) {
        String email = null;
        Cursor emails = this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, (String[]) null, "contact_id=" + contactId, (String[]) null, (String) null);
        int emailIndex = 0;
        if (emails.getCount() > 0) {
            emailIndex = emails.getColumnIndex("data1");
        }
        while (emails.moveToNext()) {
            email = emails.getString(emailIndex);
            MobileLog.i(this.TAG, email);
        }
        return email;
    }

    private String getContactPhoneNumber(Cursor c, String contactId) {
        String hasPhone;
        String phoneNumber = null;
        if (c.getString(c.getColumnIndex("has_phone_number")).equalsIgnoreCase("1")) {
            hasPhone = Constant.TRUE;
        } else {
            hasPhone = Constant.FALSE;
        }
        if (Boolean.parseBoolean(hasPhone)) {
            Cursor phones = this.context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, "contact_id=" + contactId, (String[]) null, (String) null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex("data1");
            }
            while (phones.moveToNext()) {
                phoneNumber = phones.getString(phoneIndex);
                MobileLog.d(this.TAG, phoneNumber);
            }
            phones.close();
        }
        return phoneNumber;
    }

    private String getContactName(Cursor c) {
        return c.getString(c.getColumnIndex("display_name"));
    }
}