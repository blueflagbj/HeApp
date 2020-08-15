package com.ai.cmcchina.crm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ai.cmcchina.crm.fragment.BaseFragment;
//import com.ai.cmcchina.crm.fragment.OrderMarketingFragment;
import com.ai.cmcchina.crm.handler.CRMRemoteHandler;
import com.ai.cmcchina.crm.util.CRMSystem;
import com.ai.cmcchina.crm.util.JsonUtil;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.multiple.util.FileUtils;
import com.ai.cmcchina.multiple.util.MemoryCacheUtil;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.alibaba.fastjson.JSON;



import com.ai.cmcchina.crm.handler.IPURemoteHandler;


import com.heclient.heapp.App;
import com.heclient.heapp.R;
import com.wade.mobile.helper.SharedPrefHelper;
import com.wade.mobile.util.Constant;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* renamed from: com.ai.cmcchina.crm.ui.BaseActivity */
public class BaseActivity extends FragmentActivity {
    private static String currenTitleName = "";
    private Button backBtn;

    /* renamed from: fm */
    protected FragmentManager f938fm;
    private BaseFragment fragment;
    private String iconName;
    protected String title;
    private TextView titleTextV;
    private View waterView;
    private String workOrderId;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle arg0) {
        try {
            super.onCreate(arg0);
            requestWindowFeature(1);
          //  setContentView(R.layout.activity_base_layout);
            Intent intent = getIntent();
            this.title = intent.getStringExtra("title");
            this.iconName = intent.getStringExtra(Constant.WadeMobileActivity.ICON);
            setOrderCenterData(intent);
            String subsystem = intent.getStringExtra(com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
            if (!StringUtil.isNotBlank(subsystem) || !"ESOP".equalsIgnoreCase(subsystem)) {
                new SharedPrefHelper(this).remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
            } else {
                SharedPrefHelper spHelper = new SharedPrefHelper(this);
                spHelper.remove(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
                spHelper.put(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem, (Object) subsystem);
            }
            checkAccountTypeIsHaveAuthority(intent);
            initTitle();
            this.f938fm = getSupportFragmentManager();
          //  String accountType = ((App) getApplication()).getAccountChannelType();
            String code = getCode(this.iconName);
            if ("触点营销".equals(this.title)) {
               // this.fragment = new ContactMarketFragment();
            } else if ("订购营销活动".equals(this.title)) {
               // this.fragment = new OrderMarketingFragment();
            } else if ("换卡".equals(this.title) || "G3YYT_11_16_99".equals(code)) {
            /*    if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL.equals(accountType)) {
//                    this.fragment = RenewCardFragment.newFragment();
                } else if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(accountType)) {
                  //  this.fragment = SocialRenewCardFragment.newFragment();
                }*/
            } else if ("交往圈验证".equals(this.title) || "G3YYT_11_16_00".equals(code)) {
              //  this.fragment = new ContactCircleFragment();
            } else if ("普通开户".equals(this.title) || "G3YYT_11_16_22".equals(code)) {

            } else if ("主副卡".equals(this.title) || "G3YYT_17_03_31".equals(code)) {
              //  this.fragment = new MainViceSimCardEntranceFragment();
            } else if ("宽带预约".equals(this.title) || "G3YYT_17_01".equals(code)) {
              //  this.fragment = BroadBandFragment.newInstance();
            } else if ("设置手势密码".equals(this.title) || "G3YYT_17_10_18".equals(code)) {
              //  this.fragment = new GesturePasswordSetting();
            } else if ("宽带移机预约".equals(this.title) || "G3YYT_17_12_12".equals(code)) {
             //   this.fragment = MoveBroadbandPredetermine.newInstance();
            } else if ("现场开户".equals(this.title) || "G3YYT_11_16_22_3".equals(code)) {
             //   this.fragment = PartnerSelfChoseNumFragment.newInstance();
            } else if ("军人入网".equals(this.title) || "G3YYT_19_09_06".equals(code)) {
                this.title = "号码预占";
              //  this.fragment = SelfChoseNumFragmentJR.newInstance();
            }
            addFragment(this.fragment, TextUtils.isEmpty(this.title) ? "页面" : this.title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOrderCenterData(Intent intent) {
        String orderCenterData = intent.getStringExtra("orderCenterData");
        if (StringUtil.isNotEmpty(orderCenterData)) {
            this.workOrderId = new DataMap(orderCenterData).getString("workOrderId");
        } else {
            this.workOrderId = "9999";
        }
    }

    public String getOrderCenterData() {
        return this.workOrderId;
    }

    private void checkAccountTypeIsHaveAuthority(Intent intent) {
        String code = getCode(intent.getStringExtra(Constant.WadeMobileActivity.ICON));
        /*if (com.ai.cmcchina.crm.constant.Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(((App) getApplication()).getAccountChannelType()) && !"G3YYT_11_16_22".equals(code) && !"G3YYT_16_03_24".equals(code) && !"G3YYT_17_03_31".equals(code) && !"G3YYT_11_16_99".equals(code) && !"G3YYT_17_10_17".equals(code) && !"G3YYT_17_10_18".equals(code)) {
            UIUtil.alert(this, "抱歉，您的账号所属类型没有权限操作！", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface diglog, int arg1) {
                    diglog.dismiss();
                    BaseActivity.this.finish();
                }
            });
        }*/
    }

    private void initTitle() {
      /*  this.backBtn = (Button) findViewById(R.id.back_btn);
        this.titleTextV = (TextView) findViewById(R.id.titleTxt);*/
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                BaseActivity.this.popBackStack();
            }
        });
    }

    private String getCode(String iconName2) {
        if (!StringUtil.isBlank(iconName2) && iconName2.lastIndexOf(FileUtils.HIDDEN_PREFIX) > 0) {
            return iconName2.substring(0, iconName2.lastIndexOf(FileUtils.HIDDEN_PREFIX));
        }
        return iconName2;
    }

    /* renamed from: com.ai.cmcchina.crm.ui.BaseActivity$FragmentHolder */
    class FragmentHolder {
        BaseFragment fragment;
        String tag;

        FragmentHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        popBackStack();
    }

    @SuppressLint({"NewApi"})
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == 4) {
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            BaseFragment fragment2 = (BaseFragment) this.f938fm.findFragmentById(getViewId());
            if (fragment2 != null) {
                fragment2.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addFragment(BaseFragment fragment2, String tag) {
        try {
            currenTitleName = tag;
            this.titleTextV.setText(tag);
            FragmentTransaction ft = this.f938fm.beginTransaction();
            if (this != null) {
                CRMSystem.hideIME(this);
            }
            ft.replace(getViewId(), fragment2, tag);
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();
            this.f938fm.executePendingTransactions();
            return true;
        } catch (Exception e) {
           e.printStackTrace();
            return false;
        }
    }

    public void remove(BaseFragment fragment2) {
        FragmentTransaction ft = this.f938fm.beginTransaction();
        ft.remove(fragment2);
        ft.commit();
    }

    /* access modifiers changed from: protected */
    public int getViewId() {

        return 0;
    }

    public void popBackStack() {
        try {
            BaseFragment fragment2 = (BaseFragment) this.f938fm.findFragmentById(getViewId());
            if (fragment2 == null || !fragment2.showBack()) {
                finish();
            } else if (!this.f938fm.popBackStackImmediate()) {
            } else {
                if (this.f938fm.getBackStackEntryCount() > 0) {
                    BaseFragment fragment3 = (BaseFragment) this.f938fm.findFragmentById(getViewId());
                    this.titleTextV.setText(fragment3.getTag());
                   // Log.d(TAG, "popBackStack: 需要显示的fragment：\n" + fragment3.getClass().toString());
                    return;
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createInvoice(String orderCode, String busiType, String invoiceContent, Button btn) {
        if (StringUtil.isBlank(orderCode)) {
            UIUtil.toast((Context) this, (Object) "获取流水号失败，不能生成电子发票！");
            return;
        }
        Map<String, String> params = new HashMap<>();
        App app = (App) getApplication();
        params.put("custOrderId", orderCode);
 /*       params.put("keyNum", app.getNum());
        params.put("busiType", busiType);
        params.put("invoiceContent", invoiceContent);
        params.put("ghfNsrmc", app.getUserFullName());
        params.put("pushEmail", app.getNum() + "@139.com");
        params.put("optrid", app.getAccount());*/
        params.put("itemName", invoiceContent);
        creatInvoiceCallSvc(params, btn);
    }

    public void createInvoice(String orderCode, String busiType, String invoiceContent, String num, String name, Button btn) {
        if (StringUtil.isBlank(orderCode)) {
            UIUtil.toast((Context) this, (Object) "获取流水号失败，不能生成电子发票！");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("custOrderId", orderCode);
        params.put("keyNum", num);
        params.put("busiType", busiType);
        params.put("invoiceContent", invoiceContent);
        params.put("ghfNsrmc", name);
        params.put("pushEmail", num + "@139.com");
       // params.put("optrid", ((App) getApplication()).getAccount());
        params.put("itemName", invoiceContent);
        creatInvoiceCallSvc(params, btn);
    }

    private void creatInvoiceCallSvc(Map<String, String> params, Button btn) {
        final Button button = btn;
        new CRMRemoteHandler(this, "/front/sh/noPaperAction!createInvoice", params, true, false) {
            public void onSuccess(String result) {
                try {
                    JSONObject resultJson = JsonUtil.string2JSONObject(result);
                    String returnCode = JsonUtil.getStringFromJSONObject(resultJson, com.ai.cmcchina.crm.constant.Constant.RETURN_CODE_KEY, "9999");
                    String returnMessage = JsonUtil.getStringFromJSONObject(resultJson, "returnMessage", "电子发票生成失败，请重试！");
                    JSONObject beanObj = JsonUtil.getJsonFromJSON(resultJson, "bean");
                    if ("0000".equals(returnCode)) {
                        String invoiceID = JsonUtil.getStringFromJSONObject(beanObj, "INVOICE_INSTANCE_ID", "");
                        if ("".equals(invoiceID)) {
                            UIUtil.toast((Context) this.context, (Object) "电子发票生成失败，请重试！");
                            return;
                        }
                        UIUtil.alert((Context) this.context, "电子发票已发送到139电子邮箱，请注意查收！\n发票ID:" + invoiceID);
                        button.setEnabled(false);
                        button.setText("电子发票已发送成功");
                        return;
                    }
                    UIUtil.alert((Context) this.context, "电子发票发送失败\n" + returnMessage);
                } catch (Exception e) {
                   e.printStackTrace();
                    UIUtil.alert((Context) this.context, "电子发票生成失败，请重试！");
                }
            }
        }.start();
    }

    public void saveOrderCodeForBill(String billId, String orderCode) {
        if (!StringUtil.isBlank(billId) && !StringUtil.isBlank(orderCode)) {
            Map<String, String> params = new HashMap<>();
            params.put("billId", billId);
            params.put("orderCode", orderCode);
            new CRMRemoteHandler(this, "/front/sh/openAccountPay!addOrderCodeForBill", params, false) {
                public void onSuccess(String result) {
                  //  G3Logger.debug(result);
                }
            }.start();
        }
    }

    public void numSegmentContainClick(View view) {
       /* if (this.fragment instanceof SelfChoseNumFragment) {
            ((SelfChoseNumFragment) this.fragment).numSegmentContainClick(view);
        }*/
    }

    public void numContainClick(View view) {
        /*if (this.fragment instanceof SelfChoseNumFragment) {
            ((SelfChoseNumFragment) this.fragment).numContainClick(view);
        }*/
    }

    public void getIDCardInfo() {
       // startActivityForResult(new Intent(this, IdCardReaderActivity.class), BaseFragment.REQUEST_CODE);
    }

    public void takeIDCardPhotoAgain() {
    /*    Intent intent = new Intent(this, IdCardReaderActivity.class);
        intent.putExtra(IdCardReaderActivity.IS_TAKE_PHOTO_AGAIN, true);
        startActivityForResult(intent, BaseFragment.TACK_IDCARD_PHOTO_AGAION);*/
    }

    public void readIDCardNum() {
      /*  Intent intent = new Intent(this, IdCardReaderActivity.class);
        intent.putExtra(IdCardReaderActivity.IS_GET_IDCARD_NUM, true);
        startActivityForResult(intent, BaseFragment.GET_IDCARD_NUM);*/
    }

    public void openGuardianCertifyPage(String billId) {
      /*  Intent intent = new Intent(this, MultipleCustomWindowActivity.class);
        intent.putExtra("pageAction", "GuardianCertificationPage");
        IData data = new DataMap();
        data.put("billId", billId);
        intent.putExtra("data", data.toString());
        startActivityForResult(intent, BaseFragment.OPEN_GUARDIAN_PAGE);*/
    }

    public static void addBusinessRecordInfo(Activity activity, String busiName, String busiCode, String userName, String userTel) {
        if (isFromEsop(activity)) {
            com.alibaba.fastjson.JSONObject data = new com.alibaba.fastjson.JSONObject();
            data.put("MANAGER_ID", (Object) MemoryCacheUtil.getMemoryCache("managerId", "").toString());
            data.put("MANAGER_CODE", (Object) MemoryCacheUtil.getMemoryCache("managerCode", "").toString());
            data.put("REGION_ID", (Object) MemoryCacheUtil.getMemoryCache("regionId", "").toString());
            data.put("MANAGER_NAME", (Object) MemoryCacheUtil.getMemoryCache("managerName", "").toString());
            data.put("BUSINESS_NAME", (Object) busiName);
            data.put("BUSINESS_CODE", (Object) busiCode);
            data.put("PROCESSING_UNIT", (Object) userName);
            data.put("UNIT_CODE", (Object) userTel);
            data.put("busiCode", (Object) "SO_addBusinessRecordInfo_0");
            Map<String, String> params = new HashMap<>();
            params.put("data", data.toString());
            params.put(Constant.Server.ACTION, "RemoteAction.getRemoteData");
            new IPURemoteHandler(activity, "/mobiledata", params, "ESOP", false) {
                public void onSuccess(String result) {
                   /* G3Logger.debug("ESOP菜单点击量和业务办理量统计--成功: " + result);
                    if (!"0000".equals(((EsopResult) JSON.parseObject(result, EsopResult.class)).getReturnCode())) {
                    }*/
                }

                public void onError(String result) {
                    super.onError(result);
                    //G3Logger.debug("ESOP菜单点击量和业务办理量统计--失败: " + result);
                }
            }.start();
        }
    }

    public static boolean isFromEsop(Activity activity) {
        String result = new SharedPrefHelper(activity).get(Constant.MobileCache.WADE_MOBILE_STORAGE, com.ai.cmcchina.crm.constant.Constant.FromWhichSubsystem);
        if (StringUtil.isBlank(result)) {
            return false;
        }
        return "ESOP".equalsIgnoreCase(result);
    }

    public void addWatermark() {
        if (this.waterView != null) {
           // ((FrameLayout) findViewById(R.id.frame_layout)).removeView(this.waterView);
        }
       // this.waterView = ((App) getApplication()).addWatermark(this, (FrameLayout) findViewById(R.id.frame_layout));
    }
}