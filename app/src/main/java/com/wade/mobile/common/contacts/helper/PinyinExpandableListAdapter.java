package com.wade.mobile.common.contacts.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wade.mobile.common.contacts.helper.TextDrawable;
import com.wade.mobile.common.contacts.setting.ChildViewSettings;
import com.wade.mobile.common.contacts.setting.ContactsSettings;
import com.wade.mobile.common.contacts.setting.GroupViewSettings;
import com.wade.mobile.common.contacts.util.ContactsConstant;

public class PinyinExpandableListAdapter extends BaseExpandableListAdapter {
    private int CHILD_IMAGE_VIEW_ID = 4099;
    private int CHILD_TEXTVIEW_ID = 4098;
    private int GROUP_TEXTVIEW_ID = 4097;
    private Activity activity;
    private ChildViewSettings cSettings;
    private ChildBackgroundView childBackgroundPressedView;
    private ChildBackgroundView childBackgroundPressedViewNoLine;
    private ChildBackgroundView childBackgroundView;
    private ChildBackgroundView childBackgroundViewNoLine;
    private DensityHelper densityHelper;
    private GroupViewSettings gSettings;
    HashList<String, ContactsRecord> hashList;
    private String noneTypeText;
    private TextDrawable.IBuilder textDrawableBuilder;

    public PinyinExpandableListAdapter(Activity activity2, HashList<String, ContactsRecord> hashList2, String noneTypeText2, ContactsSettings settings) {
        this.activity = activity2;
        this.hashList = hashList2;
        this.densityHelper = new DensityHelper(activity2);
        this.gSettings = settings.getGroupViewSettings();
        this.cSettings = settings.getChildViewSettings();
        if (TextUtils.isEmpty(noneTypeText2)) {
            this.noneTypeText = ContactsConstant.NONE_TYPE_TEXT;
        } else {
            this.noneTypeText = noneTypeText2;
        }
        if (242 == this.cSettings.getChildViewStyle()) {
            DisplayMetrics dm = new DisplayMetrics();
            activity2.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            PointF from = new PointF((float) this.densityHelper.dip2px((float) this.densityHelper.dip2px((float) this.cSettings.getDividerMarginLeft())), (float) this.densityHelper.dip2px((float) this.cSettings.getDividerHeight()));
            PointF to = new PointF((float) (width - this.densityHelper.dip2px((float) this.cSettings.getDividerMarginRight())), (float) this.densityHelper.dip2px((float) this.cSettings.getDividerHeight()));
            this.childBackgroundView = new ChildBackgroundView(from, to);
            this.childBackgroundView.setBackgroundColor(this.cSettings.getChildViewNormalBgColor());
            this.childBackgroundView.setWithLine(true);
            this.childBackgroundView.setLineColor(this.cSettings.getDividerColor());
            this.childBackgroundPressedView = new ChildBackgroundView(from, to);
            this.childBackgroundPressedView.setWithLine(true);
            this.childBackgroundPressedView.setBackgroundColor(this.cSettings.getChildViewPressedBgColor());
            this.childBackgroundPressedView.setLineColor(this.cSettings.getDividerColor());
            this.childBackgroundViewNoLine = new ChildBackgroundView();
            this.childBackgroundViewNoLine.setBackgroundColor(this.cSettings.getChildViewNormalBgColor());
            this.childBackgroundViewNoLine.setWithLine(false);
            this.childBackgroundPressedViewNoLine = new ChildBackgroundView();
            this.childBackgroundPressedViewNoLine.setWithLine(false);
            this.childBackgroundPressedViewNoLine.setBackgroundColor(this.cSettings.getChildViewPressedBgColor());
        }
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public int getGroupCount() {
        return this.hashList.typeSize();
    }

    public int getChildrenCount(int group) {
        return this.hashList.getValues(group).size();
    }

    public Object getGroup(int group) {
        return this.hashList.getValues(group);
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parentView) {
        if (convertView == null) {
            convertView = createGroupView(this.activity);
        }
        String type = this.hashList.getType(groupPosition);
        TextView textView = (TextView) convertView.findViewById(this.GROUP_TEXTVIEW_ID);
        if (TypeBarView.TYPE_NONE.equals(type)) {
            textView.setText(this.noneTypeText);
        } else {
            textView.setText(type);
        }
        return convertView;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.hashList.getValue(groupPosition, childPosition);
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parentView) {
        if (convertView == null) {
            convertView = createChildView(groupPosition, childPosition, isLastChild);
        }
        convertView.setBackgroundDrawable(createSelector(isLastChild));
        ((TextView) convertView.findViewById(this.CHILD_TEXTVIEW_ID)).setText(this.hashList.getValue(groupPosition, childPosition).getValue());
        if (this.cSettings.isWithImage()) {
            String value = this.hashList.getValue(groupPosition, childPosition).getValue();
            ((ImageView) convertView.findViewById(this.CHILD_IMAGE_VIEW_ID)).setBackgroundDrawable(createContactsIcon(String.valueOf(value.charAt(value.length() - 1)), this.cSettings.getImageViewBgColor()));
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    private View createGroupView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(this.gSettings.getGroupViewGravity());
        linearLayout.setBackgroundColor(this.gSettings.getGroupViewbgColor());
        linearLayout.setClickable(true);
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, -1));
        TextView textView = new TextView(context);
        textView.setSingleLine(true);
        textView.setTextAppearance(context, this.gSettings.getTextAppearance());
        textView.setTextColor(this.gSettings.getTextColor());
        textView.setTextSize((float) this.densityHelper.sp2px((float) this.gSettings.getTextSize()));
        textView.setId(this.GROUP_TEXTVIEW_ID);
        textView.setGravity(this.gSettings.getTextGravity());
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(-1, this.densityHelper.dip2px((float) this.gSettings.getTextViewHeight()));
        textViewParams.setMargins(this.densityHelper.dip2px((float) this.gSettings.getTextViewMarginLeft()), 0, this.densityHelper.dip2px((float) this.gSettings.getTextViewMarginRight()), 0);
        linearLayout.addView(textView, textViewParams);
        return linearLayout;
    }

