package com.wade.mobile.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import com.wade.mobile.common.MobileLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MobileGraphics {
    private static final String TAG = MobileGraphics.class.getSimpleName();

    public static Bitmap screenSnapshot(View view, Matrix matrix) {
        Bitmap bmp = null;
        if (null != view)
            try {
                bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                view.draw(new Canvas(bmp));
                if (matrix != null)
                    bmp = Bitmap.createBitmap(bmp, 0, 0, view.getWidth(), view.getHeight(), matrix, true);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "CreateBitmap Failed:" + e.getMessage());
            }
        return bmp;
    }
    public static Bitmap screenSnapshot(View view) {
        return screenSnapshot(view, (Matrix) null);
    }

    public static Bitmap screenSnapshot(View view, int zoomScale) {
        Matrix matrix = new Matrix();
        matrix.postScale((float) zoomScale, (float) zoomScale);
        return screenSnapshot(view, matrix);
    }

    public static ImageView screenSnapshotView(Context context, View view, Matrix matrix) {
        ImageView imageView = new ImageView(context);
        Bitmap bmp = screenSnapshot(view, matrix);
        imageView.setTag(view.getTag());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(bmp);
        return imageView;
    }
    public static void savePicToLocal(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "savePicToLocal:FilaName error!!!" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "savePicToLocal:Write to File error!!!" + e.getMessage());
        } finally {
            if (fos != null)
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
        }
    }
    public static Bitmap webViewSnapshot(WebView webView) {
        Bitmap bmp = null;
        if (webView == null)
            return null;
        Picture picture = webView.capturePicture();
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width > 0 && height > 0) {
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            picture.draw(new Canvas(bmp));
            return bmp;
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int roundedSize, initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize)
                roundedSize <<= 1;
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int)Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int)Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound)
            return lowerBound;
        if (maxNumOfPixels == -1 && minSideLength == -1)
            return 1;
        if (minSideLength == -1)
            return lowerBound;
        return upperBound;
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos))
                return Base64.encode(baos.toByteArray());
        } finally {
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public static Bitmap stringtoBitmap(String string) {
        byte[] bitmapArray = Base64.decodeByte(string);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        return bitmap;
    }
    public static Bitmap zoomImage(Bitmap bitmap, double maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        double fileSize = ((baos.toByteArray()).length / 1024);
        Bitmap newBitmap = null;
        if (fileSize > maxSize) {
            double i = fileSize / maxSize;
            try {
                newBitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i));
            } finally {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        }
        return (newBitmap == null) ? bitmap : newBitmap;
    }
    private static Bitmap zoomImage(Bitmap bitmap, double newWidth, double newHeight) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (float)newWidth / width;
        float scaleHeight = (float)newHeight / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = null;
        try {
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height, matrix, true);
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        return (newBitmap == null) ? bitmap : newBitmap;
    }

    public static Bitmap compressImage(Bitmap bitmap, double maxSize, int quality) {
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        Bitmap resizeBitmap = null;
        try {
            resizeBitmap = zoomImage(bitmap, maxSize);
            baos = new ByteArrayOutputStream();
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            bais = new ByteArrayInputStream(baos.toByteArray());
            return BitmapFactory.decodeStream(bais, null, null);
        } finally {
            try {
                if (baos != null)
                    baos.close();
                if (bais != null)
                    bais.close();
            } catch (Exception exception) {}
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            if (resizeBitmap != null && !resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
                resizeBitmap = null;
            }
        }
    }

    public static void compressImageFile(String fromFile, String toFile, double maxSize, int quality) throws IOException {
        Bitmap bitmap = null, resizeBitmap = null;
        FileOutputStream out = null;
        try {
            bitmap = BitmapFactory.decodeFile(fromFile);
            resizeBitmap = zoomImage(bitmap, maxSize);
            File captureFile = new File(toFile);
            out = new FileOutputStream(captureFile);
            if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
                out.flush();
        } finally {
            if (out != null)
                out.close();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            if (resizeBitmap != null && !resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
                resizeBitmap = null;
            }
        }
    }

}