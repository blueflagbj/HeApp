package com.wade.mobile.func;

import android.content.ActivityNotFoundException;
import android.widget.Toast;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.DirectionUtil;
import com.wade.mobile.util.FileUtil;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;

public class MobileFile extends Plugin {
    private DirectionUtil util = DirectionUtil.getInstance(this.context);

    public MobileFile(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void cleanResource(JSONArray param) throws Exception {
        boolean isSdcard = true;
        String relativePath = param.getString(0);
        if (Constant.FALSE.equals(param.getString(1))) {
            isSdcard = false;
        }
        cleanResource(relativePath, isSdcard);
    }

    public void cleanResource(String relativePath, boolean isSdcard) {
        String absolutePath = this.util.getDirection(relativePath, isSdcard);
        FileUtil.deleteFolder(absolutePath);
        if (new File(absolutePath).list().length == 0) {
            Toast.makeText(this.context, "清除成功!", 0).show();
        } else {
            Toast.makeText(this.context, "清除失败!", 0).show();
        }
    }

    public void getAllFile(JSONArray param) throws Exception {
        callback(getAllFile(param.getString(0), param.getString(1), Constant.TRUE.equals(param.getString(2))).toString());
    }

    private JSONArray getAllFile(String fileName, String type, boolean isSdcard) {
        String[] fileNames = new File(DirectionUtil.getInstance(this.context).getDirection(DirectionUtil.getInstance(this.context).getRelativePath(fileName, type), isSdcard)).list();
        JSONArray arr = new JSONArray();
        if (fileNames != null) {
            for (String name : fileNames) {
                arr.put(name);
            }
        }
        return arr;
    }

    public void openFile(JSONArray param) throws Exception {
        boolean isSdcard = true;
        String relativePath = param.getString(0);
        if (Constant.FALSE.equals(param.getString(1))) {
            isSdcard = false;
        }
        openFile(relativePath, isSdcard);
    }

    public void openFile(String relativePath, boolean isSdcard) throws Exception {
        File file = new File(this.util.getDirection(relativePath, isSdcard));
        try {
            FileUtil.openFile(this.context, file);
        } catch (ActivityNotFoundException e) {
            FileUtil.openFile(this.context, file, FileUtil.MIME_MapTable[0][1]);
        }
    }

    public void readFile(JSONArray param) throws JSONException {
        boolean z = true;
        String fileName = param.getString(0);
        String type = param.getString(1);
        Boolean isSdcard = Boolean.valueOf(Constant.TRUE.equals(param.getString(2)));
        Boolean isEncode = Boolean.valueOf(param.getBoolean(3));
        try {
            String value = FileUtil.readFile(this.util.getDirection(DirectionUtil.getInstance(this.context).getRelativePath(fileName, type), isSdcard == null ? true : isSdcard.booleanValue()));
            if (isEncode != null) {
                z = isEncode.booleanValue();
            }
            callback(value, z);
        } catch (Exception e) {
            throw new MobileException((Throwable) e);
        }
    }

    public void appendFile(JSONArray param) throws Exception {
        String content = param.getString(0);
        String fileName = param.getString(1);
        String type = param.getString(2);
        writeFile(content, DirectionUtil.getInstance(this.context).getRelativePath(fileName, type), Constant.TRUE.equals(param.getString(3)), true);
    }

    public void writeFile(JSONArray param) throws Exception {
        String content = param.getString(0);
        String fileName = param.getString(1);
        String type = param.getString(2);
        writeFile(content, DirectionUtil.getInstance(this.context).getRelativePath(fileName, type), Constant.TRUE.equals(param.getString(3)), false);
    }

    public void writeFile(String content, String relativePath, boolean isSdcard, boolean append) throws Exception {
        String absolutePath = this.util.getDirection(relativePath, isSdcard);
        String parentPath = new File(absolutePath).getParent();
        if (!FileUtil.check(parentPath)) {
            FileUtil.createDir(parentPath);
        }
        FileUtil.writeFile(content, absolutePath, append);
    }

    public void getRelativePath(JSONArray params) throws Exception {
        callback(DirectionUtil.getInstance(this.context).getRelativePath(params.getString(0), params.getString(1)));
    }

    public void deleteFile(JSONArray param) throws Exception {
        String fileName = param.getString(0);
        String type = param.getString(1);
        deleteFile(DirectionUtil.getInstance(this.context).getRelativePath(fileName, type), Constant.TRUE.equals(param.getString(2)));
    }

    public void deleteFile(String relativePath, boolean isSdcard) {
        FileUtil.deleteFile(this.util.getDirection(relativePath, isSdcard));
    }
}