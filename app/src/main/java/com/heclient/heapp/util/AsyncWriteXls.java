package com.heclient.heapp.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AsyncWriteXls {
    private static volatile AsyncWriteXls instance = null;
    private SimpleDateFormat mFormat = null;
    private WriteThread mThread = null;

    @SuppressLint("SimpleDateFormat")
    private AsyncWriteXls() {
        mThread = new WriteThread();
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"yyyyMMddhhmmss"
        mThread.start();
    }

    //使用安全的单例模式
    public static AsyncWriteXls getInstance() {
        if (instance == null) {
            synchronized (AsyncWriteXls.class) {
                if (instance == null) {
                    instance = new AsyncWriteXls();
                }
            }
        }
        return instance;
    }
    //外部直接调用保存到xls文件中,按行保存
    public static void saveRows(String str,String type){
        JSONObject jsonObject = JSON.parseObject(str);
        jsonObject.put("tag","writrows");
        str=jsonObject.toJSONString();
        System.out.println("saveRows:"+str);
        AsyncWriteXls.getInstance().writexls(str);
    }
    public synchronized void writexls(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        String time = mFormat.format(new Date());
        jsonObject.put("time",time);
        str=jsonObject.toJSONString();
        System.out.println(str);
        //加入队列
        mThread.enqueue(str);
    }


     //线程保持常在,不工作时休眠,需要工作时再唤醒就可.
    public class WriteThread extends Thread {
        private boolean isRunning = false;
        private String filePath = null;
        private Object lock = new Object();
        private ConcurrentLinkedQueue<String> mQueue = new ConcurrentLinkedQueue<String>(); //创建队列

        public WriteThread() {
            String sdcard = getPath();  //获取读写文件夹
            filePath = sdcard + "/Pictures";
            File file = new File(filePath);
            if (!file.exists()) { //创建新文件
                file.mkdir();
            }
            isRunning = true;
        }

        public String getPath() {
            return exist() ? Environment.getExternalStorageDirectory().toString() : null;
        }

        public boolean exist() {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }

        //将需要写入文本的字符串添加到队列中.线程休眠时,再唤醒线程写入文件
        public void enqueue(String str) {
            mQueue.add(str);
            if (isRunning() == false) {
                awake();
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void awake() {
            synchronized (lock) {
                lock.notify();
            }
        }
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    isRunning = true;
                    while (!mQueue.isEmpty()) {
                        try {
                            //pop出队列的头字符串写入到文件中
                            write(mQueue.poll());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isRunning = false;
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


        public void write(String str) {
            JSONObject jsonObject =JSON.parseObject(str);
            String tag=jsonObject.getString("tag");
            String type=jsonObject.getString("type");
            String xlsPath = jsonObject.getString("xlsPath");
            if(xlsPath.equals("")){
                xlsPath=getPath()+"/Pictures";
            }

            String fileName = jsonObject.getString("fileName");
            Integer sheetNum = jsonObject.getInteger("sheetNum");// 第几个sheet
            String sheetName = jsonObject.getString("sheetName");//

            JSONArray CellsArray = jsonObject.getJSONArray("cells");//行内容
            filePath = xlsPath+ "/"+fileName;
            File file = new File(filePath);//文件
            System.out.println("filepath:"+filePath);

            Workbook originWwb = null;
            WritableWorkbook wwb = null;
            if (!file.exists()) {
                try {
                file.createNewFile();
                    OutputStream os = null;
                    os = new FileOutputStream(file);
                    //创建一个可写的Workbook
                    wwb = Workbook.createWorkbook(os);
                    //创建一个可写的sheet,第一个参数是名字,第二个参数是第几个sheet,写入第一行
                    WritableSheet sheet = wwb.createSheet(sheetName, sheetNum); //
                    //创建一个Label,第一个参数是x轴,第二个参数是y轴,第三个参数是内容,第四个参数可选,指定类型
                    for (Object obj : CellsArray) {
                        JSONObject cellsObject = (JSONObject) obj;
                        int x=cellsObject.getInteger("rowx");
                        int y=cellsObject.getInteger("rowy");
                        String cellValue =cellsObject.getString("cellValue");
                        Label label = new Label(x,y,cellValue);
                        //把label加入sheet对象中
                        sheet.addCell(label);
                    }
                   wwb.write();
                    //只有执行close时才会写入到文件中,可能在close方法中执行了io操作
                   wwb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                /*Workbook originWwb = null;
                WritableWorkbook wwb = null;*/
                //插入数据需要拿到原来的表格originWwb，然后通过其创建一个新的表格newWwb，在newWwb上完成插入操作
                try{
                    originWwb = Workbook.getWorkbook(file);
                    wwb = Workbook.createWorkbook(file, originWwb);

                    int sheets=wwb.getNumberOfSheets();
                    System.out.println("sheets:"+sheets);
                    //
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddhhmmss");
                    String sheetname0 =s_format.format(new Date());
                    sheetname0 = String.format("%s(%s)",sheetName,sheetname0);
                    WritableSheet sheet =wwb.createSheet(sheetname0, sheets);//在末尾新增sheet

                    for (Object obj : CellsArray) {
                        JSONObject cellsObject = (JSONObject) obj;
                        int x=cellsObject.getInteger("rowx");
                        int y=cellsObject.getInteger("rowy");
                        String cellValue =cellsObject.getString("cellValue");
                        Label label = new Label(x,y,cellValue);
                        //把label加入sheet对象中
                        sheet.addCell(label);
                    }
                    //从内存中的数据写入到sd卡excel文件中。
                    wwb.write();
                    wwb.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        /**
         * 判断日志文件是否存在
         *
         * @return
         */
        public boolean isExitLogFile() {
            File file = new File(filePath);
            if (file.exists() && file.length() > 3) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 删除日志文件
         */
        public void deleteLogFile() {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

}
