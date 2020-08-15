package com.wade.mobile.func;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.data.impl.DataMap;
//import com.tencent.bugly.beta.tinker.TinkerReport;
import com.alibaba.fastjson.JSONObject;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.common.contacts.activity.ContactsActivity;
import com.wade.mobile.common.contacts.helper.ContactsData;
import com.wade.mobile.common.contacts.helper.ContactsRecord;
import com.wade.mobile.common.contacts.setting.ContactsSettings;
import com.wade.mobile.common.contacts.util.ContactsConstant;
import com.wade.mobile.common.scan.decoding.Intents;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.WadeMobileActivity;
import com.wade.mobile.frame.client.WadeWebViewClient;
import com.wade.mobile.frame.config.ServerPageConfig;
import com.wade.mobile.frame.event.impl.TemplateWebViewEvent;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.template.TemplateWebView;
import com.wade.mobile.ui.activity.CustomDialogActivity;
import com.wade.mobile.ui.activity.CustomWindowActivity;
import com.wade.mobile.ui.activity.SlidingMenuActivity;
import com.wade.mobile.ui.anim.AnimationResource;
import com.wade.mobile.ui.build.dialog.progressdialog.SimpleProgressDialog;
import com.wade.mobile.ui.comp.dialog.HintDialog;
import com.wade.mobile.ui.comp.dialog.YMPickerDialog;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.ui.layout.ConstantParams;
import com.wade.mobile.ui.util.UiTool;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.Utility;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

public class MobileUI extends Plugin {
    /* access modifiers changed from: private */
    public static SimpleProgressDialog progressDialog = null;
    private final String DATA_REQUEST_ERROR = "dataRequestError";
    private final int REQUEST_CODE_CONTACTS_VIEW = 400;
    private final int REQUEST_CODE_CUSTOM_DIALOG = 100;
    private final int REQUEST_CODE_CUSTOM_WINDOW = 200;
    private final int REQUEST_CODE_SLIDING_MENU = 300;

