package com.heclient.heapp.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ai.cmcchina.crm.util.UIUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.callback.JobManagerCallback;
import com.heclient.heapp.App;
import com.heclient.heapp.R;
import com.heclient.heapp.db.DaoMaster;
import com.heclient.heapp.db.DaoSession;
import com.heclient.heapp.db.PhoneNum;
import com.heclient.heapp.db.PhoneNumDao;
import com.heclient.heapp.db.ResultInfo;
import com.heclient.heapp.db.ResultInfoAdapter;
import com.heclient.heapp.db.ResultInfoDao;
import com.heclient.heapp.jobs.CustomerLoginJob;
import com.heclient.heapp.jobs.ReadWriteCsvJob;
import com.heclient.heapp.ui.dashboard.DashboardFragment;
import com.heclient.heapp.util.AsyncWriteXls;
import com.heclient.heapp.util.EventBusDoneResult;
import com.heclient.heapp.util.PhoneNumAdapter;
import com.heclient.heapp.xlstable.XlsCells;
import com.heclient.heapp.xlstable.XlsTable;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationsFragment extends Fragment {
    public static final String TAG = "notificationsFragment";
    private NotificationsViewModel notificationsViewModel;
    private View root =null;
    private TextView textViewNotifications;
    private Button button_query =null;
    private Button buttonOutXls =null;
    private ListView listViewResult =null;
    private Button buttonClean=null;
    private Context mContext;
    private DaoSession daoSession;
    private ResultInfoDao resultInfoDao = null; //
    private ResultInfoAdapter resultInfoAdapter=null;
    private String csvPath="";

    private Query<ResultInfo> resultInfoQuery=null;
    private Query<ResultInfo> query=null;
    private LazyList<ResultInfo> resultInfoLazyList;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String string=(String)msg.obj;
                    notificationsViewModel.setText(string);
                    return;
                case 1:
                    //updateResource();
                    return;
                case 2:
                  /*  if (TemplateSubActivity.LoadingDialogStyle.HORIZONTAL.equals(TemplateSubActivity.this.getLoadingDialogStyle())) {
                        TemplateSubActivity.this.updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }*/
                    return;
                case 3:
                  /*  if (TemplateSubActivity.this.updateResProgressDialog != null) {
                        TemplateSubActivity.this.updateResProgressDialog.dismiss();
                        return;
                    }*/
                    return;
                case 4:

                    return;
                case 5:
                 //  NotificationsFragment.this.setContentView(NotificationsFragment.this.mainLayout);
                    return;
                default:
            }
        }
    };
    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        if(root==null){
            root = inflater.inflate(R.layout.fragment_notifications, container, false);
        }

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清空回调 messages
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setListener();
    }
    private void initView(){
        this.mContext = this.getActivity();
        this.textViewNotifications=root.findViewById(R.id.text_notifications);
        this.listViewResult = root.findViewById(R.id.lv_result);
        this.buttonOutXls = root.findViewById(R.id.button_outXls);
        this.buttonClean=root.findViewById(R.id.button_clean);
        this.button_query = root.findViewById(R.id.button_query);

    }
    private void setListener(){
        /*设置查询按钮监听事件*/
        this.button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //
             //   System.out.println("NotificationFragment:0:");
                daoSession =App.getInstance().getDaoMaster().newSession();
                resultInfoDao = daoSession.getResultInfoDao();
                // resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao();
                QueryBuilder<ResultInfo> queryBuilder =resultInfoDao.queryBuilder();
                // queryBuilder.where(PhoneNumDao.Properties.IsDone.eq(0));
                // queryBuilder.orderRaw("T.'TIME' ASC");
                Query<ResultInfo> resultInfoQuery= queryBuilder.build();
                resultInfoLazyList  =resultInfoQuery.listLazy();//懒加载
                resultInfoAdapter = new ResultInfoAdapter(mContext, R.layout.resultinfo_item, resultInfoLazyList);
                /*resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao();
                List<ResultInfo> resultInfos= resultInfoDao.loadAll();
                resultInfoAdapter = new ResultInfoAdapter(mContext, R.layout.resultinfo_item, resultInfos);*/
                // 将适配器上的数据传递给listView
                //  final ListView listView =(ListView)v.findViewById(R.id.lv);
                if (listViewResult!= null) {
                    listViewResult.setAdapter(resultInfoAdapter);
                    EventBus.getDefault().post(new EventBusDoneResult("查询成功..."));
                   // UIUtil.toast(getActivity(),"查询成功...");
                }

            }
        });
        /*设置清空按钮监听事件*/
        this.buttonClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultInfoLazyList.close();
                resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao();
                resultInfoDao.deleteAll();

               // resultInfoAdapter.clear();
                PhoneNumDao  phoneNumDao=App.getInstance().getDaoSession().getPhoneNumDao();
                phoneNumDao.deleteAll();
                EventBus.getDefault().post(new EventBusDoneResult("清理成功..."));
                //UIUtil.toast(getActivity(),"清理成功...");
                //List<ResultInfo> resultInfos= resultInfoDao.loadAll();
            }
        });
        /*设置导出数据监听事件*/
       this.buttonOutXls.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // outxls();
               csvPath = Environment.getExternalStorageDirectory().getPath() + "/Pictures/";
               UIUtil.toast(getActivity(),"数据导出路径："+csvPath);
