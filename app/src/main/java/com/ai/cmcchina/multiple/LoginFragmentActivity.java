package com.ai.cmcchina.multiple;


import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.location.BDLocation;
import com.ai.cmcchina.crm.constant.Constant;

import com.ai.cmcchina.crm.util.LocationUtil;
import com.ai.cmcchina.crm.util.PreferenceHelper;
import com.ai.cmcchina.crm.util.UIUtil;
import com.ai.cmcchina.esop.loc.LocationTool;
//import com.ai.cmcchina.multiple.fragment.login.PartnerLoginFragment;
import com.ai.cmcchina.multiple.fragment.login.SelfSupportChannelLoginFragment;
//import com.ai.cmcchina.multiple.fragment.login.SocietyChannelLoginFragment;

import com.heclient.heapp.R;
import com.tencent.tinker.loader.hotplug.EnvConsts;

import java.text.MessageFormat;

/* renamed from: com.ai.cmcchina.multiple.LoginFragmentActivity */
public class LoginFragmentActivity extends FragmentActivity implements LocationTool.LocationHandler {
    private BDLocation currentLocation;
    /* access modifiers changed from: private */
    public Location currentLocation2;
    private long exitTime = 0;
    private boolean noHijack = false;
   // private PartnerLoginFragment partnerlLoginFragment;
    private SelfSupportChannelLoginFragment selfSupportLoginFragment;
  //  private SocietyChannelLoginFragment societyLoginFragment;
    private int textSize;
    private int textSizeClick;
    private TextView tvPartnerl;
    private TextView tvSelfSuppor;
    private TextView tvSociety;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_login);
        initView();
        initData();
        setListener();
        getWindow().addFlags(8192);
        initLocation();
    }

    private void initLocation() {
        LocationUtil.getInstance(this).setOnLocationListener(new LocationUtil.CustomLocationListener() {
            public void onSuccess(Location location) {
                Location unused = LoginFragmentActivity.this.currentLocation2 = location;
                //G3Logger.debug("LocationUtil获取位置信息成功:" + location.getLongitude() + "--" + location.getLatitude());
            }

            public void onFailed(String msg) {
                //G3Logger.debug("LocationUtil:" + msg);
                UIUtil.toast((Context) LoginFragmentActivity.this, (Object) msg);
            }
        });
        LocationTool.getInstance(getApplicationContext()).startLocation();
        LocationTool.getInstance(getApplicationContext()).setLocationHandler(this);
    }

    private void initData() {
        this.selfSupportLoginFragment = new SelfSupportChannelLoginFragment();
//        this.societyLoginFragment = new SocietyChannelLoginFragment();
//        this.partnerlLoginFragment = new PartnerLoginFragment();
        String loginType = PreferenceHelper.get(Constant.APP_LOGIN_TYPE + getIntent().getStringExtra("username"), "");
        if (Constant.LOGIN_TYPE_SELF_SUPPORT_CHANNEL.equals(loginType)) {
            getFourALoginFragment();
        } else if (Constant.LOGIN_TYPE_SOCIETY_CHANNEL.equals(loginType)) {
            getEmptyLoginFragment();
        } else if (Constant.LOGIN_TYPE_PARTNER_CHANNEL.equals(loginType)) {
            getPartnerlLoginFragment();
        } else {
            getFourALoginFragment();
        }
    }

    private void initView() {
        this.tvSelfSuppor = (TextView) findViewById(R.id.tv_self_support_login);
        this.tvSociety = (TextView) findViewById(R.id.tv_society_channel_login);
        this.tvPartnerl = (TextView) findViewById(R.id.tv_partnerl_login);
        findViewById(R.id.login).setPadding(UIUtil.getFinalSize(this, 35.0f), 0, UIUtil.getFinalSize(this, 35.0f), 0);
        this.textSize = UIUtil.getFinalSize(this, 18.0f);
        this.textSizeClick = UIUtil.getFinalSize(this, 20.0f);
    }

    private void setListener() {
        this.tvSelfSuppor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoginFragmentActivity.this.getFourALoginFragment();
            }
        });
        this.tvSociety.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoginFragmentActivity.this.getEmptyLoginFragment();
            }
        });
        this.tvPartnerl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoginFragmentActivity.this.getPartnerlLoginFragment();
            }
        });
    }

    /* access modifiers changed from: private */
    public void getFourALoginFragment() {
        this.tvSelfSuppor.setTextColor(-1);
        this.tvSelfSuppor.setTextSize(0, (float) this.textSizeClick);
        this.tvSociety.setTextColor(-16777216);
        this.tvSociety.setTextSize(0, (float) this.textSize);
        this.tvPartnerl.setTextColor(-16777216);
        this.tvPartnerl.setTextSize(0, (float) this.textSize);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_login_framelayout, this.selfSupportLoginFragment);
        transaction.addToBackStack((String) null);
        transaction.commit();
    }

    /* access modifiers changed from: private */
    public void getEmptyLoginFragment() {
        this.tvSociety.setTextColor(-1);
        this.tvSociety.setTextSize(0, (float) this.textSizeClick);
        this.tvSelfSuppor.setTextColor(-16777216);
        this.tvSelfSuppor.setTextSize(0, (float) this.textSize);
        this.tvPartnerl.setTextColor(-16777216);
        this.tvPartnerl.setTextSize(0, (float) this.textSize);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
     //   transaction.replace(R.id.activity_login_framelayout, this.societyLoginFragment);
        transaction.addToBackStack((String) null);
        transaction.commit();
    }

    /* access modifiers changed from: private */
    public void getPartnerlLoginFragment() {
        this.tvPartnerl.setTextColor(-1);
        this.tvPartnerl.setTextSize(0, (float) this.textSizeClick);
        this.tvSelfSuppor.setTextColor(-16777216);
        this.tvSelfSuppor.setTextSize(0, (float) this.textSize);
        this.tvSociety.setTextColor(-16777216);
        this.tvSociety.setTextSize(0, (float) this.textSize);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      //  transaction.replace(R.id.activity_login_framelayout, this.partnerlLoginFragment);
        transaction.addToBackStack((String) null);
        transaction.commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (System.currentTimeMillis() - this.exitTime > 2000) {
            UIUtil.toast((Context) this, (Object) "再按一次退出程序");
            this.exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.noHijack && !((ActivityManager) getSystemService( Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName().equals(getPackageName())) {
            UIUtil.toast((Context) this, (Object) "正在离开登陆页面！");
            this.noHijack = false;
        }
        this.noHijack = true;
    }

    public void handleLocation(BDLocation paramBDLocation) {
        if (!LocationUtil.checkLocation(paramBDLocation)) {
            UIUtil.toast((Context) this, MessageFormat.format("经纬度信息异常：{0}--{1}", (Object)  paramBDLocation.getLatitude(), paramBDLocation.getLongitude()));
            return;
        }
        if (this.currentLocation == null) {
            UIUtil.toast((Context) this, (Object) "获取位置信息成功！");
        }
        this.currentLocation = paramBDLocation;
        //G3Logger.debug("获取位置信息成功：" + this.currentLocation.getLatitude() + "__" + this.currentLocation.getLongitude());
    }

    public BDLocation getCurrentLocation() {
        return this.currentLocation;
    }

    private void closeLocationUpdate() {
        try {
            LocationTool.getInstance(this).stopLocation();
        } catch (Exception e) {
            //G3Logger.debug(e);
        }
    }
}