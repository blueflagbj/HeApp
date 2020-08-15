package com.heclient.heapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.heclient.heapp.App;

import org.greenrobot.greendao.database.Database;

public class PhoneNumDAOUtil {
    private static String TAG = PhoneNumDAOUtil.class.getSimpleName();

    private static DaoSession daoSession;
    private Context context;

    private PhoneNumDAOUtil(Context context) {
        this.context = context;
    }

    public  PhoneNumDao getPhoneNumDao() {
        daoSession = ((App)this.context.getApplicationContext()).getDaoSession();
        return daoSession.getPhoneNumDao();
    }



}
