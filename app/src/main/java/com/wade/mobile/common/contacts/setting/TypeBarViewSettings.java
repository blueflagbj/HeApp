package com.wade.mobile.common.contacts.setting;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import com.wade.mobile.common.contacts.util.TypefaceTool;

public class TypeBarViewSettings implements Parcelable {
    public static final Creator<TypeBarViewSettings> CREATOR = new Creator<TypeBarViewSettings>() {
        public TypeBarViewSettings[] newArray(int size) {
            return new TypeBarViewSettings[size];
        }

        public TypeBarViewSettings createFromParcel(Parcel source) {
            return new TypeBarViewSettings(source, (TypeBarViewSettings) null);
        }
    };
    private int popupTextColor;
    private int popupTextGravity;
    private int popupTextSize;
    private int popupTextTypeface;
    private int popupWindowBgColor;
    private int popupWindowGravity;
    private int popupWindowHeight;
    private int popupWindowRadius;
    private int popupWindowWidth;
    private int tbNormalTextColor;
    private double tbNormalTextScale;
    private int tbNormalTextTypeface;
    private boolean tbPressedFakeBoldText;
    private int tbPressedTextColor;
    private double tbPressedTextScale;
    private int tbViewMarginBottom;
    private int tbViewMarginLeft;
    private int tbViewMarginRight;
    private int tbViewMarginTop;
    private int tbViewNormalBgColor;
    private int tbViewPressedBgColor;
    private int tbViewWidth;

    public TypeBarViewSettings() {
        this.tbViewWidth = 16;
        this.tbViewMarginLeft = 0;
        this.tbViewMarginRight = 0;
        this.tbViewMarginTop = 23;
        this.tbViewMarginBottom = 23;
        this.tbViewNormalBgColor = 0;
        this.tbNormalTextTypeface = 65521;
        this.tbNormalTextColor = Color.parseColor("#6B6B6B");
        this.tbNormalTextScale = 0.7d;
        this.tbPressedTextColor = Color.parseColor("#525252");
        this.tbPressedFakeBoldText = true;
        this.tbPressedTextScale = 0.9d;
        this.tbViewPressedBgColor = Color.parseColor("#DDBEBEBE");
        this.popupWindowRadius = 6;
        this.popupWindowBgColor = Color.parseColor("#DD7F7F7F");
        this.popupTextGravity = 17;
        this.popupTextColor = Color.parseColor("#FFFFFF");
        this.popupTextSize = 40;
        this.popupTextTypeface = TypefaceTool.SERIF;
        this.popupWindowWidth = 100;
        this.popupWindowHeight = 100;
        this.popupWindowGravity = 17;
    }

    private TypeBarViewSettings(Parcel source) {
        boolean z = true;
        this.tbViewWidth = source.readInt();
        this.tbViewMarginLeft = source.readInt();
        this.tbViewMarginRight = source.readInt();
        this.tbViewMarginTop = source.readInt();
        this.tbViewMarginBottom = source.readInt();
        this.tbViewNormalBgColor = source.readInt();
        this.tbNormalTextTypeface = source.readInt();
        this.tbNormalTextColor = source.readInt();
        this.tbNormalTextScale = source.readDouble();
        this.tbPressedTextColor = source.readInt();
        this.tbPressedFakeBoldText = source.readByte() != 1 ? false : z;
        this.tbPressedTextScale = source.readDouble();
        this.tbViewPressedBgColor = source.readInt();
        this.popupWindowRadius = source.readInt();
        this.popupWindowBgColor = source.readInt();
        this.popupTextGravity = source.readInt();
        this.popupTextColor = source.readInt();
        this.popupTextSize = source.readInt();
        this.popupTextTypeface = source.readInt();
        this.popupWindowWidth = source.readInt();
        this.popupWindowHeight = source.readInt();
        this.popupWindowGravity = source.readInt();
    }

    /* synthetic */ TypeBarViewSettings(Parcel parcel, TypeBarViewSettings typeBarViewSettings) {
        this(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tbViewWidth);
        dest.writeInt(this.tbViewMarginLeft);
        dest.writeInt(this.tbViewMarginRight);
        dest.writeInt(this.tbViewMarginTop);
        dest.writeInt(this.tbViewMarginBottom);
        dest.writeInt(this.tbViewNormalBgColor);
        dest.writeInt(this.tbNormalTextTypeface);
        dest.writeInt(this.tbNormalTextColor);
        dest.writeDouble(this.tbNormalTextScale);
        dest.writeInt(this.tbPressedTextColor);
        dest.writeByte(this.tbPressedFakeBoldText ? (byte) 1 : 0);
        dest.writeDouble(this.tbPressedTextScale);
        dest.writeInt(this.tbViewPressedBgColor);
        dest.writeInt(this.popupWindowRadius);
        dest.writeInt(this.popupWindowBgColor);
        dest.writeInt(this.popupTextGravity);
        dest.writeInt(this.popupTextColor);
        dest.writeInt(this.popupTextSize);
        dest.writeInt(this.popupTextTypeface);
        dest.writeInt(this.popupWindowWidth);
        dest.writeInt(this.popupWindowHeight);
        dest.writeInt(this.popupWindowGravity);
    }

