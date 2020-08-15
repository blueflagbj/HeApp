package com.ai.cmcchina.crm.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class IOUtil {
    public static byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            int len = inStream.read(buffer);
            if (len != -1) {
                outSteam.write(buffer, 0, len);
            } else {
                outSteam.close();
                inStream.close();
                return outSteam.toByteArray();
            }
        }
    }

    public static void string2File(String string, String path) throws NumberFormatException, IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(string.getBytes());
        outStream.flush();
        outStream.close();
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open("images/" + strName));
        } catch (IOException e) {
            Log.e(TAG, "getBitmapFromAsset: ",e );
            return null;
        }
    }

}