/*               JSONObject reqData=new JSONObject();
               reqData.put("taskTag","OutCsv");
               reqData.put("type","writeCsv"); //类型
               reqData.put("tag","readwriteCsv");//
               reqData.put("csvPath",csvPath);
               System.out.println("SD卡reqData:" +reqData.toJSONString());*/
                //创建runnnable实例 任务处理
               ExportCsvRunnable exportCsvRunnable=new ExportCsvRunnable();
               //创建Thread实例并将runnable实例放入
               Thread exportCsvThread=new Thread(exportCsvRunnable,"exportCsvThread");
               exportCsvThread.start();
           }
       });
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewNotifications.setText(s);
            }
        });

    }
    public void ShowList(){
        resultInfoDao =  App.getInstance().getDaoMaster().newSession().getResultInfoDao();
       //resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao();
        QueryBuilder<ResultInfo> queryBuilder =resultInfoDao.queryBuilder();
       //queryBuilder.where(PhoneNumDao.Properties.IsDone.eq(0));
        // queryBuilder.orderRaw("T.'TIME' ASC");
        Query<ResultInfo> resultInfoQuery= queryBuilder.build();

        LazyList<ResultInfo> list =resultInfoQuery.listLazy();//懒加载

        resultInfoAdapter = new ResultInfoAdapter(mContext, R.layout.resultinfo_item, list);
        if (resultInfoAdapter != null) {
            // 将适配器上的数据传递给listView
            //final ListView listView =(ListView)v.findViewById(R.id.lv);
            if (listViewResult != null) {
                listViewResult.setAdapter(resultInfoAdapter);
            }
        }

    }
    public LazyList<ResultInfo> resultInfoLazyList(long isdone) {
        synchronized (this) {
            if (resultInfoQuery == null) {
               // resultInfoQuery=resultInfoDao.
                List<ResultInfo> resultInfos=resultInfoDao.loadAll();
            }
        }
        query = resultInfoQuery.forCurrentThread();
       // query.setParameter(0, isdone);
        return query.listLazy();
    }
    //实现Runnable接口并重写run方法
    private class ExportCsvRunnable implements  Runnable {
        //线程开始后，检测isRunning 标志，每秒检测一次
        @Override
        public void run() {
            //检测停止标志
            Message msg=new Message();
            // 也可以用下面两种方法获得Message
            // Message msg1=Message.obtain();
            // Message msg2=handler.obtainMessage();
            msg.what=0;//msg.what的类型是int型，作为msg的识别码
            msg.arg1=1;//msg.arg1的类型是int型,可以传递简单的参数
            msg.arg2=2;//msg.arg2的类型是int型，可以传递简单的参数
            try {
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String filepath = csvPath+ "IsDone"+time+".csv";
                System.out.println("SD卡:" + filepath);
                File file = new File(filepath);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));  // 防止出现乱码
                // 添加头部
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("_id","X_RESULTINFO","returnCode","user_name","returnMsg",
                        "user_full_name","phone","isVip","preSave","currentOfferId","state"," starLevelCreditLimit","brandName",
                        "brandId","username","balOrgName","brandCode","historyCost","validateLevel","createDate",
                        "offerName","regionId","noticeFlag","banlance","balOrgId","regionName","cost","is4G","starLevel","point","currentOweFee",
                        "starLevelValue","age","broadbandSpeed","amountCredit","X_RESULTCODE","X_RECORDNUM"));
                // 添加内容
                long stime= SystemClock.currentThreadTimeMillis();
                ResultInfoDao resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao();
                List<ResultInfo> resultInfos= resultInfoDao.loadAll();

                for (int i = 0; i < resultInfos.size(); i++) {
                    csvPrinter.printRecord(
                            resultInfos.get(i).get_id(),
                            resultInfos.get(i).getX_RESULTINFO(),
                            resultInfos.get(i).getReturnCode(),
                            resultInfos.get(i).getUser_name(),
                            resultInfos.get(i).getReturnMsg(),
                            resultInfos.get(i).getUser_full_name(),
                            resultInfos.get(i).getPhone(),
                            resultInfos.get(i).getIsVip(),
                            resultInfos.get(i).getPreSave(),
                            resultInfos.get(i).getCurrentOfferId(),
                            resultInfos.get(i).getState(),
                            resultInfos.get(i).getStarLevelCreditLimit(),
                            resultInfos.get(i).getBrandName(),
                            resultInfos.get(i).getBrandId(),
                            resultInfos.get(i).getUsername(),
                            resultInfos.get(i).getBalOrgName(),
                            resultInfos.get(i).getBrandCode(),
                            resultInfos.get(i).getHistoryCost(),
                            resultInfos.get(i).getValidateLevel(),
                            resultInfos.get(i).getCreateDate(),
                            resultInfos.get(i).getOfferName(),
                            resultInfos.get(i).getRegionId(),
                            resultInfos.get(i).getNoticeFlag(),
                            resultInfos.get(i).getBanlance(),
                            resultInfos.get(i).getBalOrgId(),
                            resultInfos.get(i).getRegionName(),
                            resultInfos.get(i).getCost(),
                            resultInfos.get(i).getIs4G(),
                            resultInfos.get(i).getStarLevel(),
                            resultInfos.get(i).getPoint(),
                            resultInfos.get(i).getCurrentOweFee(),
                            resultInfos.get(i).getStarLevelValue(),
                            resultInfos.get(i).getAge(),
                            resultInfos.get(i).getBroadbandSpeed(),
                            resultInfos.get(i).getAmountCredit(),
                            resultInfos.get(i).getX_RESULTCODE(),
                            resultInfos.get(i).getX_RECORDNUM()
                    );
                }
                csvPrinter.printRecord();
                csvPrinter.flush();
                stime=SystemClock.currentThreadTimeMillis()-stime;

                msg.obj="导出数据处理完毕，耗时："+stime+" 毫秒！";//msg.obj是Object型，可以传递任意参数
                //将Message发送给handler


            } catch (IOException e) {

                msg.obj="导出异常...";//msg.obj是Object型，可以传递任意参数
                e.printStackTrace();
            }
            handler.sendMessage(msg);
        }
    }
    public void StoreResultInfo(String str){
        JSONObject jsonObject =JSON.parseObject(str);
        // System.out.println(jsonObject.toJSONString());
        ResultInfoDao resultInfoDao = App.getInstance().getDaoSession().getResultInfoDao(); //
        ResultInfo resultInfo=new ResultInfo();
        resultInfo.setX_RESULTINFO(jsonObject.getString("X_RESULTINFO"));
        String returnCode=jsonObject.getString("returnCode");
        resultInfo.setReturnCode(returnCode);
        resultInfo.setReturnMsg(jsonObject.getString("returnMsg"));
        resultInfo.setX_RESULTCODE(jsonObject.getString("X_RESULTCODE"));
        resultInfo.setX_RECORDNUM(jsonObject.getString("X_RECORDNUM"));
        //{"X_RESULTINFO":"OK","returnCode":"9999","returnMsg":"用户信息不存在","X_RESULTCODE":"0","X_RECORDNUM":1}
        if(returnCode.equals("0000")){
            resultInfo.setUser_name(jsonObject.getString("user_name"));
            resultInfo.setUser_full_name(jsonObject.getString("user_full_name"));
            JSONObject user=jsonObject.getJSONObject("user");
            resultInfo.setPhone(user.getString("phone"));
            resultInfo.setIs4G(user.getString("isVip"));
            resultInfo.setPreSave(user.getString("preSave"));
            resultInfo.setCurrentOfferId(user.getString("currentOfferId"));
            resultInfo.setState(user.getString("state"));
            resultInfo.setStarLevelCreditLimit(user.getString("starLevelCreditLimit"));
            resultInfo.setBrandName(user.getString("brandName"));
            resultInfo.setBrandId(user.getString("brandId"));
            resultInfo.setUsername(user.getString("username"));
            resultInfo.setBalOrgName(user.getString("balOrgName"));
            resultInfo.setBrandCode(user.getString("brandCode"));
            resultInfo.setHistoryCost(user.getString("historyCost"));
            resultInfo.setValidateLevel(user.getString("validateLevel"));
            resultInfo.setCreateDate(user.getString("createDate"));
            resultInfo.setOfferName(user.getString("offerName"));
            resultInfo.setRegionId(user.getString("regionId"));
            resultInfo.setNoticeFlag(user.getString("noticeFlag"));
            resultInfo.setBanlance(user.getString("banlance"));
            resultInfo.setBalOrgId(user.getString("balOrgId"));
            resultInfo.setRegionName(user.getString("regionName"));
            resultInfo.setCost(user.getString("cost"));
            resultInfo.setIs4G(user.getString("is4G"));
            resultInfo.setStarLevel(user.getString("starLevel"));
            resultInfo.setPoint(user.getString("point"));
            resultInfo.setCurrentOweFee(user.getString("currentOweFee"));
            resultInfo.setStarLevelValue(user.getString("starLevelValue"));
            JSONObject extInfo=user.getJSONObject("extInfo");
            resultInfo.setAge(extInfo.getString("age"));
            resultInfo.setBroadbandSpeed(extInfo.getString("broadbandSpeed"));
            resultInfo.setAmountCredit(user.getString("amountCredit"));
        }
        resultInfoDao.insert(resultInfo);
        /*
         *
         * */
        // List<ResultInfo> resultInfos = new ArrayList<>();
    }

}