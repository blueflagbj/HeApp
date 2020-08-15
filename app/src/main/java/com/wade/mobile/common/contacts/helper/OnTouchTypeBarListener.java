package com.wade.mobile.common.contacts.helper;

import android.app.Activity;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wade.mobile.common.contacts.setting.ContactsSettings;
import com.wade.mobile.common.contacts.setting.TypeBarViewSettings;
import com.wade.mobile.common.contacts.util.TypefaceTool;

public class OnTouchTypeBarListener implements IOnTouchTypeBarListener {
    private Activity activity;
    private DensityHelper densityHelper;
    private ExpandableListView expandableListView;
    private HashList<String, ContactsRecord> hashList;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private TypeBarViewSettings tSettings;
    private TextView textView;

    public OnTouchTypeBarListener(Activity activity2, ExpandableListView expandableListView2, HashList<String, ContactsRecord> hashList2, ContactsSettings settings) {
        this.activity = activity2;
        this.expandableListView = expandableListView2;
        this.hashList = hashList2;
        this.tSettings = settings.getTypeBarViewSettings();
        this.densityHelper = new DensityHelper(activity2);
        int radius = d2p(this.tSettings.getPopupWindowRadius());
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{(float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius, (float) radius}, (RectF) null, (float[]) null));
        shapeDrawable.getPaint().setColor(this.tSettings.getPopupWindowBgColor());
        this.relativeLayout = new RelativeLayout(activity2);
        this.relativeLayout.setBackgroundDrawable(shapeDrawable);
        this.textView = new TextView(activity2);
        this.textView.setBackgroundColor(0);
        this.textView.setGravity(this.tSettings.getPopupTextGravity());
        this.textView.setTextColor(this.tSettings.getPopupTextColor());
        this.textView.setTextSize((float) d2p(this.tSettings.getPopupTextSize()));
        this.textView.setTypeface(TypefaceTool.getTypeface(this.tSettings.getPopupTextTypeface()));
        this.relativeLayout.addView(this.textView, new RelativeLayout.LayoutParams(-1, -1));
    }

    public void onTouch(String type) {
        int index = this.hashList.indexOfType(type);
        if (index != -1) {
            this.expandableListView.setSelectedGroup(index);
        } else if (TypeBarView.TYPE_NONE.equals(type)) {
            this.expandableListView.setSelection(0);
        } else if (TypeBarView.TYPE_FIRST.equals(type)) {
            this.expandableListView.setSelectedGroup(1);
        }
        if (this.popupWindow != null) {
            this.textView.setText(type);
        } else {
            this.popupWindow = new PopupWindow(this.relativeLayout, d2p(this.tSettings.getPopupWindowWidth()), d2p(this.tSettings.getPopupWindowHeight()), false);
            this.popupWindow.showAtLocation(this.activity.getWindow().getDecorView(), 17, 0, 0);
        }
        this.textView.setText(type);
    }

    public void onTouchUP() {
        if (this.popupWindow != null) {
            this.popupWindow.dismiss();
            this.popupWindow = null;
        }
    }

    private int d2p(int dipValue) {
        return this.densityHelper.dip2px((float) dipValue);
    }
}