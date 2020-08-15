package com.ailk.common.data.impl;

import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.json.JSONArray;
import com.ailk.common.json.JSONException;
import com.ailk.common.json.JSONObject;
import com.wade.mobile.util.Constant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataMap extends HashMap<String, Object> implements IData {
    private static final String CLASS_REP_STRING1 = "\"_^CCBW^_\":";
    private static final String CLASS_REP_STRING2 = "_^CCBW^_";
    private static final String CLASS_STRING1 = "\"class\":";
    private static final String CLASS_STRING2 = "class";
    private static final long serialVersionUID = 5728540280422795959L;

    public DataMap() {
    }

    public DataMap(int size) {
        super(size);
    }

    public DataMap(Map<String, Object> map) {
        super(map);
    }

    public DataMap(String jsonObject) throws JSONException {
        if (!(jsonObject == null || !jsonObject.contains(CLASS_STRING1))) {
            jsonObject = jsonObject.replaceAll(CLASS_STRING1, CLASS_REP_STRING1);
        }
        JSONObject map = new JSONObject(parseJsonString(jsonObject));
        if (map != null) {
            putAll(fromJSONObject(map));
        }
    }

    public static DataMap fromJSONObject(JSONObject paramJSONObject) {
        DataMap dataMap;
        if (paramJSONObject != null) {
            DataMap dataMap1 = new DataMap();
            Iterator<String> iterator = paramJSONObject.keys();
            while (true) {
                dataMap = dataMap1;
                if (iterator.hasNext()) {
                    String str2 = iterator.next();
                    Object object = paramJSONObject.get(str2);
                    String str1 = str2;
                    if (str2.indexOf("_^CCBW^_") != -1)
                        str1 = str2.replaceAll("_^CCBW^_", "class");
                    if (object != null) {
                        if (object instanceof JSONObject) {
                            DataMap dataMap2 =new DataMap();
                            if (JSONObject.NULL.equals(object)) {
                                str2 = null;
                            } else {
                                dataMap2 = fromJSONObject((JSONObject)object);
                            }
                            dataMap1.put(str1, dataMap2);
                            continue;
                        }
                        if (object instanceof JSONArray) {
                            DatasetList datasetList =new DatasetList();
                            if (JSONObject.NULL.equals(object)) {
                                str2 = null;
                            } else {
                                datasetList = DatasetList.fromJSONArray((JSONArray)object);
                            }
                            dataMap1.put(str1, datasetList);
                            continue;
                        }
                        if (object instanceof String) {
                            dataMap1.put(str1, object);
                            continue;
                        }
                        dataMap1.put(str1, object);
                        continue;
                    }
                    dataMap1.put(str1, object);
                    continue;
                }
                break;
            }
        } else {
            dataMap = null;
        }
        return dataMap;
    }

    public Object get(String key) {
        Object value = super.get(key);
        if (value == null) {
            return null;
        }
        if (JSONObject.NULL.equals(value)) {
            return null;
        }
        return value;
    }

    public String[] getNames() {
        String[] names = new String[size()];
        int index = 0;
        for (String str : keySet()) {
            names[index] = str;
            index++;
        }
        return names;
    }

    public boolean isNoN(String name) {
        return name == null || !containsKey(name);
    }

    public String getString(String name) {
        Object value = get(name);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public String getString(String name, String defaultValue) {
        String value = getString(name);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Constant.TRUE.equalsIgnoreCase(value.toString());
    }

    public double getDouble(String name) {
        return getDouble(name, 0.0d);
    }

    public double getDouble(String name, double defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Double.parseDouble(value.toString());
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Integer.parseInt(value.toString());
    }

    public long getLong(String name) {
        return getLong(name, 0);
    }

    public long getLong(String name, long defaultValue) {
        Object value = get(name);
        return value == null ? defaultValue : Long.parseLong(value.toString());
    }

    public IData getData(String name) {
        Object value = get(name);
        if (value == null) {
            return null;
        }
        if (value instanceof IData) {
            return (IData) value;
        }
        if (value instanceof String) {
            return new DataMap((String) value);
        }
        return null;
    }

    public IData getData(String name, IData def) {
        Object value = get(name);
        if (value == null) {
            return def;
        }
        if (value instanceof IData) {
            return (IData) value;
        }
        if (value instanceof String) {
            return new DataMap((String) value);
        }
        return def;
    }

    public IDataset getDataset(String name, IDataset def) {
        Object value = get(name);
        if (value == null) {
            return def;
        }
        if (value instanceof IDataset) {
            return (IDataset) value;
        }
        if (value instanceof JSONArray) {
            return DatasetList.fromJSONArray((JSONArray) value);
        }
        return def;
    }

    public IDataset getDataset(String name) {
        return getDataset(name, (IDataset) null);
    }

    public IData subData(String group) throws Exception {
        return subData(group, false);
    }

    public IData subData(String group, boolean istrim) throws Exception {
        String str;
        IData element = new DataMap();
        String prefix = group + Constant.SEPARATOR_UNDERLINE;
        for (String name : getNames()) {
            if (name.startsWith(prefix)) {
                if (istrim) {
                    str = name.substring(prefix.length());
                } else {
                    str = name;
                }
                element.put(str, get(name));
            }
        }
        return element;
    }

    public String put(String key, String value) {
        return (String) super.put(key, value);
    }

    public IData put(String key, IData value) {
        return (IData) super.put(key, value);
    }

    public IDataset put(String key, IDataset value) {
        return (IDataset) super.put(key, value);
    }

    private static String parseJsonString(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case 8:
                    sb.append("\\b");
                    break;
                case 9:
                    sb.append("\\t");
                    break;
                case 10:
                    sb.append("\\n");
                    break;
                case 12:
                    sb.append("\\f");
                    break;
                case 13:
                    sb.append("\\r");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public String toString() {
        return new JSONObject((Map<String, Object>) this).toString();
    }

    public static void main(String[] args) {
        IData d = new DataMap();
        d.put("A", (Object) null);
        d.put("B", "");
        System.out.println(new DataMap(d.toString()).toString());
    }

}
