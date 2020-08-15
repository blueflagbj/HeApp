package com.wade.mobile.util.assets;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.wade.mobile.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsUtil {
    /* access modifiers changed from: private */
    public static final String TAG = AssetsUtil.class.getSimpleName();

    public static void traversal(Activity context, String assetDir, IAssetsFileOperation fileOper) throws Exception {
        new AssetsRecursion(context, fileOper).recursion(assetDir);
    }

    public static void copyAssetsDir(Context context, String assetDir, final String targetDir) throws Exception {
        File targetFile = new File(targetDir);
        if (!targetFile.exists() && !targetFile.mkdirs()) {
            Log.d(TAG, "copyAssetsDir:cannot create directory : " + targetDir);
        }
        new AssetsRecursion(context, new IAssetsFileOperation() {
            public boolean fileFliter(String name) throws Exception {
                return true;
            }

            public void fileDo(InputStream is, String filePath) throws Exception {
                File outFile = new File(targetDir, filePath);
                String filePath2 = outFile.getCanonicalPath();
                boolean success = true;
                if (!outFile.exists()) {
                    success = outFile.mkdirs();
                }
                if (success) {
                    // Do something on success
                   // FileUtil.createDir(filePath2.substring(0, filePath2.lastIndexOf(File.separator)));
                    FileOutputStream fos = new FileOutputStream(outFile);
                    try {
                        byte[] buf = new byte[1024];
                        while (true) {
                            int len = is.read(buf);
                            if (len > 0) {
                                fos.write(buf, 0, len);
                            } else {
                                Log.d(TAG, "fileDo:copy resource : " + outFile);
                                return;
                            }
                        }
                    } finally {
                        fos.close();
                    }
                } else {
                    // Do something else on failure
                }
            }
        }).recursion(assetDir);
    }

    public static void copyAssetsFile(Context context, String assetsFile, String file) throws Exception {
        try {
            InputStream in = context.getAssets().open(assetsFile);
            FileOutputStream out = new FileOutputStream(file);
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//
//        FileUtil.writeFile(context.getAssets().open(assetsFile, ACCESS_BUFFER), file);
    }
}