    public MobileUI(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void openUrl(JSONArray param) throws Exception {
        openUrl(param.getString(0));
    }

    public void openUrl(String url) throws Exception {
        getWebView().loadRemoteUrl(URLDecoder.decode(url));
    }

    public void openPage(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        System.out.println("JSONArray-openPage:pageAction:"+pageAction);
        String data = param.getString(1);
        System.out.println("JSONArray-openPage:data:"+data);
        boolean isAnimation = true;
        if (param.length() > 2) {
            isAnimation = isNull(param.getString(2)) ? true : Boolean.parseBoolean(param.getString(2));
        }

        openPage(pageAction, isNull(data) ? null :data, isAnimation);

    }

    public void openPage(String pageAction, String param, boolean isAnimation) throws Exception {
        System.out.println("IData-openPage:data0:"+pageAction);
        String templatePath = ServerPageConfig.getTemplate(pageAction);
        System.out.println("IData-openPage:data1:"+templatePath);
        if (templatePath == null || "".equals(templatePath)) {
            Utility.error(Messages.NO_TEMPLATE + ",Action:" + pageAction);
        }
        IData data = new DataMap();
        System.out.println("IData-openPage:data2:"+pageAction);
        String dataAction = ServerPageConfig.getData(pageAction);
        System.out.println("IData-openPage:data3:"+dataAction);
        if (dataAction != null) {
           final String iData =param;
            System.out.println("IData-openPage:data4:"+param.toString());
            data = new DataMap(((MobileNetWork) this.wademobile.getPluginManager().getPlugin(MobileNetWork.class)).dataRequest(dataAction, iData,"MobileNetWork-openPage"));
            if (data.getInt("X_RESULTCODE") < 0) {
                error(data.toString());
                return;
            }
        }
        System.out.println("IData-openPage:data5:");
        openTemplate(pageAction, data, isAnimation);
        System.out.println("IData-openPage:data6:");
    }

    public void openPage(String pageAction, String param) throws Exception {
        final String iData = param;
        System.out.println("MobileUI-openPage:0:");
        openPage(pageAction, iData, true);
        System.out.println("MobileUI-openPage:6:");
    }

    public void openTemplate(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        boolean isAnimation = true;
        System.out.println("MobileUI-openTemplate:0:");
        if (param.length() > 2) {
            isAnimation = isNull(param.getString(2)) ? true : Boolean.parseBoolean(param.getString(2));
        }
        System.out.println("MobileUI-openTemplate:5:");
        openTemplate(pageAction, isNull(data) ? null : new DataMap(data), isAnimation);
        System.out.println("MobileUI-openTemplate:6:");
    }

    public void openTemplate(String pageAction, IData data, boolean isAnimation) throws Exception {
        final String templatePath = ServerPageConfig.getTemplate(pageAction);
        System.out.println("openTemplate0:"+pageAction);
        System.out.println("openTemplate1:"+templatePath);
        if (templatePath == null || "".equals(templatePath)) {
            Utility.error(Messages.NO_TEMPLATE + ",Action:" + pageAction);
        }
        final String str = pageAction;
        final boolean z = isAnimation;
        final IData iData = data;
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    MobileUI.this.initFlipperPage(str, z).loadTemplate(templatePath, iData);
                } catch (Exception e) {
                    MobileLog.e(MobileUI.this.TAG, e.getMessage(), e);
                }
            }
        });
    }

    public void getTemplate(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        System.out.println("MobileUI-JsonArray-getTemplate:0:"+param.toString());
        JSONObject jsonObject = JSONObject.parseObject(data);
        Map<String,Object> map = (Map<String,Object>)jsonObject;
        callback(getTemplate(pageAction, isNull(data) ? null : map), true);
        System.out.println("MobileUI-JsonArray-getTemplate:6:");
    }

    public String getTemplate(String pageAction, Map<String, Object> data) throws Exception {
        System.out.println("MobileUI-getTemplate:0:"+data.toString());
        String templatePath = ServerPageConfig.getTemplate(pageAction);
        if (templatePath == null || "".equals(templatePath)) {
            Utility.error(Messages.NO_TEMPLATE);
        }
        return ((TemplateWebView) getWebView()).getTemplate(templatePath, data);
    }

    public void getPage(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        String html = getPage(pageAction, isNull(data) ? null : data);
        if (!"dataRequestError".equals(html)) {
            callback(html, true);
        }
    }

    public String getPage(String pageAction, String param) throws Exception {
        String templatePath = ServerPageConfig.getTemplate(pageAction);
        if (templatePath == null || "".equals(templatePath)) {
            Utility.error(Messages.NO_TEMPLATE + ",Action:" + pageAction);
        }
        String data = null;
        String dataAction = ServerPageConfig.getData(pageAction);
        if (dataAction != null) {
            data = ((MobileNetWork) this.wademobile.getPluginManager().getPlugin(MobileNetWork.class)).dataRequest(dataAction, param,"MobileNetWork-getPage");
            if (data.contains("X_RESULTCODE") ) {
                error(data.toString());
                return "dataRequestError";
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(data);

        Map<String,Object> map = (Map<String,Object>)jsonObject;

        return ((TemplateWebView) getWebView()).getTemplate(templatePath, map);
    }

    /* access modifiers changed from: private */
    public TemplateWebView initFlipperPage(String pageAction, boolean isAnimation) {
        FlipperLayout mainFlipper = this.wademobile.getFlipperLayout();
        TemplateWebView webview = (TemplateWebView) mainFlipper.getNextView();
        if (webview == null) {
            webview = addFlipperPage(mainFlipper);
        }
        if (isAnimation) {
            mainFlipper.setAnimation(AnimationResource.pushLeft[0], AnimationResource.pushLeft[1]);
            mainFlipper.setBackAnimation(AnimationResource.pushRight[0], AnimationResource.pushRight[1]);
        }
        webview.setTag(pageAction);
        mainFlipper.setPreCurrView(webview);
        return webview;
    }

    private TemplateWebView addFlipperPage(FlipperLayout mainFlipper) {
        TemplateWebView webview = new TemplateWebView(this.wademobile) {
            /* access modifiers changed from: protected */
            public void initialize() {
                setWebViewClient(new WadeWebViewClient(this.wademobile, new TemplateWebViewEvent(this.wademobile) {
                    public void loadingFinished(WebView view, String url) {
                        this.wademobile.getFlipperLayout().showNextView();
                    }
                }));
            }
        };
        webview.setLayoutParams(ConstantParams.getFillParams(LinearLayout.LayoutParams.class));
        ((WadeMobileActivity) this.wademobile).getWebviewSetting().setWebViewStyle(webview);
        mainFlipper.addNextView(webview);
        return webview;
    }

    public void back(JSONArray param) throws Exception {
        final FlipperLayout mainFlipper = this.wademobile.getFlipperLayout();
        if (mainFlipper == null) {
            HintHelper.alert(this.context, "不支持back方法");
        } else if (!mainFlipper.isCanBack()) {
            this.context.finish();
        } else {
            this.context.runOnUiThread(new Runnable() {
                public void run() {
                    mainFlipper.back();
                }
            });
        }
    }

    public void alert(JSONArray param) throws Exception {
        String title = null;
        if (param.length() > 1) {
            title = param.getString(1);
        }
        new HintDialog(this.context, param.getString(0), title) {
            /* access modifiers changed from: protected */
            public void clickEvent() {
                MobileUI.this.callback("1212");
            }
        }.show();
    }

    public void tip(JSONArray param) throws Exception {
        int duration = 0;
        if (param.length() > 1) {
            if (param.getInt(1) == 0) {
                duration = 0;
            } else {
                duration = 1;
            }
        }
        HintHelper.tip(this.context, param.getString(0), duration);
    }

    public void loadingStart(JSONArray param) throws Exception {
        boolean z = true;
        String message = param.getString(0);
        String title = param.getString(1);
        String cancelable = param.getString(2);
        if (!isNull(cancelable)) {
            z = Boolean.parseBoolean(cancelable);
        }
        loadingStart(message, title, z);
    }

    public synchronized void loadingStart(String message, final String title, final boolean cancelable) {
        final String msg;
        if (isNull(message)) {
            msg = Messages.DIALOG_LOADING;
        } else {
            msg = message;
        }
        this.context.runOnUiThread(new Runnable() {
            /* access modifiers changed from: private */
            public void clear() {
                if (MobileUI.progressDialog != null && MobileUI.progressDialog.getProgressDialog().isShowing()) {
                    MobileUI.progressDialog.getProgressDialog().dismiss();
                    SimpleProgressDialog unused = MobileUI.progressDialog = null;
                }
            }

            public void run() {
                clear();
                SimpleProgressDialog unused = MobileUI.progressDialog = new SimpleProgressDialog(MobileUI.this.context);
                MobileUI.progressDialog.setProgressStyle(0);
                if (title != null && !title.equals("") && !title.equals("null")) {
                    MobileUI.progressDialog.setTitle(title);
                }
                MobileUI.progressDialog.setMessage(msg);
                MobileUI.progressDialog.setCancelable(cancelable);
                MobileUI.progressDialog.setCanceledOnTouchOutside(false);
                MobileUI.progressDialog.getProgressDialog().getWindow().setGravity(17);
                MobileUI.progressDialog.getProgressDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        //C36315.this.clear();

                    }
                });
                MobileUI.progressDialog.build().show();
            }
        });
    }

    public void loadingStop(JSONArray param) throws Exception {
        int count = 0;
        while (!loadingStop()) {
            Thread.sleep(500);
            count++;
            if (count > 3) {
                return;
            }
        }
    }

    public boolean loadingStop() {
        if (progressDialog == null || !progressDialog.getProgressDialog().isShowing()) {
            return false;
        }
        progressDialog.getProgressDialog().dismiss();
        progressDialog = null;
        return true;
    }

    public void confirm(JSONArray param) throws Exception {
        confirm(param.getString(0), param.getString(1), isNull(param.getString(2)) ? null : param.getString(2).split(Constant.PARAMS_SQE), isNull(param.getString(3)) ? null : param.getString(3).split(Constant.PARAMS_SQE));
    }

    @SuppressLint("ResourceType")
    public void confirm(String message, String title, final String[] events, String[] buttons) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog.setMessage(message);
        if (title != null) {
            dialog.setTitle(title);
        }
        dialog.setCancelable(false);
        if (buttons == null || buttons[0] == null) {
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (events != null && events[0] != null) {
                        MobileUI.this.executeJs(events[0]);
                    }
                }
            });
        } else {
            dialog.setPositiveButton(buttons[0], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (events != null && events[0] != null) {
                        MobileUI.this.executeJs(events[0]);
                    }
                }
            });
        }
        if (buttons == null || buttons[1] == null) {
            dialog.setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (events != null && events[1] != null) {
                        MobileUI.this.executeJs(events[1]);
                    }
                }
            });
        } else {
            dialog.setNegativeButton(buttons[1], new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (events != null && events[1] != null) {
                        MobileUI.this.executeJs(events[1]);
                    }
                }
            });
        }
        dialog.show();
    }

    public void getDate(JSONArray param) throws Exception {
        final String date = param.getString(0);
        final String format = param.getString(1);
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                MobileUI.this.getDate(date, format);
            }
        });
    }

    public void getDate(String date, String format) {
        final Calendar cal = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat(format);
        if (date == null || date.equals("null") || date.equals("")) {
            cal.setTime(new Date());
        } else {
            try {
                cal.setTime(df.parse(date));
            } catch (ParseException e) {
                cal.setTime(new Date());
            }
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String fmt = format.toUpperCase();
        if (fmt.matches("YYYY.*MM.*DD.*")) {
            new DatePickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(year, monthOfYear, dayOfMonth);
                    MobileUI.this.callback(df.format(cal.getTime()));
                }
            }, year, month, day_of_month).show();
        } else if (fmt.matches("YYYY.*MM.*")) {
            new YMPickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    cal.set(year, monthOfYear, dayOfMonth);
                    MobileUI.this.callback(df.format(cal.getTime()));
                }
            }, year, month, day_of_month, df).show();
        } else if (fmt.matches("HH.*MM.*")) {
            new TimePickerDialog(this.context, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker paramTimePicker, int hour, int minute) {
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);
                    MobileUI.this.callback(df.format(cal.getTime()));
                }
            }, hour, minute, true).show();
        }
    }

    public void getContactsView(JSONArray params) throws Exception {
        Intent intent = new Intent();
        intent.setClass(this.context, ContactsActivity.class);
        IData dataMap = new DataMap(params.getString(0));
        ContactsData contactsData = new ContactsData();
        boolean hasCustomeID = dataMap.getBoolean(ContactsConstant.KEY_HAS_CUSTOME_ID);
        addNoneTypeRecord(contactsData, dataMap.getDataset(ContactsConstant.KEY_NONE_TYPE_RECORD_LIST), hasCustomeID);
        addRecord(contactsData, dataMap.getDataset(ContactsConstant.KEY_RECORD_LIST), hasCustomeID);
        contactsData.setNoneTypeText(dataMap.getString(ContactsConstant.KEY_NONE_TYPE_TEXT));
        intent.putExtra(ContactsConstant.KEY_CONTACTS_DATA, contactsData);
        IData settingMap = new DataMap(params.getString(1));
        ContactsSettings settings = new ContactsSettings();
        settings.getChildViewSettings().setWithImage(settingMap.getBoolean(ContactsConstant.KEY_WITH_IMAGE));
        if (!settingMap.getBoolean(ContactsConstant.KEY_WITH_IMAGE)) {
            settings.getChildViewSettings().setTextMarginLeft(30);
        }
        settings.getChildViewSettings().setTextColor(Color.parseColor(settingMap.getString(ContactsConstant.KEY_CHILD_TEXT_COLOR)));
        settings.getChildViewSettings().setChildViewNormalBgColor(Color.parseColor(settingMap.getString(ContactsConstant.KEY_CHILD_VIEW_NORMAL_BG_COLOR)));
        settings.getGroupViewSettings().setGroupViewbgColor(Color.parseColor(settingMap.getString(ContactsConstant.KEY_GROUP_TEXT_COLOR)));
        settings.getGroupViewSettings().setGroupViewbgColor(Color.parseColor(settingMap.getString(ContactsConstant.KEY_GROUP_VIEW_BG_COLOR)));
        settings.getChildViewSettings().setDividerColor(Color.parseColor(settingMap.getString(ContactsConstant.KEY_DIVIDER_COLOR)));
        intent.putExtra(ContactsConstant.KEY_CONTACTS_SETTINGS, settings);
        startActivityForResult(intent, 400);
    }

    private void addRecord(ContactsData contactsData, IDataset recordList, boolean hasCustomeID) {
        int id;
        if (recordList != null && !recordList.isEmpty()) {
            String value = null;
            for (int i = 0; i < recordList.size(); i++) {
                new DataMap();
                IData data = recordList.getData(i);
                if (!hasCustomeID || data.getString("ID") == null) {
                    id = i + 1;
                } else {
                    id = Integer.parseInt(data.getString("ID"));
                }
                if (data.getString("VALUE") != null) {
                    value = data.getString("VALUE");
                }
                contactsData.addRecord(id, value);
            }
        }
    }

    private void addNoneTypeRecord(ContactsData contactsData, IDataset noneTypeRecordList, boolean hasCustomeID) {
        int id;
        if (noneTypeRecordList != null && !noneTypeRecordList.isEmpty()) {
            String value = null;
            for (int i = 0; i < noneTypeRecordList.size(); i++) {
                new DataMap();
                IData data = noneTypeRecordList.getData(i);
                if (!hasCustomeID || data.getString("ID") == null) {
                    id = i + 1;
                } else {
                    id = Integer.parseInt(data.getString("ID"));
                }
                if (data.getString("VALUE") != null) {
                    value = data.getString("VALUE");
                }
                contactsData.addNoneTypeRecord(id, value);
            }
        }
    }

    public void getChoice(JSONArray param) throws Exception {
        final String[] options = isNull(param.getString(0)) ? null : param.getString(0).split(Constant.PARAMS_SQE);
        final String[] values = isNull(param.getString(1)) ? null : param.getString(1).split(Constant.PARAMS_SQE);
        final String title = param.getString(2);
        String iconName = param.getString(3);
        final int iconId = isNull(iconName) ? 17301659 : UiTool.getR(this.context, "drawable", iconName);
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                MobileUI.this.getChoice(options, values, title, iconId);
            }
        });
    }

    public void getChoice(String[] options, final String[] values, String title, int iconId) {
        if (options != null) {
            AlertDialog.Builder choiceDialog = new AlertDialog.Builder(this.context);
            if (title != null && !title.equals("null")) {
                choiceDialog.setTitle(title);
                choiceDialog.setIcon(iconId);
            }
            choiceDialog.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (values == null || which >= values.length - 1) {
                        MobileUI.this.callback(String.valueOf(which));
                    } else {
                        MobileUI.this.callback(values[which]);
                    }
                    dialog.dismiss();
                }
            });
            choiceDialog.show();
        }
    }

    public void openDialog(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        double width = isNull(param.getString(2)) ? 0.5d : param.getDouble(2);
        double height = isNull(param.getString(3)) ? 0.5d : param.getDouble(3);
        Intent intent = new Intent(this.context, CustomDialogActivity.class);
        intent.putExtra("pageAction", pageAction);
        intent.putExtra("data", data);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        startActivityForResult(intent, 100);
    }

    public void closeDialog(JSONArray param) throws Exception {
        int resultState = 1;
        String resultData = param.getString(0);
        if (isNull(resultData)) {
            resultData = null;
        }
        if (!isNull(param.getString(1))) {
            resultState = param.getInt(1);
        }
        if (this.context instanceof CustomDialogActivity) {
            ((CustomDialogActivity) this.context).closeDialog(resultData, resultState);
        } else {
            HintHelper.alert(this.context, "无对话框可以关闭!");
        }
    }

    public void openWindow(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        Intent intent = new Intent(this.context, CustomWindowActivity.class);
        intent.putExtra("pageAction", pageAction);
        intent.putExtra("data", data);
        startActivityForResult(intent, 200);
    }

    public void closeWindow(JSONArray param) throws Exception {
        int resultState = 1;
        String resultData = param.getString(0);
        if (isNull(resultData)) {
            resultData = null;
        }
        if (!isNull(param.getString(1))) {
            resultState = param.getInt(1);
        }
        if (this.context instanceof CustomWindowActivity) {
            ((CustomWindowActivity) this.context).closeWindow(resultData, resultState);
        } else {
            HintHelper.alert(this.context, "无窗口可以关闭!");
        }
    }

    public void openSlidingMenu(JSONArray param) throws Exception {
        String pageAction = param.getString(0);
        String data = param.getString(1);
        String width = param.getString(2);
        String height = param.getString(3);
        String leftMargin = param.getString(4);
        String topMargin = param.getString(5);
        Intent intent = new Intent(this.context, SlidingMenuActivity.class);
        intent.putExtra("pageAction", pageAction);
        intent.putExtra("data", data);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra(SlidingMenuActivity.KEY_LEFT_MARGIN, leftMargin);
        intent.putExtra(SlidingMenuActivity.KEY_TOP_MARGIN, topMargin);
        startActivityForResult(intent,300);
    }

    public void closeSlidingMenu(JSONArray param) throws Exception {
        int resultState = 1;
        String resultData = param.getString(0);
        if (isNull(resultData)) {
            resultData = null;
        }
        if (!isNull(param.getString(1))) {
            resultState = param.getInt(1);
        }
        if (this.context instanceof SlidingMenuActivity) {
            ((SlidingMenuActivity) this.context).closeSlidingMenu(resultData, resultState);
        } else {
            HintHelper.alert(this.context, "无侧滑菜单可以关闭!");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String resultData;
        String resultData2;
        String resultData3;
        if (requestCode == 100) {
            if (intent != null && (resultData3 = intent.getStringExtra("result")) != null) {
                callback(resultData3, true);
            }
        } else if (requestCode == 200) {
            if (intent != null && (resultData2 = intent.getStringExtra("result")) != null) {
                callback(resultData2, true);
            }
        } else if (requestCode == 300) {
            if (intent != null && (resultData = intent.getStringExtra("result")) != null) {
                callback(resultData, true);
            }
        } else if (requestCode == 400 && intent != null) {
            ContactsRecord record = (ContactsRecord) intent.getParcelableExtra(ContactsConstant.KEY_SELECT_RECORD);
            IData data = new DataMap();
            data.put("ID", Integer.valueOf(record.getId()));
            data.put(Intents.WifiConnect.TYPE, record.getType());
            data.put("VALUE", record.getValue());
            data.put("COLOR", record.getColor());
            callback(data.toString());
        }
    }
}