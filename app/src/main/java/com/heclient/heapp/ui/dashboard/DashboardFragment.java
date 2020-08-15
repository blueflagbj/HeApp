package com.heclient.heapp.ui.dashboard;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ai.cmcchina.crm.util.UIUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.callback.JobManagerCallback;
import com.heclient.heapp.App;
import com.heclient.heapp.MainActivity;
import com.heclient.heapp.R;
import com.heclient.heapp.db.DaoSession;
import com.heclient.heapp.db.PhoneNum;
import com.heclient.heapp.db.PhoneNumDao;
import com.heclient.heapp.db.ResultInfo;
import com.heclient.heapp.db.ResultInfoDao;
import com.heclient.heapp.jobs.CustomerLoginJob;
import com.heclient.heapp.jobs.ReadWriteCsvJob;
import com.heclient.heapp.ui.home.HomeFragment;
import com.heclient.heapp.util.AsyncWriteXls;
import com.heclient.heapp.util.EventBusDoneResult;
import com.heclient.heapp.util.ExcelHelper;
import com.heclient.heapp.util.Phone;
import com.heclient.heapp.util.PhoneNumAdapter;
import com.heclient.heapp.xlstable.XlsCells;
import com.heclient.heapp.xlstable.XlsTable;
import com.wade.mobile.app.AppRecord;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.app.MobileOperation;
import com.wade.mobile.common.MobileThread;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.IWadeMobileClient;
import com.wade.mobile.frame.WadeMobileApplication;
import com.wade.mobile.frame.WadeWebView;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.frame.plugin.PluginManager;
import com.wade.mobile.frame.template.ResVersionManager;
import com.wade.mobile.frame.template.TemplateManager;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.ui.build.dialog.progressdialog.SimpleProgressDialog;
import com.wade.mobile.ui.comp.dialog.ConfirmDialog;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.ui.view.FlipperLayout;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Messages;
import com.wade.mobile.util.http.HttpTool;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public  class DashboardFragment extends Fragment implements IWadeMobile {
    public static final String TAG = "dashboardFragment";
    private DashboardViewModel dashboardViewModel;
    private String xlsPath = null;
    private List<PhoneNum> phoneNumList =null;
    private PhoneNumAdapter phoneNumAdapter=null;
    private View root = null;
    private Context mContext;
    private Boolean showed=false;
    private ListView listView=null;
    private Button buttonLoadXls =null;
    private Button buttonDoing=null;
    private Button buttonstop=null;

    private JobManager jobManager;
    private CustomerLoginJob customerLoginJob;
    private ReadWriteCsvJob readWriteCsvJob;
    private static Bundle outState;
    private TextView textViewDashboard;

    private DaoSession daoSession;
    private PhoneNumDao phoneNumDao;
    private Query<PhoneNum> phoneNumQuery=null;
    private Query<PhoneNum> query=null;
    private LazyList<PhoneNum> phoneNumLazyList;

    private int taskNum=0; //每批的任务数量
    private int tasksID=0;//批次
    private boolean isRunning=false;//执行标志

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                   // updateClient();
                    return;
                case 1:
                    updateResource();
                    return;
                case 2:
                    if (LoadingDialogStyle.HORIZONTAL.equals(getLoadingDialogStyle())) {
                        updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }
                    return;
                case 3:
                    if (updateResProgressDialog != null) {
                        updateResProgressDialog.dismiss();
                        return;
                    }
                    return;
                case 4:
                  //  TemplateSubActivity.this.initWebView();
                    return;
                case 5:
                  //  TemplateSubActivity.this.setContentView(TemplateSubActivity.this.mainLayout);
                    String result5 = String.valueOf(msg.obj);
                    dashboardViewModel.setText(result5);
                    return;
                case 6:

                    String result6 = String.valueOf(msg.obj);
                    dashboardViewModel.setText(result6);

                    return;
                case 7:
                     if (phoneNumAdapter != null) {
                        phoneNumAdapter=null;
                    }
                    phoneNumAdapter = new PhoneNumAdapter(mContext, R.layout.phone_item, phoneNumList);
                    // 将适配器上的数据传递给listView
                    //final ListView listView =(ListView)v.findViewById(R.id.lv);
                    if (listView != null) {
                        listView.setAdapter(phoneNumAdapter);
                    }
                    //phoneNumAdapter.notifyDataSetChanged();//通知数据更新
                    dashboardViewModel.setText("数据获取完成...");
                    return;
                case 8:
                    String string=(String)msg.obj;
                    dashboardViewModel.setText(string);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String isForceUpdate;
    /* access modifiers changed from: private */
    public long loadingTime = Long.parseLong(MobileConfig.getInstance().getConfigValue("loading_time", "2000"));
    /* access modifiers changed from: private */
    public ProgressDialog updateResProgressDialog;

    protected enum LoadingDialogStyle {
        SPINNER,
        HORIZONTAL
    }
/*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }*/
    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        if(root==null){
            root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        }
        EventBus.getDefault().register(this); //
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.jobManager=null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListener();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
       super.onSaveInstanceState(outState);

    }
    @Override
    public void onPause() {
        super.onPause();//通常是当前的acitivty被暂停了，比如被另一个透明或者Dialog样式的Activity覆盖了
        //  System.out.println("HomeFragment-onPause");
        super.onPause();
        if (outState != null) {
            outState.clear();
            outState = null;
        }
        outState = new Bundle();

       // outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) phoneNumList);
/*      String inputUserNameStr = inputUserName.getText().toString();
        outState.putString("inputUserName",inputUserNameStr);
        String inputPasswordStr=inputPassword.getText().toString();
        outState.putString("inputPassword",inputPasswordStr);
        outState.putBoolean("loginIsEnable",buttonLogin.isEnabled());//
        System.out.println("状态保存：名称"+inputUserNameStr+"密码："+inputPasswordStr);*/


    }

    @Override
    public void onResume() {
        super.onResume();//被激活
        if(outState!=null){
         /*   String inputUserNameStr= outState.getString("inputUserName");
            inputUserName.setText(inputUserNameStr);
            String inputPasswordStr=outState.getString("inputPassword");
            inputPassword.setText(inputPasswordStr);
            boolean isEnable=outState.getBoolean("loginIsEnable");//
            buttonLogin.setEnabled(isEnable);
            System.out.println("恢复：名称"+inputUserNameStr+"密码："+inputPasswordStr);*/
           // listView.setAdapter(phoneNumAdapter);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusDoneResult result) {
        //接收以及处理数据
        String S=result.getResult();
        JSONObject backStr =JSON.parseObject(S);
        String taskTag=backStr.getString("taskTag");
        switch (taskTag) {
            case "requestLogin":
                String backInfo =backStr.getString("backInfo");
                String resultStr=backStr.getString("backResult");
                int backSize =backStr.getInteger("backSize"); //总数
                int backID =backStr.getInteger("backID"); //总数
                phoneNumList.get(backID).setIsDone(1);
                if(phoneNumAdapter!=null){
                    phoneNumAdapter.notifyDataSetChanged();
                }

                taskNum=taskNum-1;
                dashboardViewModel.setText(backInfo+" 已完成...");
                return;
            case "importCsv":
                String backInfos =backStr.getString("backInfo");
                dashboardViewModel.setText(backInfos);
                return;
            case "2":

                return;
            default:
                return;
        }

    };
    private void initView(){
        this.listView = root.findViewById(R.id.lv);
        this.buttonDoing = root.findViewById(R.id.button_doing);
        this.buttonLoadXls = root.findViewById(R.id.button_loadXls);
        this.buttonstop = root.findViewById(R.id.button_stop);
        this.mContext = this.getActivity();
        this.jobManager = App.getInstance().getJobManager(); //
        /*this.phoneNumDao = App.getInstance().getDaoSession().getPhoneNumDao();*/
        this.textViewDashboard = root.findViewById(R.id.text_Dashboard);

        initBasePath();
       // update();
    }
    private void setListener(){
        //导入数据
        this.buttonLoadXls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //导入csv
            // importCsv(mContext);
                //创建runnnable实例 任务处理
                ImportCsvRunnable importCsvRunnable=new ImportCsvRunnable();
                //创建Thread实例并将runnable实例放入
                Thread importCsvRunnableThread=new Thread(importCsvRunnable,"importCsvRunnableThread");
                importCsvRunnableThread.start();
            }
        });
        //开始处理按钮事件
       this.buttonDoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //打开处理数据
          //  ShowList();

            //创建runnnable实例 任务处理
             TaskRunnable taskRunnable=new TaskRunnable();
            //创建Thread实例并将runnable实例放入
             Thread taskThread=new Thread(taskRunnable,"taskThread");
             isRunning=true;
             taskThread.start();
            }
        });

        //停止处理按钮事件
        this.buttonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 isRunning=false;
                 if(jobManager!=null){
                     jobManager.stop();
                 }

            }
        });


        this.dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               textViewDashboard.setText(s);
            }
        });
        //设置job后台任务回调 ，前台显示，这部分考虑数据处理是否完全放回后台写入数据库，前端只是显示处理数据
        this.jobManager.addCallback(new JobManagerCallback() {
            @Override
            public void onJobAdded(@NonNull Job job) {

            }
            @Override
            public void onJobRun(@NonNull Job job, int resultCode) {

            }

            @Override
            public void onJobCancelled(@NonNull Job job, boolean byCancelRequest, @Nullable Throwable throwable) {

            }
            @Override
            public void onDone(@NonNull Job job) {
                if (job instanceof CustomerLoginJob) {
                    CustomerLoginJob a = (CustomerLoginJob) job;
                    String result=a.getResult();
                    int inid=a.getInId();
                  //Log.d(a.getTag(),job.getId()+" onDone : "+result);
                    EventBus.getDefault().post(new EventBusDoneResult(result));
                }
                if (job instanceof ReadWriteCsvJob) {
                    ReadWriteCsvJob b = (ReadWriteCsvJob) job;
                    String result=b.getResult(); //
                    EventBus.getDefault().post(new EventBusDoneResult(result));
                }
            }
            @Override
            public void onAfterJobRun(@NonNull Job job, int resultCode) {
            }
        });
    }
    private void doTask(int limit){
        String sessionId =App.getInstance().getSessionId();
        Message msg=new Message();
        msg.what=8;//msg.what的类型是int型，作为msg的识别码
        msg.arg1=1;//msg.arg1的类型是int型,可以传递简单的参数
        msg.arg2=2;//msg.arg2的类型是int型，可以传递简单的参数
        if (sessionId != null) {
            daoSession =App.getInstance().getDaoSession();
            phoneNumDao = daoSession.getPhoneNumDao();
            QueryBuilder<PhoneNum> queryBuilder =phoneNumDao.queryBuilder().limit(limit);
            queryBuilder.where(PhoneNumDao.Properties.IsDone.eq(0));
            //queryBuilder.orderRaw("T.'TIME' ASC");
            phoneNumQuery= queryBuilder.build();
            phoneNumList=phoneNumQuery.list();
            handler.sendEmptyMessage(7);//通知更新列表
            int size = phoneNumList.size();
            String dataAction = "MainPage.customerLogin_new";
            if(size>0){ //有需要处理的数据
                for (int i = 0; i < size; i++) {
                    PhoneNum phone=phoneNumList.get(i);
                    String backInfo="第"+tasksID+"批,共"+size+"条,第"+(i+1)+":"+phone.getPhonenum();
                    // System.out.println("DashboardFragment-phoneClick:"+backInfo);
                    String phoneNum =phone.getPhonenum();
                    int phoneid = phone.getPhoneid();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("phone_id", phoneid);
                    jsonObject.put("phone_num", phoneNum);
                    jsonObject.put("SESSION_ID", sessionId);
                    JSONObject backObject = new JSONObject();
                    backObject.put("backInfo",backInfo);
                    backObject.put("taskTag","requestLogin");
                    backObject.put("backSize",size); //总数
                    backObject.put("backID",i);//序号
                    backObject.put("phoneID",phone.getId());//号码ID
                    String param =jsonObject.toJSONString();
                    String back=backObject.toJSONString();
                    // System.out.println("DashboardFragment-param:0:" + param);
                    try {
                        requestCustomerLoginJob(dataAction, param,back); //
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else{
                isRunning=false; //停止执行
                msg.obj="没有需要处理的数据，请先导入数据！";//msg.obj是Object型，可以传递任意参数
                handler.sendMessage(msg);
            }
        } else {
            isRunning=false;//停止执行任务


            msg.obj="获取sessionid信息失败，请登陆后重试！";//msg.obj是Object型，可以传递任意参数
            handler.sendMessage(msg);

        }
    }
    public void ShowList(){

        daoSession =App.getInstance().getDaoSession();
        phoneNumDao = daoSession.getPhoneNumDao();
        QueryBuilder<PhoneNum> queryBuilder =phoneNumDao.queryBuilder().limit(10); //显示10000条
        queryBuilder.where(PhoneNumDao.Properties.IsDone.eq(0));
        // queryBuilder.orderRaw("T.'TIME' ASC");
        phoneNumQuery= queryBuilder.build();
        phoneNumList  =phoneNumQuery.list();
       // LazyList<PhoneNum> list =phoneNumLazyList(0);//懒加载
        phoneNumAdapter = new PhoneNumAdapter(mContext, R.layout.phone_item, phoneNumList);
        if (phoneNumAdapter != null) {
            // 将适配器上的数据传递给listView
            //final ListView listView =(ListView)v.findViewById(R.id.lv);
            if (listView != null) {
                listView.setAdapter(phoneNumAdapter);
            }
        }

    }
    public LazyList<PhoneNum> phoneNumLazyList(long isdone) {

        synchronized (this) {
            if (phoneNumQuery == null) {
               // System.out.println("aaaaaa000");
                QueryBuilder<PhoneNum> queryBuilder =phoneNumDao.queryBuilder();
                queryBuilder.where(PhoneNumDao.Properties.IsDone.eq(0));
               // queryBuilder.orderRaw("T.'TIME' ASC");
                phoneNumQuery= queryBuilder.build();

            }
        }
        query = phoneNumQuery.forCurrentThread();
        query.setParameter(0, isdone);
        return query.listLazy();
    }


    public void initBasePath() {
        TemplateManager.initBasePath(MobileAppInfo.getSdcardAppPath(this.mContext) + File.separator);
    }

    private void importCsv(Context context) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //为真则SD卡已装入，读取SD卡中文件
            String csvPath = Environment.getExternalStorageDirectory().getPath() + "/Pictures/phonecsv.csv";
          //  System.out.println("SD卡csv路径:" + csvPath);
            String dbPath =context.getDatabasePath("he-db").getAbsolutePath();
          //  System.out.println("SD卡db路径:" + dbPath);
            JSONObject reqData=new JSONObject();
            reqData.put("taskTag","importCsv");
            reqData.put("tag","readwriteCsv");//
            reqData.put("type","readCsv"); //类型
            JSONObject csvData =new JSONObject();
            csvData.put("csvPath",csvPath);//csv文件路径
            String sql="insert into phone_num(PHONEID,PHONENUM,IS_DONE) values(?,?,?)";
            csvData.put("sql",sql);
            reqData.put("csvData",csvData.toJSONString());
           // System.out.println("SD卡reqData:" +reqData.toJSONString());
            readWriteCsvJob=new ReadWriteCsvJob(reqData.toJSONString());

            jobManager.addJobInBackground(readWriteCsvJob);

        }
    }
    private void requestCustomerLoginJob(String dataAction, String param, String backInfo) throws Exception {

/*      System.out.println("Dashboard-requestBizData:0:" + dataAction);
        System.out.println("Dashboard-requestBizData:1:" + param);
        System.out.println("Dashboard-requestBizData:2:" + backInfo);*/
        JSONObject reqData=new JSONObject();
        reqData.put("tag","reqhttp");
        reqData.put("backInfo",backInfo);
        reqData.put("dataAction",dataAction);//
        reqData.put("param",param);

        customerLoginJob = new CustomerLoginJob(reqData.toJSONString());
        jobManager.addJobInBackground(customerLoginJob);

    }

    public String getResKey() throws Exception {
      //  System.out.println("TemplateSubActivity-getResKey:0:");
        Map<String, String> postParam = new HashMap<>();
        postParam.put(Constant.Server.ACTION, Constant.MobileSecurity.RES_KEY_ACTION);
        MobileSecurity.init(this.getActivity());
        postParam.put(Constant.Server.KEY, MobileSecurity.getDesKey());

        String url =MobileConfig.getInstance().getRequestUrl();
       // System.out.println("TemplateSubActivity-getResKey:1"+url);
        String param= HttpTool.toQueryStringWithEncode(postParam);
       // System.out.println("TemplateSubActivity-getResKey:2"+param);
        String req=HttpTool.httpRequest(url, param, "POST","TemplateSubActivity-getResKey0");
       // System.out.println("TemplateSubActivity-getResKey:3"+req);
        req=MobileSecurity.responseDecrypt(req);
      //  System.out.println("TemplateSubActivity-getResKey:4:"+req);
        JSONObject jsonObject = JSONObject.parseObject(req);
        String reqStr =jsonObject.getString("KEY");
        return reqStr;
    }
    private void update() {
        new MobileThread("subUpdate") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                long start = System.currentTimeMillis();
                String resKey = DashboardFragment.this.getResKey();
                if (resKey != null) {
                    TemplateManager.initResKey(resKey);
                }
                if (ResVersionManager.isUpdateResource(DashboardFragment.this.getActivity(), ResVersionManager.getRemoteResVersions())) {
                    DashboardFragment.this.handler.sendEmptyMessage(1);
                    return;
                }
                long period = System.currentTimeMillis() - start;
                if (period < DashboardFragment.this.loadingTime) {
                    Thread.sleep(DashboardFragment.this.loadingTime - period);
                }
                DashboardFragment.this.handler.sendEmptyMessage(4);
            }

        }.start();
    }
    public void updateResource() {
        if (isHintWithUpdateRes()) {
            new ConfirmDialog(this.getActivity(), getHintTitleWithUpdateRes(), getHintInfoWithUpdateRes()) {
                /* access modifiers changed from: protected */
                public void okEvent() {
                    super.okEvent();
                    updateRes();
                }

                /* access modifiers changed from: protected */
                public void cancelEvent() {
                    if (isSkipUpdateRes()) {
                        if (AppRecord.isFirst(DashboardFragment.this.getActivity())) {
                            onBackPressed();
                        }
                        handler.sendEmptyMessage(3);
                        handler.sendEmptyMessage(4);

                        return;
                    }
                    onBackPressed();
                }
            }.show();
        } else {
            updateRes();
        }
    }
    private void updateRes() {
      //  System.out.println("TemplateSubActivity-updateRes:0:");
        this.updateResProgressDialog = createUpdateResProgressDialog();
        this.updateResProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                HintHelper.tip(getActivity(), "应用退出,请重新启动");
                MobileOperation.exitApp();
            }
        });
        this.updateResProgressDialog.show();
        new MobileThread("subUpdateResource") {
            /* access modifiers changed from: protected */
            public void execute() throws Exception {
                TemplateManager.resetDownloadPercent();
                TemplateManager.downloadResource(DashboardFragment.this.handler, DashboardFragment.this.getActivity());
                DashboardFragment.this.handler.sendEmptyMessage(3);
                DashboardFragment.this.handler.sendEmptyMessage(4);
            }

        }.start();
    }

    public boolean isSkipUpdateRes() {
        return false;
    }
    public boolean isHintWithUpdateRes() {
        return true;
    }
    public String getHintTitleWithUpdateRes() {
        return "资源更新";
    }

    public String getHintInfoWithUpdateRes() {

        return "远端发现新资源,是否更新";
    }
    public void onBackPressed() {
        FlipperLayout flipperLayout2 = getFlipperLayout();
        if (flipperLayout2 == null || !flipperLayout2.isCanBack()) {
            this.getActivity().finish();
             //getWadeMobileClient().shutdownByConfirm(Messages.CONFIRM_CLOSE);
        } else {
            flipperLayout2.back();
        }
    }
    /* access modifiers changed from: protected */
    public ProgressDialog createUpdateResProgressDialog() {
        SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(this.getActivity()).setMessage(Messages.RES_INIT);
        if (LoadingDialogStyle.HORIZONTAL.equals(getLoadingDialogStyle())) {
            simpleProgressDialog.setProgressStyle(1);
            simpleProgressDialog.getProgressDialog().setMax(ResVersionManager.updateCount);
            simpleProgressDialog.getProgressDialog().getWindow().setGravity(17);
        }
        return simpleProgressDialog.build();
    }
    public LoadingDialogStyle getLoadingDialogStyle() {
        return LoadingDialogStyle.SPINNER;
    }

    @Override
    public WadeWebView getCurrentWebView() {
        return null;
    }

    @Override
    public FlipperLayout getFlipperLayout() {
        return null;
    }

    @Override
    public boolean getKeepRunning() {
        return false;
    }

    @Override
    public ViewGroup getMainLayout() {
        return null;
    }

    @Override
    public PluginManager getPluginManager() {
        return null;
    }

    @Override
    public IWadeMobileClient getWadeMobileClient() {
        return null;
    }

    @Override
    public void startActivityForResult(Plugin plugin, Intent intent, int i) {

    }
   //开始执行处理任务
    private class TaskRunnable implements  Runnable {
         //线程开始后，检测isRunning 标志，每秒检测一次
        @Override
        public void run() {
            //检测停止标志
            while (isRunning) {
                if(taskNum<=0){
                    tasksID=tasksID+1; //执行批次
                    taskNum=1000;
                    doTask(1000); //
                }else{
                    //每1秒检测该批是否执行完毕
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //导入文件线程
    private class ImportCsvRunnable implements  Runnable{
        @Override
        public void run() {
            String csvPath = Environment.getExternalStorageDirectory().getPath() + "/Pictures/phonecsv.csv";
            String sql="insert into phone_num(PHONEID,PHONENUM,IS_DONE) values(?,?,?)";

            Message msg=new Message();
            msg.what=8;//msg.what的类型是int型，作为msg的识别码
            msg.arg1=1;//msg.arg1的类型是int型,可以传递简单的参数
            msg.arg2=2;//msg.arg2的类型是int型，可以传递简单的参数
            try {

                DaoSession daoSession = App.getInstance().getDaoSession();
                if(daoSession!=null){
                    Database db=daoSession.getDatabase();
                    DatabaseStatement stat = db.compileStatement(sql);
                    File file=new File(csvPath);
                    if (file.exists()) {
                        // FileInputStream fiStream;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"));  // 防止出现乱码
                        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                        long stime= SystemClock.currentThreadTimeMillis();
                        db.beginTransaction();
                        for (CSVRecord csvRecord : csvRecords) {
                        /*ApacheBean apacheBean = new ApacheBean();
                        apacheBean.setId(Integer.parseInt(csvRecord.get("id")));
                        apacheBean.setName(csvRecord.get("name"));
                        apacheBean.setAge(Integer.parseInt(csvRecord.get("age")));
                        mList.add(apacheBean);*/
                            stat.bindLong(1, Integer.parseInt(csvRecord.get("phoneid")));
                            stat.bindString(2,csvRecord.get("phonenum"));
                            stat.bindLong(3,0);
                            stat.executeInsert();
                        }
                        db.setTransactionSuccessful();
                        db.endTransaction();

                        //db.close(); //关闭数据源
                        stime=SystemClock.currentThreadTimeMillis()-stime;

                        msg.obj= "导入数据处理完毕，耗时："+stime+" 毫秒！";

                    }else{
                        msg.obj= "导入数据模板不存在，请检查后再行操作！";
                    }
                }else{
                    msg.obj= "导入时，获取缓存数据库实例异常...";
                }

            } catch (IOException e) {
                msg.obj= "导入异常...";
                e.printStackTrace();
            }

            handler.sendMessage(msg);


        }
    }


}