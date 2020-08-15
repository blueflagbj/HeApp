package com.wade.mobile.common.contacts.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactsRecord implements Parcelable {
    public static final Creator<ContactsRecord> CREATOR = new Creator<ContactsRecord>() {
        public ContactsRecord[] newArray(int size) {
            return new ContactsRecord[size];
        }

        public ContactsRecord createFromParcel(Parcel source) {
            return new ContactsRecord(source, (ContactsRecord) null);
        }
    };
    private String color;

    /* renamed from: id */
    private int f9612id;
    private String type;
    private String value;

    public ContactsRecord() {
    }

    public ContactsRecord(int id, String value2, String color2, String type2) {
        this.f9612id = id;
        this.value = value2;
        this.color = color2;
        this.type = type2;
    }

    public ContactsRecord(int id, String value2, String color2) {
        this(id, value2, color2, (String) null);
    }

    public ContactsRecord(int id, String value2) {
        this(id, value2, (String) null);
    }

    public String toString() {
        return "ContactsRecord [id=" + this.f9612id + ", value=" + this.value + ", color=" + this.color + ", type=" + this.type + "]";
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public int getId() {
        return this.f9612id;
    }

    public void setId(int id) {
        this.f9612id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color2) {
        this.color = color2;
    }

    private ContactsRecord(Parcel source) {
        this.f9612id = source.readInt();
        this.value = source.readString();
        this.color = source.readString();
        this.type = source.readString();
    }

    /* synthetic */ ContactsRecord(Parcel parcel, ContactsRecord contactsRecord) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.f9612id);
        dest.writeString(this.value);
        dest.writeString(this.color);
        dest.writeString(this.type);
    }
}