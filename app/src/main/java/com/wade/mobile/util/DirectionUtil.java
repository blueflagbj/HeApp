package com.wade.mobile.util;

import android.content.Context;
import com.wade.mobile.app.MobileAppInfo;

public class DirectionUtil {
    private static Context context;

    private DirectionUtil() {
    }

    private static class InstanceHolder {
        /* access modifiers changed from: private */
        public static DirectionUtil instance = new DirectionUtil();

        private InstanceHolder() {
        }
    }

    public static DirectionUtil getInstance(Context context2) {
        if (context == null) {
            context = context2;
        }
        return InstanceHolder.instance;
    }

    public String getFileDirection(boolean isSdcard) {
        return getDirection(getFilePath(), isSdcard);
    }

    public String getImageDirection(boolean isSdcard) {
        return getDirection(getImagePath(), isSdcard);
    }

    public String getAudioDirection(boolean isSdcard) {
        return getDirection(getAudioPath(), isSdcard);
    }

    public String getVideoDirection(boolean isSdcard) {
        return getDirection(getVideoPath(), isSdcard);
    }

    public String getFilePath() {
        return getRelativePath((String) null, Constant.TYPE_FILE);
    }

    public String getImagePath() {
        return getRelativePath((String) null, Constant.TYPE_IMAGE);
    }

    public String getAudioPath() {
        return getRelativePath((String) null, Constant.TYPE_AUDIO);
    }

    public String getVideoPath() {
        return getRelativePath((String) null, Constant.TYPE_VIDEO);
    }

    public String getDirection(String relativePath, boolean isSdcard) {
        if (isSdcard) {
            return getDirectionInSdcard(relativePath);
        }
        return getDirectionInSandbox(relativePath);
    }

    public String getRelativePath(String relativePath, String type) {
        String path = null;
        if (Constant.TYPE_FILE.equals(type)) {
            path = Constant.PATH_FILE;
        } else if (Constant.TYPE_AUDIO.equals(type)) {
            path = Constant.PATH_AUDIO;
        } else if (Constant.TYPE_IMAGE.equals(type)) {
            path = Constant.PATH_IMAGE;
        } else if (Constant.TYPE_VIDEO.equals(type)) {
            path = Constant.PATH_VIDEO;
        }
        if (relativePath == null || path == null) {
            return path;
        }
        return FileUtil.connectFilePath(path, relativePath);
    }

    public String getDirectionInSdcard(String relativePath) {
        return FileUtil.connectFilePath(MobileAppInfo.getSdcardAppPath(context), relativePath);
    }

    public String getDirectionInSandbox(String relativePath) {
        return FileUtil.connectFilePath(context.getFilesDir().getAbsolutePath(), relativePath);
    }
}