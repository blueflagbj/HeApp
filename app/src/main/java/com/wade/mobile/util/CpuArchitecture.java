package com.wade.mobile.util;

import android.content.Context;
import android.util.Log;

import com.wade.mobile.app.MobileAppInfo;
import com.wade.mobile.common.MobileLog;
import com.wade.mobile.util.assets.AssetsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import android.os.SystemProperties;

public class CpuArchitecture {
    public static final String ABIS_ARM64_V8A = "arm64-v8a";
    public static final String ABIS_ARMEABI = "armeabi";
    public static final String ABIS_ARMEABI_V7A = "armeabi-v7a";
    public static final String ABIS_MIPS = "mips";
    public static final String ABIS_MIPS64 = "mips64";
    public static final String ABIS_X86 = "x86";
    public static final String ABIS_X86_64 = "x86_64";
    private static final String CPU_BIT_32 = "bit32";
    private static final String CPU_BIT_64 = "bit64";
    public static final String LIBS = "libs";
    private static final String LIBS_PATH = (LIBS + File.separator);
    public static final String TAG = CpuArchitecture.class.getName();
    private static final String CPU_ARCHITECTURE_DIR = (LIBS_PATH + getCpuArchitecture().replace('-', '_') + File.separator);
    public static final String CPU_ARCHITECTURE_PATH = (LIBS_PATH + getCpuArchitecture() + File.separator);

    static {
        System.loadLibrary("CpuArchitecture");
    }
    private static String getCpuArchitecture(){
        // String cpuAbi ="";
       String cpuAbi =SystemProperties.get("ro.product.cpu.abi");
      //  System.out.println("getCpuArchitecture0001:"+cpuAbi);
        return cpuAbi;
    }

    private static Object[] getCpu() {
        Object[] mArmArchitecture =null;

//ro.product.cpu.abi
        if ((Integer) mArmArchitecture[1] != -1) {
            return mArmArchitecture;
        }
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                String nameProcessor = "Processor";
                String nameFeatures = "Features";
                String nameModel = "model name";
                String nameCpuFamily = "cpu family";
                while (true) {
                    String line = br.readLine();
                    String[] pair = null;
                    if (line == null) {
                        break;
                    }
                    pair = line.split(":");
                    if (pair.length != 2)
                        continue;
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareTo(nameProcessor) == 0) {
                        String n = "";
                        for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {
                            String temp = val.charAt(i) + "";
                            if (temp.matches("\\d")) {
                                n += temp;
                            } else {
                                break;
                            }
                        }
                        mArmArchitecture[0] = "ARM";
                        mArmArchitecture[1] = Integer.parseInt(n);
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameFeatures) == 0) {
                        if (val.contains("neon")) {
                            mArmArchitecture[2] = "neon";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameModel) == 0) {
                        if (val.contains("Intel")) {
                            mArmArchitecture[0] = "INTEL";
                            mArmArchitecture[2] = "atom";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameCpuFamily) == 0) {
                        mArmArchitecture[1] = Integer.parseInt(val);
                        continue;
                    }
                }
            } finally {
                br.close();
                ir.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mArmArchitecture;
    }

    public static String getCpuBit() {
        return getCpuArchitecture().contains("64") ? CPU_BIT_64 : CPU_BIT_32;
    }

    public static void loadAssetsLib(Context context, boolean update, String... libNames) throws Exception {
        for (String libName : libNames) {
            if (!libName.endsWith(".so")) {
                libName = "lib" + libName + ".so";
            }
            String libAbsolutePath = copyAssetsLib(context, libName, libName, update);
            System.loadLibrary(libAbsolutePath);
        }
    }

    public static void loadSdcardLib(Context context, boolean update, String... libNames) throws Exception {
        for (String libName : libNames) {
          //  System.out.println("loadSdcardLib0001:"+libName);
            if (!libName.endsWith(".so")) {
                libName ="lib" + libName + ".so";
              //  System.out.println("loadSdcardLib0002:"+libName);
            }
           // System.out.println("loadSdcardLib0003:"+libName);
            String libAbsolutePath = copySdcardLib(context, libName, libName, update);
            //System.out.println("loadSdcardLib0004:"+libAbsolutePath);
            System.load(libAbsolutePath);
           // System.loadLibrary(libAbsolutePath);//lib/data/user/0/com.example.testhost/files/libs/libluajava.so.so
           // System.out.println("loadSdcardLib0005:"+libAbsolutePath);

        }
    }

    public static String copyAssetsLib(Context context, String libName, String targetName) throws Exception {
       String returnStr=copyAssetsLib(context, libName, targetName, false);
        Log.d(TAG, "copyAssetsLib: "+returnStr);
        return returnStr;
    }

    private static String copyAssetsLib(Context context, String libName, String targetName, boolean overload) throws Exception {
        File baseOutPath = new File(context.getFilesDir(), LIBS);
        File file = new File(baseOutPath, targetName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (!file.exists() || overload) {
            String originalFile = CPU_ARCHITECTURE_DIR + libName;

            AssetsUtil.copyAssetsFile(context, originalFile, file.getAbsolutePath());
            MobileLog.d(TAG, originalFile + " successfully written to " + file.getAbsolutePath());
        }
        return file.getAbsolutePath();
    }

    public static String copySdcardLib(Context context, String libName, String targetName) throws Exception {
        return copySdcardLib(context, libName, targetName, false);
    }

    public static String copySdcardLib(Context context, String libName, String targetName, boolean overload) throws Exception {
        File baseOutPath = new File(context.getFilesDir(), LIBS);
        File file = new File(baseOutPath, targetName);
        System.out.println("copySdcardLib0001:"+targetName);
        if (!file.getParentFile().exists()) {
            System.out.println("copySdcardLib0002:"+file.getPath());
            file.getParentFile().mkdirs();
        }
        if (!file.exists() || overload) {
            System.out.println("copySdcardLib0003:"+file.getPath());
            String originalFile = MobileAppInfo.getSdcardAppPath(context) + File.separator + CPU_ARCHITECTURE_PATH + targetName;
            System.out.println("copySdcardLib0004:"+originalFile);
            FileUtil.writeFile((InputStream) new FileInputStream(originalFile), file.getAbsolutePath());
            MobileLog.d(TAG, originalFile + " successfully written to " + file.getAbsolutePath());
        }
        System.out.println("copySdcardLib0005:"+file.getAbsolutePath());
        return file.getAbsolutePath();
    }

}