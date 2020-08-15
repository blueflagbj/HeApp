package com.wade.mobile.common.contacts.setting;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import com.wade.mobile.common.contacts.util.ContactsConstant;

public class ChildViewSettings implements Parcelable {
    public static final Creator<ChildViewSettings> CREATOR = new Creator<ChildViewSettings>() {
        public ChildViewSettings[] newArray(int size) {
            return new ChildViewSettings[size];
        }

        public ChildViewSettings createFromParcel(Parcel source) {
            return new ChildViewSettings(source, (ChildViewSettings) null);
        }
    };
    private int childViewGravity;
    private int childViewHeight;
    private int childViewNormalBgColor;
    private int childViewOrientation;
    private int childViewPressedBgColor;
    private int childViewStyle;
    private int dividerColor;
    private int dividerHeight;
    private int dividerMarginLeft;
    private int dividerMarginRight;
    private int imageViewBgColor;
    private int imageViewBgStyle;
    private String imageViewDescription;
    private int imageViewHeight;
    private int imageViewMarginLeft;
    private int imageViewMarginRight;
    private int imageViewWidth;
    private int textColor;
    private int textMarginLeft;
    private int textMarginRight;
    private int textSize;
    private boolean withImage;

    public ChildViewSettings() {
        this.childViewHeight = 40;
        this.childViewGravity = 19;
        this.childViewOrientation = 0;
        this.childViewPressedBgColor = Color.parseColor("#D0D0D0");
        this.childViewNormalBgColor = -1;
        this.childViewStyle = ContactsConstant.WE_CHAT;
        this.dividerColor = Color.parseColor("#A0A0A0");
        this.dividerHeight = 40;
        this.dividerMarginLeft = 8;
        this.dividerMarginRight = 16;
        this.withImage = true;
        this.imageViewHeight = 30;
        this.imageViewWidth = 30;
        this.imageViewMarginLeft = 8;
        this.imageViewMarginRight = 4;
        this.imageViewDescription = ContactsConstant.IMG_INFO;
        this.textColor = Color.parseColor("#393939");
        this.textSize = 12;
        this.textMarginLeft = 4;
        this.textMarginRight = 0;
        this.imageViewBgColor = Color.parseColor("#AEAEAE");
        this.imageViewBgStyle = ContactsConstant.RECT;
    }

    private ChildViewSettings(Parcel source) {
        boolean z = true;
        this.childViewHeight = source.readInt();
        this.childViewGravity = source.readInt();
        this.childViewOrientation = source.readInt();
        this.childViewPressedBgColor = source.readInt();
        this.childViewNormalBgColor = source.readInt();
        this.childViewStyle = source.readInt();
        this.dividerColor = source.readInt();
        this.dividerHeight = source.readInt();
        this.dividerMarginLeft = source.readInt();
        this.dividerMarginRight = source.readInt();
        this.withImage = source.readByte() != 1 ? false : z;
        this.imageViewHeight = source.readInt();
        this.imageViewWidth = source.readInt();
        this.imageViewMarginLeft = source.readInt();
        this.imageViewMarginRight = source.readInt();
        this.imageViewDescription = source.readString();
        this.textColor = source.readInt();
        this.textSize = source.readInt();
        this.textMarginLeft = source.readInt();
        this.textMarginRight = source.readInt();
        this.imageViewBgColor = source.readInt();
        this.imageViewBgStyle = source.readInt();
    }

