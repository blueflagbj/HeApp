package com.wade.mobile.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.ailk.common.data.IData;
import com.wade.mobile.common.MobileException;
import com.wade.mobile.common.MobileLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: com.wade.mobile.common.db.DBHelper */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final Map<String, SQLiteDatabase> dbMap = new ConcurrentHashMap();

    static SQLiteDatabase getConnDatabase(Context context, String dbName) {
        if (dbMap.get(dbName) == null || !dbMap.get(dbName).isOpen()) {
            dbMap.put(dbName, openDatabase(context, dbName));
        }
        return dbMap.get(dbName);
    }

    private static SQLiteDatabase openDatabase(Context context, String dbName) {
        try {
            return context.openOrCreateDatabase(dbName, 0, (SQLiteDatabase.CursorFactory) null);
        } catch (SQLiteException e) {
            MobileLog.e(TAG, "Open Database:" + dbName + " Failed!", e);
            throw new MobileException("Open Database:" + dbName + " Failed!", e);
        }
    }

    static SQLiteDatabase getConnDatabase(Context context, DBInstance dbInstance) {
        if (dbMap.get(dbInstance.getDBName()) == null || !dbMap.get(dbInstance.getDBName()).isOpen()) {
            dbMap.put(dbInstance.getDBName(), openDatabase(context, dbInstance));
        }
        return dbMap.get(dbInstance.getDBName());
    }

    private static SQLiteDatabase openDatabase(Context context, DBInstance dbInstance) {
        try {
            return dbInstance.getWritableDatabase();
        } catch (SQLiteException e) {
            MobileLog.e(TAG, "Open Database:" + dbInstance.getDBName() + " Failed!", e);
            throw new MobileException("Open Database:" + dbInstance.getDBName() + " Failed!", e);
        }
    }

    public static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void close(String dbName) {
        SQLiteDatabase db = dbMap.get(dbName);
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public static void closeAll() {
        for (String str : dbMap.keySet()) {
            SQLiteDatabase db = dbMap.get(str.toString());
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public static ContentValues parseCvValue(IData data) {
        ContentValues cv = new ContentValues();
        for (Object obj : data.keySet()) {
            String key = obj.toString();
            cv.put(key, data.getString(key));
        }
        return cv;
    }

    public static String[] parseCvCond(IData data) {
        List<String> list = new ArrayList<>();
        for (Object obj : data.keySet()) {
            list.add(data.getString(obj.toString()));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}