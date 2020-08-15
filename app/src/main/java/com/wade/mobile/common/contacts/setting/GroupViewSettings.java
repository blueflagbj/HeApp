package com.wade.mobile.common.contacts.setting;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class GroupViewSettings implements Parcelable {
    public static final Creator<GroupViewSettings> CREATOR = new Creator<GroupViewSettings>() {
        public GroupViewSettings[] newArray(int size) {
            return new GroupViewSettings[size];
        }

        public GroupViewSettings createFromParcel(Parcel source) {
            return new GroupViewSettings(source, (GroupViewSettings) null);
        }
    };
    private int groupViewGravity;
    private int groupViewbgColor;
    private int textAppearance;
    private int textColor;
    private int textGravity;
    private int textSize;
    private int textViewHeight;
    private int textViewMarginLeft;
    private int textViewMarginRight;

    public GroupViewSettings() {
        this.groupViewbgColor = Color.parseColor("#EAEAEA");
        this.groupViewGravity = 19;
        this.textAppearance = 16842817;
        this.textColor = Color.parseColor("#A6A6A6");
        this.textSize = 8;
        this.textGravity = 19;
        this.textViewHeight = 20;
        this.textViewMarginLeft = 15;
        this.textViewMarginRight = 3;
    }

    private GroupViewSettings(Parcel source) {
        this.groupViewbgColor = source.readInt();
        this.groupViewGravity = source.readInt();
        this.textAppearance = source.readInt();
        this.textColor = source.readInt();
        this.textSize = source.readInt();
        this.textGravity = source.readInt();
        this.textViewHeight = source.readInt();
        this.textViewMarginLeft = source.readInt();
        this.textViewMarginRight = source.readInt();
    }

    /* synthetic */ GroupViewSettings(Parcel parcel, GroupViewSettings groupViewSettings) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.groupViewbgColor);
        dest.writeInt(this.groupViewGravity);
        dest.writeInt(this.textAppearance);
        dest.writeInt(this.textColor);
        dest.writeInt(this.textSize);
        dest.writeInt(this.textGravity);
        dest.writeInt(this.textViewHeight);
        dest.writeInt(this.textViewMarginLeft);
        dest.writeInt(this.textViewMarginRight);
    }

    public int getGroupViewbgColor() {
        return this.groupViewbgColor;
    }

    public void setGroupViewbgColor(int groupViewbgColor2) {
        this.groupViewbgColor = groupViewbgColor2;
    }

    public int getGroupViewGravity() {
        return this.groupViewGravity;
    }

    public void setGroupViewGravity(int groupViewGravity2) {
        this.groupViewGravity = groupViewGravity2;
    }

    public int getTextAppearance() {
        return this.textAppearance;
    }

    public void setTextAppearance(int textAppearance2) {
        this.textAppearance = textAppearance2;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor2) {
        this.textColor = textColor2;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize2) {
        this.textSize = textSize2;
    }

    public int getTextGravity() {
        return this.textGravity;
    }

    public void setTextGravity(int textGravity2) {
        this.textGravity = textGravity2;
    }

    public int getTextViewHeight() {
        return this.textViewHeight;
    }

    public void setTextViewHeight(int textViewHeight2) {
        this.textViewHeight = textViewHeight2;
    }

    public int getTextViewMarginLeft() {
        return this.textViewMarginLeft;
    }

    public void setTextViewMarginLeft(int textViewMarginLeft2) {
        this.textViewMarginLeft = textViewMarginLeft2;
    }

    public int getTextViewMarginRight() {
        return this.textViewMarginRight;
    }

    public void setTextViewMarginRight(int textViewMarginRight2) {
        this.textViewMarginRight = textViewMarginRight2;
    }
}