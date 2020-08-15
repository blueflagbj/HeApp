package com.ai.cmcchina.crm.util;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wade.mobile.common.MobileLog;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.ai.cmcchina.crm.util.QRCodeUtil */
public class QRCodeUtil {
    public static boolean createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm, String filePath) {
        if (content == null) {
            return false;
        }
        try {
            if ("".equals(content)) {
                return false;
            }
            Bitmap bitmap = getQRCodeBitmap(content, widthPix, heightPix);
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }
            if (bitmap == null || !bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            MobileLog.d("createQRImage Err:",e.getMessage());
            return false;
        }
    }

    public static Bitmap getQRCodeBitmap(String content, int widthPix, int heightPix) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
        int[] pixels = new int[(widthPix * heightPix)];
        for (int y = 0; y < heightPix; y++) {
            for (int x = 0; x < widthPix; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[(y * widthPix) + x] = -16777216;
                } else {
                    pixels[(y * widthPix) + x] = -1;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
        return bitmap;
    }

    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        float scaleFactor = ((((float) srcWidth) * 1.0f) / 5.0f) / ((float) logoWidth);
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0.0f, 0.0f, (Paint) null);
            canvas.scale(scaleFactor, scaleFactor, (float) (srcWidth / 2), (float) (srcHeight / 2));
            canvas.drawBitmap(logo, (float) ((srcWidth - logoWidth) / 2), (float) ((srcHeight - logoHeight) / 2), (Paint) null);

            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}