    public int getTbViewWidth() {
        return this.tbViewWidth;
    }

    public void setTbViewWidth(int tbViewWidth2) {
        this.tbViewWidth = tbViewWidth2;
    }

    public int getTbViewMarginLeft() {
        return this.tbViewMarginLeft;
    }

    public void setTbViewMarginLeft(int tbViewMarginLeft2) {
        this.tbViewMarginLeft = tbViewMarginLeft2;
    }

    public int getTbViewMarginRight() {
        return this.tbViewMarginRight;
    }

    public void setTbViewMarginRight(int tbViewMarginRight2) {
        this.tbViewMarginRight = tbViewMarginRight2;
    }

    public int getTbViewMarginTop() {
        return this.tbViewMarginTop;
    }

    public void setTbViewMarginTop(int tbViewMarginTop2) {
        this.tbViewMarginTop = tbViewMarginTop2;
    }

    public int getTbViewMarginBottom() {
        return this.tbViewMarginBottom;
    }

    public void setTbViewMarginBottom(int tbViewMarginBottom2) {
        this.tbViewMarginBottom = tbViewMarginBottom2;
    }

    public int getTbViewNormalBgColor() {
        return this.tbViewNormalBgColor;
    }

    public void setTbViewNormalBgColor(int tbViewNormalBgColor2) {
        this.tbViewNormalBgColor = tbViewNormalBgColor2;
    }

    public int getTbNormalTextTypeface() {
        return this.tbNormalTextTypeface;
    }

    public void setTbNormalTextTypeface(int tbNormalTextTypeface2) {
        this.tbNormalTextTypeface = tbNormalTextTypeface2;
    }

    public int getTbNormalTextColor() {
        return this.tbNormalTextColor;
    }

    public void setTbNormalTextColor(int tbNormalTextColor2) {
        this.tbNormalTextColor = tbNormalTextColor2;
    }

    public double getTbNormalTextScale() {
        return this.tbNormalTextScale;
    }

    public void setTbNormalTextScale(double tbNormalTextScale2) {
        this.tbNormalTextScale = tbNormalTextScale2;
    }

    public int getTbPressedTextColor() {
        return this.tbPressedTextColor;
    }

    public void setTbPressedTextColor(int tbPressedTextColor2) {
        this.tbPressedTextColor = tbPressedTextColor2;
    }

    public boolean isTbPressedFakeBoldText() {
        return this.tbPressedFakeBoldText;
    }

    public void setTbPressedFakeBoldText(boolean tbPressedFakeBoldText2) {
        this.tbPressedFakeBoldText = tbPressedFakeBoldText2;
    }

    public double getTbPressedTextScale() {
        return this.tbPressedTextScale;
    }

    public void setTbPressedTextScale(double tbPressedTextScale2) {
        this.tbPressedTextScale = tbPressedTextScale2;
    }

    public int getTbViewPressedBgColor() {
        return this.tbViewPressedBgColor;
    }

    public void setTbViewPressedBgColor(int tbViewPressedBgColor2) {
        this.tbViewPressedBgColor = tbViewPressedBgColor2;
    }

    public int getPopupWindowRadius() {
        return this.popupWindowRadius;
    }

    public void setPopupWindowRadius(int popupWindowRadius2) {
        this.popupWindowRadius = popupWindowRadius2;
    }

    public int getPopupWindowBgColor() {
        return this.popupWindowBgColor;
    }

    public void setPopupWindowBgColor(int popupWindowBgColor2) {
        this.popupWindowBgColor = popupWindowBgColor2;
    }

    public int getPopupTextGravity() {
        return this.popupTextGravity;
    }

    public void setPopupTextGravity(int popupTextGravity2) {
        this.popupTextGravity = popupTextGravity2;
    }

    public int getPopupTextColor() {
        return this.popupTextColor;
    }

    public void setPopupTextColor(int popupTextColor2) {
        this.popupTextColor = popupTextColor2;
    }

    public int getPopupTextSize() {
        return this.popupTextSize;
    }

    public void setPopupTextSize(int popupTextSize2) {
        this.popupTextSize = popupTextSize2;
    }

    public int getPopupTextTypeface() {
        return this.popupTextTypeface;
    }

    public void setPopupTextTypeface(int popupTextTypeface2) {
        this.popupTextTypeface = popupTextTypeface2;
    }

    public int getPopupWindowWidth() {
        return this.popupWindowWidth;
    }

    public void setPopupWindowWidth(int popupWindowWidth2) {
        this.popupWindowWidth = popupWindowWidth2;
    }

    public int getPopupWindowHeight() {
        return this.popupWindowHeight;
    }

    public void setPopupWindowHeight(int popupWindowHeight2) {
        this.popupWindowHeight = popupWindowHeight2;
    }

    public int getPopupWindowGravity() {
        return this.popupWindowGravity;
    }

    public void setPopupWindowGravity(int popupWindowGravity2) {
        this.popupWindowGravity = popupWindowGravity2;
    }
}