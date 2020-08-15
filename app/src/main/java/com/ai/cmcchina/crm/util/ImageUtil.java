package com.ai.cmcchina.crm.util;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/* renamed from: com.ai.cmcchina.crm.util.ImageUtil */
public class ImageUtil {
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        if (width > height) {
            return Math.round(((float) height) / ((float) reqHeight));
        }
        return Math.round(((float) width) / ((float) reqWidth));
    }

    public static Bitmap view2Bitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeBitmapFromUrl(String url, int reqWidth, int reqHeight) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            InputStream is2 = new URL(url).openStream();
            BitmapFactory.decodeStream(is2, (Rect) null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            System.out.println(options.inSampleSize);
            options.inJustDecodeBounds = false;
            is2.close();
            is = new URL(url).openStream();
            Bitmap decodeStream = BitmapFactory.decodeStream(is, (Rect) null, options);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                   // G3Logger.debug(e);
                }
            }
            return decodeStream;
        } catch (Exception e2) {
            throw new Exception("failed");
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                   // G3Logger.debug(e3);
                }
            }
            throw th;
        }
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static String drawable2File(Drawable drawable, File file) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
            outStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
           // G3Logger.debug(e);
            return null;
        }
    }

    public static String bitmap2File(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return null;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outStream);
            outStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
          //  G3Logger.debug(e);
            return null;
        }
    }

    public static String bitmap2FileNew(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return null;
        }
        try {
            if (file.exists()) {
                file.delete();
            }
            if (!file.createNewFile()) {
                return null;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
           // G3Logger.debug(e);
            return null;
        }
    }

    public static String bitmap2PngFile(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return null;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, outStream);
            outStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
//            G3Logger.debug(e);
            return null;
        }
    }

    public static Drawable file2Drawable(File file) {
        if (file.exists()) {
            return new BitmapDrawable(file.getPath());
        }
        return null;
    }

    public static Bitmap file2Bitmap(File file) {
        if (file.exists()) {
            return new BitmapDrawable(file.getPath()).getBitmap();
        }
        return null;
    }

    public static Bitmap fileString2Bitmap(String url) {
        File file = new File(url);
        if (file.exists()) {
            return new BitmapDrawable(file.getPath()).getBitmap();
        }
        return null;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height / 2) + height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 4), new Paint());
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (height + 4), (Paint) null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) bitmap.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 4), 1895825407, 16777215, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (bitmapWithReflection.getHeight() + 4), paint);
        return bitmapWithReflection;
    }

    public static Bitmap zoomImage(Bitmap bitmap, double maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        double fileSize = (double) (baos.toByteArray().length / 1024);
        Bitmap newBitmap = null;
        if (fileSize > maxSize) {
            double i = fileSize / maxSize;
            try {
                newBitmap = zoomImage(bitmap, ((double) bitmap.getWidth()) / Math.sqrt(i), ((double) bitmap.getHeight()) / Math.sqrt(i));
            } finally {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        if (newBitmap == null) {
            return bitmap;
        }
        return newBitmap;
    }

    private static Bitmap zoomImage(Bitmap bitmap, double newWidth, double newHeight) {
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / width, ((float) newHeight) / height);
        try {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
            if (newBitmap == null) {
                return bitmap;
            }
            return newBitmap;
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static Bitmap view2BitmapNew(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate((float) (-v.getScrollX()), (float) (-v.getScrollY()));
        v.draw(canvas);
        return screenshot;
    }

    public static Bitmap compressBySize(Bitmap image, float minWidth) {
        float minHeight = minWidth * 0.63084114f;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = minWidth;
        float ww = minHeight;
        int be = 1;
        if (w > h && ((float) w) > ww) {
            be = (int) (((float) newOpts.outWidth) / ww);
        } else if (w < h && ((float) h) > hh) {
            be = (int) (((float) newOpts.outHeight) / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, (Rect) null, newOpts);
        try {
            isBm.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap resize(Bitmap bm) {
        try {
            int width = bm.getWidth();
            int height = bm.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f, 0.5f);
            return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            return null;
        }
    }

    public static String file2EncodeToBase64String(String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            return null;
        }
        try {
            Bitmap bitmap = file2Bitmap(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String encodeToString = Base64.encodeToString(baos.toByteArray(), 0);
            baos.close();
            return encodeToString;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap string2Bitmap(String string) {
        try {
            byte[] bitmapArray = Base64.decode(string, 0);
            return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
//            G3Logger.debug(e);
            return null;
        }
    }

    public static String bitmapEncodeToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String base64String = "";
        try {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)) {
                base64String = com.wade.mobile.util.Base64.encode(byteArrayOutputStream.toByteArray());
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }
        } catch (Exception e) {
//            G3Logger.debug("bitmapEncodeToBase64String:Bitmap转Base64 String 失败！！！");
//            G3Logger.debug(e);
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException var102) {
                    var102.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException var103) {
                    var103.printStackTrace();
                }
            }
            throw th;
        }
        return base64String;
    }
}