    private View createChildView(int groupPosition, int childPosition, boolean isLastChild) {
        LinearLayout linearLayout = new LinearLayout(this.activity);
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, this.densityHelper.dip2px((float) this.cSettings.getChildViewHeight())));
        linearLayout.setGravity(this.cSettings.getChildViewGravity());
        linearLayout.setOrientation(this.cSettings.getChildViewOrientation());
        if (this.cSettings.isWithImage()) {
            ImageView imageView = new ImageView(this.activity);
            imageView.setId(this.CHILD_IMAGE_VIEW_ID);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(this.densityHelper.dip2px((float) this.cSettings.getImageViewWidth()), this.densityHelper.dip2px((float) this.cSettings.getImageViewHeight()));
            imageViewParams.setMargins(this.densityHelper.dip2px((float) this.cSettings.getImageViewMarginLeft()), 0, this.densityHelper.dip2px((float) this.cSettings.getImageViewMarginRight()), 0);
            imageView.setContentDescription(this.cSettings.getImageViewDescription());
            linearLayout.addView(imageView, imageViewParams);
        }
        TextView textView = new TextView(this.activity);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(-2, -2);
        textViewParams.setMargins(this.densityHelper.dip2px((float) this.cSettings.getTextMarginLeft()), 0, this.densityHelper.dip2px((float) this.cSettings.getTextMarginRight()), 0);
        textView.setTextColor(this.cSettings.getTextColor());
        textView.setTextSize((float) this.densityHelper.sp2px((float) this.cSettings.getTextSize()));
        textView.setId(this.CHILD_TEXTVIEW_ID);
        linearLayout.addView(textView, textViewParams);
        return linearLayout;
    }

    private StateListDrawable createSelector(boolean isLastChild) {
        StateListDrawable stateListDrawbale = new StateListDrawable();
        if (242 != this.cSettings.getChildViewStyle()) {
            stateListDrawbale.addState(new int[]{16842919}, new ColorDrawable(this.cSettings.getChildViewPressedBgColor()));
            stateListDrawbale.addState(new int[]{-16842908, -16842913, -16842919}, new ColorDrawable(this.cSettings.getChildViewNormalBgColor()));
        } else if (isLastChild) {
            stateListDrawbale.addState(new int[]{16842919}, this.childBackgroundPressedViewNoLine);
            stateListDrawbale.addState(new int[]{-16842908, -16842913, -16842919}, this.childBackgroundViewNoLine);
        } else {
            stateListDrawbale.addState(new int[]{16842919}, this.childBackgroundPressedView);
            stateListDrawbale.addState(new int[]{-16842908, -16842913, -16842919}, this.childBackgroundView);
        }
        return stateListDrawbale;
    }

    private Drawable createContactsIcon(String text, int color) {
        if (this.textDrawableBuilder == null) {
            switch (this.cSettings.getImageViewBgStyle()) {
                case ContactsConstant.RECT /*4081*/:
                    this.textDrawableBuilder = TextDrawable.builder().rect();
                    break;
                case ContactsConstant.ROUND /*4082*/:
                    this.textDrawableBuilder = TextDrawable.builder().round();
                    break;
                default:
                    this.textDrawableBuilder = TextDrawable.builder().rect();
                    break;
            }
        }
        return this.textDrawableBuilder.build(text, color);
    }
}