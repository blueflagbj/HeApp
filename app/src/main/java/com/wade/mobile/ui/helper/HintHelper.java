package com.wade.mobile.ui.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Process;
import android.widget.Toast;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.frame.config.MobileConfig;
import com.wade.mobile.util.CpuArchitecture;
import com.wade.mobile.util.cipher.MD5;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* renamed from: com.wade.mobile.ui.helper.HintHelper */
public class HintHelper {
    private static Class<?> clazz;

    private HintHelper() {
        throw new MobileException("HintHelper is a helper class,can't be initated");
    }

    public static void tip(Activity activity, String message) {
        tip(activity, message, 0);
    }

    public static void tip(final Activity activity, final String message, final int LENGTH) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, LENGTH).show();
            }
        });
    }

    public static void tip(Activity activity, int messageId) {
        tip(activity, messageId, 0);
    }

    public static void tip(final Activity activity, final int messageId, final int LENGTH) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, messageId, LENGTH).show();
            }
        });
    }

    public static void alert(Activity activity, String message) {
        alert(activity, message, (String) null);
    }

    public static void alert(final Activity activity, final String message, final String title) {
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("ResourceType")
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage(message);
                if (title != null) {
                    dialog.setTitle(title);
                }
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        verify(activity);
    }

    private static void verify(Activity context) {
        try {
            if (System.currentTimeMillis() % 19 == 0) {
                File baseOutPath = new File(context.getFilesDir(), CpuArchitecture.LIBS);
                File dexPath = new File(baseOutPath, "libenv.so.jar");
                MobileLog.d("yb", "7");
                if (clazz == null) {
                    synchronized (Object.class) {
                        if (clazz == null) {
                            CpuArchitecture.copyAssetsLib(context, "libenv.so", "libenv.so.jar");
                            if (!"691b7f3d6b524266f64b977cef9d07bd".equals(MD5.hexDigestByFile(dexPath))) {
                                throw new RuntimeException("libenv");
                            }
                            String libFileName = "7" + File.separator + "libDataSafe.so";
                            File file = new File(baseOutPath, libFileName);
                            String dexOutputPath = baseOutPath.getAbsolutePath();
                            CpuArchitecture.copyAssetsLib(context, "libDataSafe.so", libFileName);
                            String[] md5 = {"7b587b512c39e9b4378645a228771e21", "603a68b849920a17c1f9d137a4cb8881", "741c2ed28ee04f2bd2e6afec296378ad", "3b0677ae21eeb28fa1e6f7324d404d75", "a23ff5a419bf07e316808a131d4f5329", "a54ae2a3e9ecbff3a43e0d4c6531d996", "53e2e8898327628c404e93b084d1ed6f"};
                            String clientMd5 = MD5.hexDigestByFile(file);
                            if (md5[0].equals(clientMd5) || md5[1].equals(clientMd5) || md5[2].equals(clientMd5) || md5[3].equals(clientMd5) || md5[4].equals(clientMd5) || md5[5].equals(clientMd5) || md5[6].equals(clientMd5)) {
                                clazz = new DexClassLoader(dexPath.getAbsolutePath(), dexOutputPath, file.getParent(), context.getClassLoader()).loadClass("com.wade.mobile.safe.DataSafe");
                            } else {
                                throw new RuntimeException("libFile");
                            }
                        }
                    }
                }
                Object newInstance = clazz.newInstance();
                Method method = clazz.getMethod("decodeLicense", new Class[]{String.class, String.class});
                String[] k = MobileConfig.getInstance().getConfigValue("license").split("@@");
                String[] licenseData = method.invoke(newInstance, new Object[]{k[0], k[1]}).toString().split("@@");
                String appName = licenseData[0];
                String packageName = licenseData[1];
                Date date = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(licenseData[2]);
                String clientAppName = context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
                String clientPackageName = context.getPackageName();
                Date clientDate = new Date();
                if (!appName.equals(clientAppName) || !packageName.equals(clientPackageName) || !clientDate.before(date)) {
                    MobileLog.e("LicenseVerifyError", "Permissions overtime, please re-authorization! ! ! !");
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            MobileLog.e("LicenseVerifyError", "LicenseVerifyErrorException! ! ! !");
            e.printStackTrace();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }
}