package com.wade.mobile.common.contacts.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import com.wade.mobile.common.contacts.helper.ContactsData;
import com.wade.mobile.common.contacts.helper.ContactsRecord;
import com.wade.mobile.common.contacts.helper.DensityHelper;
import com.wade.mobile.common.contacts.helper.HashList;
import com.wade.mobile.common.contacts.helper.OnTouchTypeBarListener;
import com.wade.mobile.common.contacts.helper.PinyinExpandableListAdapter;
import com.wade.mobile.common.contacts.helper.TypeBarView;
import com.wade.mobile.common.contacts.setting.ChildViewSettings;
import com.wade.mobile.common.contacts.setting.ContactsSettings;
import com.wade.mobile.common.contacts.setting.TypeBarViewSettings;
import com.wade.mobile.common.contacts.util.ContactsConstant;
import com.wade.mobile.common.contacts.util.PinyinTool;
import java.util.ArrayList;

public class ContactsActivity extends Activity {
    private ChildViewSettings cSettings;
    private DensityHelper densityHelper;
    private ExpandableListView expandableListView;
    /* access modifiers changed from: private */
    public HashList<String, ContactsRecord> hashList;
    private ArrayList<ContactsRecord> itemList;
    private ArrayList<ContactsRecord> noneTypeItemList;
    private PinyinExpandableListAdapter pinyinAdapter;
    private RelativeLayout relativeLayout;
    private ContactsSettings settings;
    private TypeBarViewSettings tSettings;
    private TypeBarView typeBarView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.densityHelper = new DensityHelper(this);
        ContactsData contactsData = (ContactsData) intent.getParcelableExtra(ContactsConstant.KEY_CONTACTS_DATA);
        this.settings = (ContactsSettings) intent.getParcelableExtra(ContactsConstant.KEY_CONTACTS_SETTINGS);
        this.tSettings = this.settings.getTypeBarViewSettings();
        this.cSettings = this.settings.getChildViewSettings();
        this.itemList = contactsData.getItemList();
        this.noneTypeItemList = contactsData.getNoneTypeItemList();
        this.hashList = PinyinTool.categories(this.itemList, this.noneTypeItemList);
        this.pinyinAdapter = new PinyinExpandableListAdapter(this, this.hashList, contactsData.getNoneTypeText(), this.settings);
        this.relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        this.expandableListView = new ExpandableListView(this);
        if (241 != this.cSettings.getChildViewStyle()) {
            this.expandableListView.setDivider((Drawable) null);
        }
        this.expandableListView.setGroupIndicator((Drawable) null);
        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(-1, -1);
        listParams.addRule(9, -1);
        listParams.addRule(10, -1);
        this.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @SuppressLint("ResourceType")
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final ContactsRecord selectRecord = (ContactsRecord) ContactsActivity.this.hashList.getValue(groupPosition, childPosition);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("确认");
                dialogBuilder.setMessage("确认选择“" + selectRecord.getValue() + "”吗？");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent result = new Intent();
                        result.putExtra(ContactsConstant.KEY_SELECT_RECORD, selectRecord);
                        ContactsActivity.this.setResult(ContactsConstant.RESULT_CODE, result);
                        ContactsActivity.this.finish();
                    }
                });
                dialogBuilder.setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogBuilder.create();
                dialogBuilder.show();
                return true;
            }
        });
        this.typeBarView = new TypeBarView(this, contactsData.hasNoneType(), this.settings);
        RelativeLayout.LayoutParams barParams = new RelativeLayout.LayoutParams(d2p(this.tSettings.getTbViewWidth()), -1);
        barParams.setMargins(d2p(this.tSettings.getTbViewMarginLeft()), d2p(this.tSettings.getTbViewMarginTop()), d2p(this.tSettings.getTbViewMarginRight()), d2p(this.tSettings.getTbViewMarginBottom()));
        barParams.addRule(11, -1);
        barParams.addRule(10, -1);
        this.typeBarView.setBackgroundColor(this.tSettings.getTbViewNormalBgColor());
        this.typeBarView.setOnTouchTypeBarListener(new OnTouchTypeBarListener(this, this.expandableListView, this.hashList, this.settings));
        this.relativeLayout.addView(this.expandableListView, listParams);
        this.relativeLayout.addView(this.typeBarView, barParams);
        this.expandableListView.setAdapter(this.pinyinAdapter);
        int length = this.pinyinAdapter.getGroupCount();
        for (int i = 0; i < length; i++) {
            this.expandableListView.expandGroup(i);
        }
        setContentView(this.relativeLayout, layoutParams);
    }

    private int d2p(int dipValue) {
        return this.densityHelper.dip2px((float) dipValue);
    }
}