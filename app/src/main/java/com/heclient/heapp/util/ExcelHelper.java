package com.heclient.heapp.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heclient.heapp.MainActivity;
import com.heclient.heapp.R;
import com.heclient.heapp.db.PhoneNum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelHelper {

    private List<Phone> phoneNumList=new ArrayList<>();
    private String XLSbasePath;

    public ExcelHelper(){

        XLSbasePath = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
        File file = new File(XLSbasePath);//创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }
    /**
     * 创建一个file，并按给出的参数进行设置
     * {"FileName":"xls.xls",
     * "XlsPath":"mnt/pice",
     * "SheetName":"处理结果",
     * "Label":[{"x":0,"y":0,"序号"},{"x":1,"y":0,"号码"},{"x":2,"y":0,"处理时间"}]
     * }
    **/
    private void createFile(String xls) {
        JSONObject jsonObject =JSON.parseObject(xls);
        String fileName = jsonObject.getString("FileName");
        String xlsPath = jsonObject.getString("XlsPath");
        Integer sheetNum = jsonObject.getInteger("SheetNum");// 第几个sheet
        String sheetName = jsonObject.getString("SheetName");//
        String JSON_ARRAY_STR =jsonObject.getString("Label");//
        JSONArray labelArray = JSON.parseArray(JSON_ARRAY_STR);
        File file = new File(xlsPath);//创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(xlsPath+ "/"+fileName);//指明存放数据的excel表示
        if (!file.exists()) {//如果文件不存在，创建文件
            createFile(String.valueOf(file));
        }
        OutputStream os = null;
        WritableWorkbook wwb = null;
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            //创建一个可写的Workbook
            wwb = Workbook.createWorkbook(os);
            //创建一个可写的sheet,第一个参数是名字,第二个参数是第几个sheet,写入第一行
            WritableSheet sheet = wwb.createSheet(sheetName, sheetNum);
            //创建一个Label,第一个参数是x轴,第二个参数是y轴,第三个参数是内容,第四个参数可选,指定类型
            for (Object obj : labelArray) {
                JSONObject LabelObject = (JSONObject) obj;
                int x=LabelObject.getInteger("x");
                int y=LabelObject.getInteger("y");
                String labelName =LabelObject.getString("LabelName");
                Label label = new Label(x,y,labelName);
                sheet.addCell(label);
            }
            wwb.write();
            //只有执行close时才会写入到文件中,可能在close方法中执行了io操作
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//关闭流
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void storPone(String phone){
        JSONObject jsonObject =JSON.parseObject(phone);
        String xlsPath = jsonObject.getString("XlsPath");
        String fileName = jsonObject.getString("FileName");
        Integer sheetNum = jsonObject.getInteger("SheetNum");// 第几个sheet
        String sheetName = jsonObject.getString("SheetName");//
        String JSON_ARRAY_STR =jsonObject.getString("Rows");//
        JSONArray RowsArray = JSON.parseArray(JSON_ARRAY_STR);
        File file = new File(xlsPath+ "/"+fileName);//文件
        if (file.exists()) {
            Workbook originWwb = null;
            WritableWorkbook newWwb = null;
            try {
                //插入数据需要拿到原来的表格originWwb，然后通过其创建一个新的表格newWwb，在newWwb上完成插入操作
                originWwb = Workbook.getWorkbook(file);
                newWwb = Workbook.createWorkbook(file, originWwb);
                //获取指定索引的表格
                WritableSheet sheet = newWwb.getSheet(sheetNum);//第几个sheet
                // 获取该表格现有的行数，将数据插入到底部
                for (Object obj : RowsArray) {
                    int row = sheet.getRows();
                    JSONObject LabelObject = (JSONObject) obj;
                    int x=LabelObject.getInteger("x");
                    int y=LabelObject.getInteger("y");
                    String labelName =LabelObject.getString("LabelVlue");
                    Label label = new Label(x,y,labelName);
                    sheet.addCell(label);
                }
                //从内存中的数据写入到sd卡excel文件中。
                newWwb.write();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {//释放资源
                if (originWwb != null) {
                    originWwb.close();
                }
                if (newWwb != null) {
                    try {
                        newWwb.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public List<Phone> LoadPhoneNum(String fileName){
        System.out.println("查询..."+fileName);
        File file = new File(fileName);//文件
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(file.getPath());//获取流
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            int cell0=0;
            //第一行默认为表头，从第二行开始读取
            for (int i = 1; i < sheet.getRows(); i++) {
/*                Log.i(TAG, sheet.getCell(0, i).getContents() + "-" + sheet.getCell(1, i).getContents() +
                "-" + sheet.getCell(2, i).getContents());*/
                cell0 = Integer.parseInt(sheet.getCell(0, i).getContents());

                Phone a=new Phone(cell0,
                        sheet.getCell(1, i).getContents(),
                        "未处理");
                phoneNumList.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return phoneNumList;
    }



/*    private List<PhoneNum> queryUser(File file) {
        InputStream is = null;
        Workbook workbook = null;

        try {
            is = new FileInputStream(file.getPath());//获取流
            workbook = Workbook.getWorkbook(is);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
*//*                Log.i(TAG, sheet.getCell(0, i).getContents() + "-" + sheet.getCell(1, i).getContents() +
                        "-" + sheet.getCell(2, i).getContents());*//*

                PhoneNum a=new PhoneNum(sheet.getCell(0, i).getContents(),
                        sheet.getCell(1, i).getContents(),
                        sheet.getCell(2, i).getContents(),
                        sheet.getCell(3, i).getContents());
                fruitList.add(a);
            }
            // 先拿到数据并放在适配器上
            // initFruits(); //初始化水果数据
            FruitAdapter adapter=new FruitAdapter(MainActivity.this, R.layout.fruit_item,fruitList);

            // 将适配器上的数据传递给listView
            ListView listView=findViewById(R.id.lv);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteUser(File file) {
        Workbook originWwb = null;
        WritableWorkbook newWwb = null;
        try {
            //插入数据需要拿到原来的表格originWwb，然后通过其创建一个新的表格newWwb，在newWwb上完成插入操作
            originWwb = Workbook.getWorkbook(file);
            newWwb = Workbook.createWorkbook(file, originWwb);
            //获取指定索引的表格
            WritableSheet ws = newWwb.getSheet(0);
            // 获取该表格现有的行数，将数据插入到底部
            int row = ws.getRows();
            ws.removeRow(row - 1);
            // 从内存中的数据写入到sd卡excel文件中。
            newWwb.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//释放资源
            if (originWwb != null) {
                originWwb.close();
            }
            if (newWwb != null) {
                try {
                    newWwb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
