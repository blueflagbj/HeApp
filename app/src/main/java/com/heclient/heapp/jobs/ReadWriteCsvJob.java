package com.heclient.heapp.jobs;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.heclient.heapp.App;
import com.heclient.heapp.db.DaoSession;
import com.heclient.heapp.db.ResultInfo;
import com.heclient.heapp.db.ResultInfoDao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReadWriteCsvJob extends Job {
    private long localId;
    private String text;
    private String tag;
    private String type;
    private JSONObject csvReq;
    private String csvPath;
    private String dbPath;//"/data/data/com.lingdududu.db/databases/stu.db"
    private String sql; //"insert into bus_line_station(direct,line_name,sno,station_name) values(?,?,?,?)";
    private String result; //
    /*private SQLiteDatabase db;
    private SQLiteStatement stat;*/

    public ReadWriteCsvJob(String text) {
        /**
         * 默认构造器传入的是int(priority)参数是该任务的优先级，优先级越高，越优先执行。
         *<p>
         * requireNetwork(): 设置该任务要求访问网络；
         *<p>
         * groupBy(String groupId)：设置组ID，被设置相同组ID的任务，将会按照顺序执行；
         *<p>
         * persist()：设置任务为可持久化的，持久化要求Job类为序列化的，这一点并不意外，
         * 因为一个类的内容只有序列化之后才能变成字节模式保存在硬盘上；
         * <p>
         *delayInMs(long delayMs)：设置延迟时间，ms为单位，在该时间之后再放入任务队列中。
         * <p>
         * addTags :添加标记取消任务时使用
         */
        super(new Params(Priority.MID).requireNetwork().groupBy("ReadWriteCsvJob"));
        //order of tweets matter, we don't want to send two in parallel
        //use a negative id so that it cannot collide w/ twitter ids
        //we have to set local id here so it gets serialized into job (to find tweet later on)
        localId = -System.currentTimeMillis();
        this.text = text;
        try {
            csvReq = new JSONObject(text);
            tag =csvReq.getString("tag");
           // inId=csvReq.getInt("inId");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getResult(){
        return result;
    }
    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        // .csv 文件路径
         System.out.println("readwritecsvJob:"+csvReq.toString());
        type =csvReq.getString("type");
        switch (type) {
            case "creatTable":
                //生成数据表;
                return;
            case "readCsv":
                //updateResource();
                String csvData=csvReq.getString("csvData");
                String backInfo ;
                backInfo=readCsv(csvData);
                String taskTag= csvReq.getString("taskTag");
                JSONObject reqData=new JSONObject();
                reqData.put("taskTag",taskTag);
                reqData.put("backInfo",backInfo);
                result= reqData.toString();
                return;
            case "writeCsv":
                  /*  if (TemplateSubActivity.LoadingDialogStyle.HORIZONTAL.equals(TemplateSubActivity.this.getLoadingDialogStyle())) {
                        TemplateSubActivity.this.updateResProgressDialog.setProgress(TemplateManager.getDownloadCount());
                        return;
                    }*/

                String csvPath0 = csvReq.getString("csvPath");
                writeCsv(csvPath0);

                return;
            default:
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
    //csv

    // 读取 .csv 文件
    private String readCsv(String str) {
        String returnStr="";
        try {
                JSONObject csvData= new JSONObject(str) ;
                csvPath=csvData.getString("csvPath");
               // dbPath=csvData.getString("dbPath");//"/data/data/com.lingdududu.db/databases/stu.db"
                sql=csvData.getString("sql"); //"insert into bus_line_station(direct,line_name,sno,station_name) values(?,?,?,?)";
               // db =SQLiteDatabase.openOrCreateDatabase(dbPath,null);
                /*((App) getApplication()).getDaoSession();*/
/*                  reqData.put("taskTag","importCsv");
                    reqData.put("tag","readwriteCsv");//
                    reqData.put("type","readCsv"); //类型*/
                DaoSession daoSession = App.getInstance().getDaoSession();
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

                    returnStr= "导入数据处理完毕，耗时："+stime+" 毫秒！";
                    System.out.println(result);
                }else{
                    returnStr= "导入数据模板不存在，请检查后再行操作！";
                }
             } catch (JSONException | IOException e) {
                returnStr= "导入异常...";
                e.printStackTrace();
            }
        return returnStr;
    }
    // 写入 .csv 文件
    private void writeCsv(String csvPath) {
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
            result="导出数据处理完毕，耗时："+stime+" 毫秒！";

        } catch (IOException e) {
            result="导出异常...";
            e.printStackTrace();
        }
    }
}
