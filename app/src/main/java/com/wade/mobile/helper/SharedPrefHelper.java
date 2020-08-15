package com.wade.mobile.helper;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public final class SharedPrefHelper {
    private ContextWrapper context;

    public SharedPrefHelper(ContextWrapper context2) {
        this.context = context2;
    }

    public final String get(String sharedName, String key) {
        return this.context.getSharedPreferences(sharedName, 0).getString(key, "");
    }

    public final String get(String sharedName, String key, String defValue) {
        return this.context.getSharedPreferences(sharedName, 0).getString(key, defValue);
    }

    public final Map<String, String> get(String sharedName, String[] keys) {
        SharedPreferences sharedata = this.context.getSharedPreferences(sharedName, 0);
        Map<String, String> datas = new HashMap<>();
        for (String key : keys) {
            datas.put(key, sharedata.getString(key, ""));
        }
        return datas;
    }

    public final IData get(String sharedName, JSONArray keys) throws JSONException {
        SharedPreferences sharedata = this.context.getSharedPreferences(sharedName, 0);
        IData datas = new DataMap();
        int len = keys.length();
        for (int i = 0; i < len; i++) {
            String key = keys.getString(i);
            datas.put(key, sharedata.getString(key, ""));
        }
        return datas;
    }

    public final Map<?, ?> getAll(String sharedName) {
        return this.context.getSharedPreferences(sharedName, 0).getAll();
    }

    public final void put(String sharedName, String key, Object value) {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        put(shareEditor, key, value);
        shareEditor.commit();
    }

    public final void put(String sharedName, Map<?, ?> map) {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        for (Object obj : map.keySet()) {
            String key = obj.toString();
            put(shareEditor, key, map.get(key), false);
        }
        shareEditor.commit();
    }

    private final void put(SharedPreferences.Editor shareEditor, String key, Object value) {
        put(shareEditor, key, value, true);
    }

    private final void put(SharedPreferences.Editor shareEditor, String key, Object value, boolean bo) {
        if (value instanceof String) {
            shareEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            shareEditor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Boolean) {
            shareEditor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Long) {
            shareEditor.putLong(key, ((Long) value).longValue());
        } else if (value instanceof Float) {
            shareEditor.putFloat(key, ((Float) value).floatValue());
        } else if (bo) {
            throw new RuntimeException("SharedPrefHelper not support stored object");
           // throw new MobileException();
        }
    }

    public final void remove(String sharedName, String key) {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        shareEditor.remove(key);
        shareEditor.commit();
    }

    public final void remove(String sharedName, String[] keys) {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        for (String key : keys) {
            shareEditor.remove(key);
        }
        shareEditor.commit();
    }

    public final void remove(String sharedName, JSONArray keys) throws JSONException {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        int len = keys.length();
        for (int i = 0; i < len; i++) {
            shareEditor.remove(keys.getString(i));
        }
        shareEditor.commit();
    }

    public final void clear(String sharedName) {
        SharedPreferences.Editor shareEditor = this.context.getSharedPreferences(sharedName, 0).edit();
        shareEditor.clear();
        shareEditor.commit();
    }

}
