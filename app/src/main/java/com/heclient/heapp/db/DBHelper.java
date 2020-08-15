package com.heclient.heapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     * @param context   上下文对象
     * @param name      数据库名称
     * @param factory
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态
     */
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DBHelper(Context context, String name){
        this(context,name,VERSION);
    }
    //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        System.out.println("create a database");
        //execSQL用于执行SQL语句 创建表
        db.execSQL("create table user(id int,name varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("upgrade a database");
    }
}
