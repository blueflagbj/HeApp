package com.ai.cmcchina.multiple.func;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.ai.cmcchina.crm.util.ImageUtil;
import com.ai.cmcchina.crm.util.QRCodeUtil;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.multiple.handler.FileUploadHandler;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.frame.config.ServerDataConfig;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.safe.MobileSecurity;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.MobileGraphics;
import com.wade.mobile.util.Utility;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

@SuppressLint({"HandlerLeak"})
/* renamed from: com.ai.cmcchina.multiple.func.ImgHandlerPlugin */
public class ImgHandlerPlugin extends Plugin {
    private static final int ERROR = 0;
    private static final int SUCCESS = 1;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ImgHandlerPlugin.this.error("操作失败");
                    return;
                case 1:
                    ImgHandlerPlugin.this.callback((String) msg.obj, true);
                    return;
                default:
                    return;
            }
        }
    };

    public ImgHandlerPlugin(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void uploadImgToAddr(JSONArray param) throws Exception {
        String[] minImgPaths;
        String dataAction = param.getString(1);
        Map dataParam = isNull(param.getString(2)) ? new DataMap() : new DataMap(param.getString(2));
        String imgPathsString = (String) dataParam.get("pictureList");
        if (TextUtils.isEmpty(imgPathsString)) {
            minImgPaths = new String[0];
        } else {
            minImgPaths = imgPathsString.split(Constant.PARAMS_SQE);
        }
        new FileUploadHandler(this.context, dataAction, dataParam, minImgPaths) {
            public void onSuccess(String result) {
                Log.d(TAG, "ImgHandlerPlugin.uploadImgToAddr>>>result:" + result);
                if (result != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    ImgHandlerPlugin.this.handler.sendMessage(msg);
                }
            }

            public void onError(String result) {
                Log.d(TAG, "ImgHandlerPlugin.uploadImgToAddr onError>>>result:" + result);
                ImgHandlerPlugin.this.handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private String compressImgByImgPath(String imgPath) {

       // String minImgPath = Environment.getExternalStorageDirectory() + "/" + MobileConfig.getInstance().getAppPath() + "/image/";
        String minImgPath = this.context.getExternalFilesDir(null) + "/" + MobileConfig.getInstance().getAppPath() + "/image/";
        Bitmap bitmap = MobileGraphics.compressImage(getBitmapByImage(imgPath), 50.0d, 80);
        String minImgName = System.currentTimeMillis() + ".jpg";
        File parent = new File(minImgPath, minImgName).getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        String minImgPath2 = minImgPath + minImgName;
        MobileGraphics.savePicToLocal(bitmap, minImgPath2);
        return minImgPath2;
    }

    public Bitmap getBitmapByImage(String imagePath) {
        BitmapFactory.Options opts = getBitmapOptions();
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inSampleSize = MobileGraphics.computeSampleSize(opts, -1, 384000);
        opts.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(imagePath, opts);
        } catch (OutOfMemoryError e) {
            Utility.error("文件过大,无法加载", e);
            return null;
        }
    }

    public BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inTempStorage = new byte[32768];
        return opts;
    }

    public Map<String, String> transPostData(String dataAction, IData dataParam) throws Exception {
        Map<String, String> postData = new HashMap<>();
        postData.put(Constant.Server.ACTION, dataAction);
        if (ServerDataConfig.isEncrypt(dataAction).booleanValue()) {
            MobileSecurity.init(this.context);
            postData.put(Constant.Server.KEY, MobileSecurity.getDesKey());
            if (dataParam != null) {
                postData.put("data", MobileSecurity.requestEncrypt(dataParam.toString()));
            }
        } else if (dataParam != null) {
            postData.put("data", dataParam.toString());
        }
        return postData;
    }

    public void createQRCode(JSONArray param) throws Exception {
        Bitmap smallPictureBitmap;
        Log.d(TAG, "生成二维码图片Base64String:createQRCode");
        IData data = new DataMap(param.getString(0));
        String url = data.getString(Constant.MobileWebCacheDB.URL_COLUMN);
        String widthString = data.getString("width");
        String smallPicture = data.getString("smallPicture");
        int width = 200;
        if (!StringUtil.isBlank(widthString)) {
            width = Integer.valueOf(widthString).intValue();
        }
        Bitmap qrCodeBitmap = QRCodeUtil.getQRCodeBitmap(url, width, width);
        if (StringUtil.isNotBlank(smallPicture) && (smallPictureBitmap = ImageUtil.string2Bitmap(smallPicture)) != null) {
            qrCodeBitmap = QRCodeUtil.addLogo(qrCodeBitmap, smallPictureBitmap);
        }
        callback(ImageUtil.bitmapEncodeToBase64String(qrCodeBitmap));
    }

    public void saveQRCode(JSONArray param) throws Exception {
        Log.d(TAG, "保存二维码图片:saveQRCode");
        String filePath = ImageUtil.bitmap2File(ImageUtil.string2Bitmap(param.getString(0)), new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "hzs") + File.separator + ("QR_Code" + System.currentTimeMillis() + ".jpg")));
        if (StringUtil.isBlank(filePath)) {
            callback("");
            Log.d(TAG, "保存二维码图片:saveQRCode 失败 ！！！！");
            return;
        }
        callback(filePath);
        Log.d(TAG, "保存二维码图片:saveQRCode 成功 ！！！！ filePath: " + filePath);
    }
}