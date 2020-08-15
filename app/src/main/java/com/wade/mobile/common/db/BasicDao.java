package com.wade.mobile.common.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.data.impl.DataMap;
import com.ailk.common.data.impl.DatasetList;

/* renamed from: com.wade.mobile.common.db.BasicDao */
public class BasicDao {
    private static final String LIMIT = "500";
    protected final Context context;
    protected DBInstance dbInstance;
    protected final String dbName;

    public BasicDao(Context context2, String dbName2) {
        this.dbName = dbName2;
        this.context = context2;
    }

    public BasicDao(Context context2, DBInstance dbInstance2) {
        this.dbName = dbInstance2.getDBName();
        this.context = context2;
        this.dbInstance = dbInstance2;
    }

    public String getDBName() {
        return this.dbName;
    }

    /* access modifiers changed from: protected */
    public SQLiteDatabase getConnDatabase() {
        if (this.dbInstance == null) {
            return DBHelper.getConnDatabase(this.context, this.dbName);
        }
        return DBHelper.getConnDatabase(this.context, this.dbInstance);
    }

    /* access modifiers changed from: protected */
    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (limit == null) {
            limit = LIMIT;
        }
        return getConnDatabase().query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /* access modifiers changed from: protected */
    public Cursor query(String sql, String[] selectionArgs) {
        return getConnDatabase().rawQuery(sql, selectionArgs);
    }

    private IDataset select(Cursor cur) {
        try {
            IDataset result = new DatasetList();
            cur.moveToFirst();
            String[] names = cur.getColumnNames();
            while (!cur.isAfterLast()) {
                IData raw = new DataMap();
                int len = names.length;
                for (int i = 0; i < len; i++) {
                    raw.put(names[i], cur.getString(i));
                }
                result.add(raw);
                cur.moveToNext();
            }
            return result;
        } finally {
            DBHelper.close(cur);
        }
    }

    public IDataset select(boolean distinct, String table, String[] columns, String condSql, String[] conds, String groupBy, String having, String orderBy, String limit) {
        return select(query(distinct, table, columns, condSql, conds, groupBy, having, orderBy, limit));
    }

    public IDataset select(String sql, String[] selectionArgs) {
        return select(query(sql, selectionArgs));
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return getConnDatabase().insert(table, nullColumnHack, values);
    }

    public long insert(String table, IData data) {
        return insert(table, (String) null, DBHelper.parseCvValue(data));
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return getConnDatabase().delete(table, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, IData conds) {
        return getConnDatabase().delete(table, whereClause, DBHelper.parseCvCond(conds));
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getConnDatabase().update(table, values, whereClause, whereArgs);
    }

    public int update(String table, IData datas, String condSQL, IData conds) {
        return update(table, DBHelper.parseCvValue(datas), condSQL, DBHelper.parseCvCond(conds));
    }

    public void execSQL(String sql) {
        getConnDatabase().execSQL(sql);
    }

    public void execSQL(String sql, Object[] bindArgs) {
        getConnDatabase().execSQL(sql, bindArgs);
    }
}