package com.wade.mobile.common.contacts.setting;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactsSettings implements Parcelable {
    public static final Creator<ContactsSettings> CREATOR = new Creator<ContactsSettings>() {
        public ContactsSettings[] newArray(int size) {
            return new ContactsSettings[size];
        }

        public ContactsSettings createFromParcel(Parcel source) {
            return new ContactsSettings(source, (ContactsSettings) null);
        }
    };
    private ChildViewSettings childViewSettings;
    private GroupViewSettings groupViewSettings;
    private TypeBarViewSettings typeBarViewSettings;

    public ContactsSettings() {
        this.groupViewSettings = new GroupViewSettings();
        this.childViewSettings = new ChildViewSettings();
        this.typeBarViewSettings = new TypeBarViewSettings();
    }

    public GroupViewSettings getGroupViewSettings() {
        return this.groupViewSettings;
    }

    public ChildViewSettings getChildViewSettings() {
        return this.childViewSettings;
    }

    public TypeBarViewSettings getTypeBarViewSettings() {
        return this.typeBarViewSettings;
    }

    private ContactsSettings(Parcel source) {
        this.groupViewSettings = (GroupViewSettings) source.readParcelable(GroupViewSettings.class.getClassLoader());
        this.childViewSettings = (ChildViewSettings) source.readParcelable(ChildViewSettings.class.getClassLoader());
        this.typeBarViewSettings = (TypeBarViewSettings) source.readParcelable(TypeBarViewSettings.class.getClassLoader());
    }

    /* synthetic */ ContactsSettings(Parcel parcel, ContactsSettings contactsSettings) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.groupViewSettings, 1);
        dest.writeParcelable(this.childViewSettings, 1);
        dest.writeParcelable(this.typeBarViewSettings, 1);
    }
}