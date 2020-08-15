package com.ailk.common.data.impl;

import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.json.JSONArray;
import com.ailk.common.json.JSONObject;
import com.wade.mobile.util.Constant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatasetList extends ArrayList<Object> implements IDataset {
    private static final long serialVersionUID = 8302984775243577040L;

    public DatasetList() {
        super(20);
    }

    public DatasetList(int size) {
        super(size);
    }

    public DatasetList(IData data) {
        super(20);
        add(data);
    }

    public DatasetList(IData[] datas) {
        super(20);
        for (IData data : datas) {
            add(data);
        }
    }

    public DatasetList(IDataset list) {
        super(20);
        addAll(list);
    }

    public DatasetList(String jsonArray) {
        super(20);
        if (!(jsonArray == null || jsonArray.indexOf("\"class\":") == -1)) {
            jsonArray = jsonArray.replaceAll("\"class\":", "\"__classChangedByFrameWork__\":");
        }
        addAll(fromJSONArray(new JSONArray(parseJsonString(jsonArray))));
    }

    public DatasetList(JSONArray array) {
        super(20);
        addAll(fromJSONArray(array));
    }

    public static DatasetList fromJSONArray(JSONArray array) {
        if (array == null) {
            return null;
        }
        DatasetList list = new DatasetList();
        int cnt = array.length();
        for (int i = 0; i < cnt; i++) {
            Object value = array.get(i);
            if (value == null) {
                list.add((Object) null);
            } else if (value instanceof JSONObject) {
                list.add(JSONObject.NULL.equals(value) ? null : DataMap.fromJSONObject((JSONObject) value));
            } else if (value instanceof DataMap) {
                list.add((IData) value);
            } else if (value instanceof String) {
                if (!(value == null || ((String) value).indexOf("__classChangedByFrameWork__") == -1)) {
                    value = ((String) value).replaceAll("__classChangedByFrameWork__", Constant.ATTR_CLASS);
                }
                if (((String) value).startsWith("{")) {
                    list.add(new DataMap((String) value));
                } else if (((String) value).startsWith("[")) {
                    list.add(new DatasetList((String) value));
                } else {
                    list.add(value);
                }
            } else {
                list.add(value);
            }
        }
        return list;
    }

    public String[] getNames() {
        if (size() > 0) {
            return ((IData) get(0)).getNames();
        }
        return null;
    }

    public Object get(int index) {
        return super.get(index);
    }

    public Object get(int index, String name) {
        Object data = get(index);
        if (data == null || !(data instanceof Map)) {
            return null;
        }
        IData map = new DataMap();
        map.putAll((HashMap) data);
        return map.get(name);
    }

    public Object get(int index, String name, Object def) {
        Object value = get(index, name);
        return value == null ? def : value;
    }

    public IData getData(int index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return new DataMap((String) value);
        }
        if (value instanceof JSONObject) {
            return DataMap.fromJSONObject((JSONObject) value);
        }
        return (IData) value;
    }

    public IDataset getDataset(int index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return new DatasetList((String) value);
        }
        if (value instanceof JSONArray) {
            return fromJSONArray((JSONArray) value);
        }
        return (IDataset) value;
    }

    public IData first() {
        if (size() > 0) {
            return (IData) get(0);
        }
        return null;
    }

    public IData toData() {
        IData data = new DataMap();
        Iterator<Object> it = iterator();
        while (it.hasNext()) {
            IData element = (IData) it.next();
            for (String key : element.keySet()) {
                if (data.containsKey(key)) {
                    ((IDataset) data.get(key)).add(element.get(key));
                } else {
                    IDataset list = new DatasetList();
                    list.add(element.get(key));
                    data.put(key, list);
                }
            }
        }
        return data;
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
        return new JSONArray((Collection<Object>) this).toString();
    }

}
