package com.ai.cmcchina.multiple.util;



import com.heclient.heapp.util.DesPlus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;


/* renamed from: com.ai.cmcchina.multiple.util.FileUploadUtil */
public class FileUploadUtil {
    private static final String BOUNDARYSTR = "--------7dbe81d3124a";
    private static final String END = "\r\n";
    public static final String ERROR_CRYPT = "文件上传失败：服务端返回数据解析异常！";
    public static final String ERROR_FAILED = "服务器链接失败！";
    private static final String LAST = "--";

    public static String post(String urlStr, String[] filePaths, Map<String, String> params) throws Exception {
       // G3Logger.debug("文件上传服务路径：" + urlStr);
        if (filePaths.length == 0) {
            throw new Exception("文件上传路径为空，请选择文件后开始上传!");
        }
        String result = "";
        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "multipart/form-data;boundary=--------7dbe81d3124a");
        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        StringBuffer inputString = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            inputString.append("----------7dbe81d3124a\r\n");
            inputString.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + "\r\n" + "\r\n");
            if (value == null) {
                value = "";
            }
            if (Pattern.compile("([\\u4E00-\\u9FA5])").matcher(value).find()) {
                value = URLEncoder.encode(value);
            }
            inputString.append(value + "\r\n");
        }
        dos.write(inputString.toString().getBytes("utf-8"));
       // G3Logger.debug("开始写入文件流...");
        for (int i = 0; i < filePaths.length; i++) {
            StringBuffer inputFileString = new StringBuffer();
            String[] fileItem = filePaths[i].split("/");
            String fileName = fileItem[fileItem.length - 1];
            inputFileString.append("----------7dbe81d3124a\r\n");
            inputFileString.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"file\";");
            inputFileString.append("filename=\"" + fileName + "\"" + "\r\n" + "\r\n");
            dos.write(inputFileString.toString().getBytes("utf-8"));
           // G3Logger.debug("文件上传 filePaths：" + filePaths[i]);
            File file = new File(filePaths[i]);
            if (!file.exists()) {
                throw new Exception("文件不存在，请重新选择：" + filePaths[i]);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            if (fileInputStream != null) {
                byte[] b = new byte[1024];
                while (true) {
                    int len = fileInputStream.read(b);
                    if (len == -1) {
                        break;
                    }
                    dos.write(b, 0, len);
                }
                dos.write("\r\n".getBytes());
            }
            fileInputStream.close();
        }
        dos.write("----------7dbe81d3124a--\r\n".getBytes());
        dos.flush();
       // G3Logger.debug("文件流写入完成！！！");
        StringBuffer resultSB = new StringBuffer();
        int resCode = connection.getResponseCode();
       // G3Logger.debug("获取相应码connection.getResponseCode():" + resCode);
        if (resCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                resultSB.append(line);
            }
           // G3Logger.debug("获取解密前数据:" + resultSB.toString());
            if (urlStr.contains("com.ai.esp.g3")) {
                try {
                    result = new DesPlus("hnesop").decrypt(resultSB.toString(), "gbk");
                   // G3Logger.debug("获取解密后数据:" + result);
                } catch (Exception e) {
                    result = resultSB.toString();
                   // G3Logger.debug("返回数据解密失败:" + result);
                }
            }
           // G3Logger.debug("文件上传完成！！！----：" + result);
            return result;
        }
        throw new Exception("服务器链接失败！");
    }
}