    /* synthetic */ ChildViewSettings(Parcel parcel, ChildViewSettings childViewSettings) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.childViewHeight);
        dest.writeInt(this.childViewGravity);
        dest.writeInt(this.childViewOrientation);
        dest.writeInt(this.childViewPressedBgColor);
        dest.writeInt(this.childViewNormalBgColor);
        dest.writeInt(this.childViewStyle);
        dest.writeInt(this.dividerColor);
        dest.writeInt(this.dividerHeight);
        dest.writeInt(this.dividerMarginLeft);
        dest.writeInt(this.dividerMarginRight);
        dest.writeByte(this.withImage ? (byte) 1 : 0);
        dest.writeInt(this.imageViewHeight);
        dest.writeInt(this.imageViewWidth);
        dest.writeInt(this.imageViewMarginLeft);
        dest.writeInt(this.imageViewMarginRight);
        dest.writeString(this.imageViewDescription);
        dest.writeInt(this.textColor);
        dest.writeInt(this.textSize);
        dest.writeInt(this.textMarginLeft);
        dest.writeInt(this.textMarginRight);
        dest.writeInt(this.imageViewBgColor);
        dest.writeInt(this.imageViewBgStyle);
    }

    public int getChildViewHeight() {
        return this.childViewHeight;
    }

    public void setChildViewHeight(int childViewHeight2) {
        this.childViewHeight = childViewHeight2;
    }

    public int getChildViewGravity() {
        return this.childViewGravity;
    }

    public void setChildViewGravity(int childViewGravity2) {
        this.childViewGravity = childViewGravity2;
    }

    public int getChildViewOrientation() {
        return this.childViewOrientation;
    }

    public void setChildViewOrientation(int childViewOrientation2) {
        this.childViewOrientation = childViewOrientation2;
    }

    public int getChildViewPressedBgColor() {
        return this.childViewPressedBgColor;
    }

    public void setChildViewPressedBgColor(int childViewPressedBgColor2) {
        this.childViewPressedBgColor = childViewPressedBgColor2;
    }

    public int getChildViewNormalBgColor() {
        return this.childViewNormalBgColor;
    }

    public void setChildViewNormalBgColor(int childViewNormalBgColor2) {
        this.childViewNormalBgColor = childViewNormalBgColor2;
    }

    public int getChildViewStyle() {
        return this.childViewStyle;
    }

    public void setChildViewStyle(int childViewStyle2) {
        this.childViewStyle = childViewStyle2;
    }

    public int getDividerColor() {
        return this.dividerColor;
    }

    public void setDividerColor(int dividerColor2) {
        this.dividerColor = dividerColor2;
    }

    public int getDividerHeight() {
        return this.dividerHeight;
    }

    public void setDividerHeight(int dividerHeight2) {
        this.dividerHeight = dividerHeight2;
    }

    public int getDividerMarginLeft() {
        return this.dividerMarginLeft;
    }

    public void setDividerMarginLeft(int dividerMarginLeft2) {
        this.dividerMarginLeft = dividerMarginLeft2;
    }

    public int getDividerMarginRight() {
        return this.dividerMarginRight;
    }

    public void setDividerMarginRight(int dividerMarginRight2) {
        this.dividerMarginRight = dividerMarginRight2;
    }

    public boolean isWithImage() {
        return this.withImage;
    }

    public void setWithImage(boolean withImage2) {
        this.withImage = withImage2;
    }

    public int getImageViewHeight() {
        return this.imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight2) {
        this.imageViewHeight = imageViewHeight2;
    }

    public int getImageViewWidth() {
        return this.imageViewWidth;
    }

    public void setImageViewWidth(int imageViewWidth2) {
        this.imageViewWidth = imageViewWidth2;
    }

    public int getImageViewMarginLeft() {
        return this.imageViewMarginLeft;
    }

    public void setImageViewMarginLeft(int imageViewMarginLeft2) {
        this.imageViewMarginLeft = imageViewMarginLeft2;
    }

    public int getImageViewMarginRight() {
        return this.imageViewMarginRight;
    }

    public void setImageViewMarginRight(int imageViewMarginRight2) {
        this.imageViewMarginRight = imageViewMarginRight2;
    }

    public String getImageViewDescription() {
        return this.imageViewDescription;
    }

    public void setImageViewDescription(String imageViewDescription2) {
        this.imageViewDescription = imageViewDescription2;
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

    public int getTextMarginLeft() {
        return this.textMarginLeft;
    }

    public void setTextMarginLeft(int textMarginLeft2) {
        this.textMarginLeft = textMarginLeft2;
    }

    public int getTextMarginRight() {
        return this.textMarginRight;
    }

    public void setTextMarginRight(int textMarginRight2) {
        this.textMarginRight = textMarginRight2;
    }

    public int getImageViewBgColor() {
        return this.imageViewBgColor;
    }

    public void setImageViewBgColor(int imageViewBgColor2) {
        this.imageViewBgColor = imageViewBgColor2;
    }

    public int getImageViewBgStyle() {
        return this.imageViewBgStyle;
    }

    public void setImageViewBgStyle(int imageViewBgStyle2) {
        this.imageViewBgStyle = imageViewBgStyle2;
    }
}