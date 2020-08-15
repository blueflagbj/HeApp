package com.wade.mobile.common.contacts.helper;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class ContactsData implements Parcelable {
    public static final Creator<ContactsData> CREATOR = new Creator<ContactsData>() {
        public ContactsData[] newArray(int size) {
            return new ContactsData[size];
        }

        public ContactsData createFromParcel(Parcel source) {
            return new ContactsData(source, (ContactsData) null);
        }
    };
    private ArrayList<ContactsRecord> itemList;
    private ArrayList<ContactsRecord> noneTypeItemList;
    private String noneTypeText;

    public ContactsData() {
        this.itemList = new ArrayList<>();
        this.noneTypeItemList = new ArrayList<>();
    }

    public boolean addRecord(ContactsRecord record) {
        return this.itemList.add(record);
    }

    public boolean addRecord(int id, String value, String color) {
        return addRecord(new ContactsRecord(id, value, color));
    }

    public boolean addRecord(int id, String value) {
        return addRecord(id, value, (String) null);
    }

    public boolean addNoneTypeRecord(ContactsRecord record) {
        record.setType(TypeBarView.TYPE_NONE);
        return this.noneTypeItemList.add(record);
    }

    public boolean addNoneTypeRecord(int id, String value, String color) {
        return addNoneTypeRecord(new ContactsRecord(id, value, color, TypeBarView.TYPE_NONE));
    }

    public boolean addNoneTypeRecord(int id, String value) {
        return addNoneTypeRecord(id, value, (String) null);
    }

    public ArrayList<ContactsRecord> getItemList() {
        return this.itemList;
    }

    public ArrayList<ContactsRecord> getNoneTypeItemList() {
        return this.noneTypeItemList;
    }

    public String getNoneTypeText() {
        return this.noneTypeText;
    }

    public void setNoneTypeText(String noneTypeText2) {
        this.noneTypeText = noneTypeText2;
    }

    public boolean hasNoneType() {
        return !this.noneTypeItemList.isEmpty();
    }

    private ContactsData(Parcel source) {
        this.noneTypeText = source.readString();
        this.itemList = source.readArrayList(ContactsRecord.class.getClassLoader());
        this.noneTypeItemList = source.readArrayList(ContactsData.class.getClassLoader());
    }

    /* synthetic */ ContactsData(Parcel parcel, ContactsData contactsData) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.noneTypeText);
        dest.writeList(this.itemList);
        dest.writeList(this.noneTypeItemList);
    }
}