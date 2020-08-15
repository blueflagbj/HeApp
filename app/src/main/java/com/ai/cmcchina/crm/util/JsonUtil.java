package com.ai.cmcchina.crm.util;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.ai.cmcchina.crm.util.JsonUtil */
public class JsonUtil {
    public static String getStringFromJSONObject(JSONObject json, String name, String defaultValue) {
        try {
            return json.getString(name);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static String getStringFromJSONArray(JSONArray json, int index, String name, String defaultValue) {
        try {
            return json.getJSONObject(index).getString(name);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public static JSONObject getJsonFromJSONArray(JSONArray json, int index) {
        try {
            return json.getJSONObject(index);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONObject getJsonFromJSON(JSONObject json, String name) {
        try {
            return json.getJSONObject(name);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONObject getJsonFromJSONObject(JSONObject json, String name) {
        try {
            return json.getJSONObject(name);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONArray getJsonArrayFromJSONObject(JSONObject json, String name) {
        try {
            return json.getJSONArray(name);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static JSONObject string2JSONObject(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
           e.printStackTrace();
            return new JSONObject();
        }
    }

    public static JSONArray string2JSONArray(String json) {
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static Map<String, Object> translateJsonToMap(JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                map.put(key, json.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}