package com.wade.mobile.common.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: com.wade.mobile.common.db.DBInstance */
public abstract class DBInstance extends SQLiteOpenHelper {
    private String dbName;

    public DBInstance(Context context, String dbName2, int version) {
        super(context, dbName2, (SQLiteDatabase.CursorFactory) null, version);
        this.dbName = dbName2;
    }

    public DBInstance(Context context, String dbName2) {
        this(context, dbName2, 1);
    }

    public String getDBName() {
        return this.dbName;
    }
}