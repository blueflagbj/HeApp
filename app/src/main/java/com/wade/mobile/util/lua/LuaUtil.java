package com.wade.mobile.util.lua;

import android.app.Activity;

import com.wade.mobile.common.MobileLog;
import com.wade.mobile.util.FileUtil;
import com.wade.mobile.util.assets.AssetsUtil;
import com.wade.mobile.util.assets.IAssetsFileOperation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LuaUtil {
    private static LuaState luaState = null;
    private static final String TAG = LuaUtil.class.getSimpleName();

    private static void init() {
        luaState = LuaStateFactory.newLuaState();
        luaState.openLibs();
    }

    public static Object execLua(String funcName, Object... params) throws LuaException {
        if (luaState == null) {
            init();
        }
        luaState.getField(LuaState.LUA_GLOBALSINDEX.intValue(), funcName);
        if (params == null) {
            params = new Object[0];
        }
        for (Object param : params) {
            luaState.pushObjectValue(param);
        }
        luaState.call(params.length, 1);
        luaState.setField(LuaState.LUA_GLOBALSINDEX.intValue(), "LUA_RESULT");
        LuaObject luaObject = luaState.getLuaObject("LUA_RESULT");
        if (luaObject.isString()) {
            if (!luaObject.isNumber()) {
                return luaObject.getString();
            }
            return Double.valueOf(luaObject.getNumber());
        } else if (luaObject.isBoolean()) {
            return Boolean.valueOf(luaObject.getBoolean());
        } else {
            if (luaObject.isJavaObject()) {
                return luaObject.getObject();
            }
            return null;
        }
    }

    public static void close() {
        if (luaState != null && !luaState.isClosed()) {
            luaState.close();
            luaState = null;
        }
    }

    public static void loadLuaScript(String lua) {
        if (luaState == null) {
            init();
        }
        luaState.LdoString(lua);
    }

    public static void loadLuaFile(String file) {
        if (luaState == null) {
            init();
        }
        luaState.LdoFile(file);
       // luaState.
    }

    public static void luaTransfer(final Activity context, final String assetsPath) throws Exception {
        AssetsUtil.traversal(context, assetsPath, new IAssetsFileOperation() {
            private String baseDir = context.getApplicationContext().getFilesDir().getCanonicalPath();
            private String separator = File.separator;

            public boolean fileFliter(String name) {
                if (name.endsWith(".lua")) {
                    return true;
                }
                return false;
            }

            public void fileDo(InputStream is, String filePath) throws Exception {
                String filePath2 = this.baseDir + this.separator + assetsPath + this.separator + filePath;
                System.out.println("LuaUtil-fileDo:0"+filePath);
                System.out.println("LuaUtil-fileDo:1"+filePath2);
                FileUtil.createDir(filePath2.substring(0, filePath2.lastIndexOf(this.separator)));
                FileOutputStream fs = null;
                fs = new FileOutputStream(filePath2);
                try {
                    byte[] arrayOfByte = new byte[1024];
                    while (true) {
                        int i = is.read(arrayOfByte);
                        if (i > 0) {
                            fs.write(arrayOfByte, 0, i);
                            continue;
                        }
                        MobileLog.d(TAG, "copy file : "+filePath2);
                        return;
                    }
                } finally {
                    fs.close();
                }
            }
        });
    }
}