package com.wade.mobile.func;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.google.zxing.WriterException;
import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.map.util.MapConstant;
import com.wade.mobile.common.scan.activity.CaptureActivity;
import com.wade.mobile.common.scan.encoding.EncodingHandler;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.ui.helper.HintHelper;
import com.wade.mobile.util.DirectionUtil;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.MobileGraphics;
import com.wade.mobile.util.Utility;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;

public class MobileCamera extends Plugin {
    private final int BASE64_TYPE = 0;
    private final int PATH_TYPE = 1;
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");
    private Function func;
    private String photoFullPath;
    private int quality = 80;

    private enum Function {
        getPhoto,
        getPicture,
        scanQrCode,
        createQrCode
    }

    public MobileCamera(IWadeMobile context) {
        super(context);
    }

    public void getPicture(JSONArray param) throws JSONException {
        int requestType;
        this.func = Function.getPicture;
        int type = param.getInt(0);
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (type == 0) {
            requestType = 0;
        } else {
            requestType = 1;
        }
        startActivityForResult(intent, requestType);
    }

    public void getPhoto(JSONArray param) throws Exception {
        int requestType;
        this.func = Function.getPhoto;
        int type = param.getInt(0);
        String photoName = MobileAppInfo.getInstance(this.context).getAppPath() + "-" + this.format.format(new Date()) + ".jpg";
        String photoDir = DirectionUtil.getInstance(this.context).getImageDirection(true);
        File photoDirFile = new File(photoDir);
        if (!photoDirFile.exists()) {
            photoDirFile.mkdirs();
        }
        File photoFile = new File(FileUtil.connectFilePath(photoDir, photoName));
        this.photoFullPath = photoFile.getAbsolutePath();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", Uri.fromFile(photoFile));
        if (type == 0) {
            requestType = 0;
        } else {
            requestType = 1;
        }
        startActivityForResult(intent, requestType);
    }

    public void transImageToBase64(JSONArray param) throws JSONException {
        try {
            Bitmap base64Bitmap = MobileGraphics.compressImage(getBitmapByImage(param.getString(0)), 10.0d, 30);
            String out = MobileGraphics.bitmapToString(base64Bitmap);
            base64Bitmap.recycle();
            callback(out);
        } catch (Exception e) {
            HintHelper.alert(this.context, e.getMessage());
        }
    }

    public void compressImage(JSONArray param) throws JSONException {
        String imagePath = param.getString(0);
        int fileSize = param.getInt(1);
        try {
            Bitmap base64Bitmap = MobileGraphics.compressImage(getBitmapByImage(imagePath), (double) fileSize, param.getInt(2));
            String out = MobileGraphics.bitmapToString(base64Bitmap);
            base64Bitmap.recycle();
            callback(out);
        } catch (Exception e) {
            HintHelper.alert(this.context, e.getMessage());
        }
    }

    public void scanQrCode(JSONArray param) throws JSONException {
        this.func = Function.scanQrCode;
        startActivityForResult(new Intent(this.context, CaptureActivity.class), 0);
    }

    public void createQrCode(JSONArray param) throws JSONException, WriterException {
        String contentString = param.getString(0);
        if (!isNull(contentString)) {
            callback(MobileGraphics.bitmapToString(EncodingHandler.createQRCode(contentString,350)));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String picturePath;
        if (this.func == Function.scanQrCode) {
            if (resultCode == -1) {
                callback(intent.getExtras().getString(MapConstant.CALLBACK_RESULT));
            }
        } else if (this.func.equals(Function.getPhoto)) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap = MobileGraphics.compressImage(getBitmapByImage(this.photoFullPath), 50.0d, this.quality);
                    MobileGraphics.savePicToLocal(bitmap, this.photoFullPath);
                    if (requestCode == 0) {
                        Bitmap base64Bitmap = MobileGraphics.compressImage(bitmap, 10.0d, 30);
                        String out = MobileGraphics.bitmapToString(base64Bitmap);
                        base64Bitmap.recycle();
                        IData result = new DataMap();
                        result.put("thumbnail", out);
                        result.put("path", this.photoFullPath);
                        callback(result.toString());
                    } else {
                        callback(this.photoFullPath);
                    }
                    bitmap.recycle();
                    this.photoFullPath = null;
                } catch (Exception e) {
                    HintHelper.alert(this.context, e.getMessage());
                }
            }
        } else if (this.func.equals(Function.getPicture) && resultCode == -1) {
            Uri uri = intent.getData();
            if (uri.toString().startsWith("file:")) {
                picturePath = uri.getPath();
            } else if (uri.toString().startsWith("content:")) {
                picturePath = getPath(uri);
            } else {
                HintHelper.alert(this.context, "请选择相册或者文件浏览器");
                return;
            }
            if (requestCode == 0) {
                try {
                    Bitmap base64Bitmap2 = MobileGraphics.compressImage(getBitmapByImage(picturePath), 10.0d, 30);
                    String out2 = MobileGraphics.bitmapToString(base64Bitmap2);
                    base64Bitmap2.recycle();
                    callback(out2);
                } catch (Exception e2) {
                    HintHelper.alert(this.context, e2.getMessage());
                }
            } else if (requestCode == 1) {
                callback(picturePath);
            }
        }
    }

    public BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inTempStorage = new byte[32768];
        return opts;
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

    private String getPath(Uri uri) {
        Cursor cursor = this.context.managedQuery(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        this.context.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}