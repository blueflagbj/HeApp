package com.ai.cmcchina.crm.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import androidx.preference.PreferenceManager;
import com.ai.cmcchina.crm.bean.OrderMarketingProduct;
import com.wade.mobile.util.Constant;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.ai.cmcchina.crm.util.OrderMarketingDBUtil */
public class OrderMarketingDBUtil extends SQLiteOpenHelper {
    public static final String NAME = "G3YYT";
    public static final int VERSION = 4;

    /* renamed from: com.ai.cmcchina.crm.util.OrderMarketingDBUtil$ProductDao */
    public interface ProductDao {
        public static final int MAXVALUE_COLLECTION = 15;

        void addAll(List<OrderMarketingProduct> list);

        void addProduct(OrderMarketingProduct orderMarketingProduct);

        void clear();

        int collect(OrderMarketingProduct orderMarketingProduct);

        void deleteFromCollections(OrderMarketingProduct orderMarketingProduct);

        List<OrderMarketingProduct> getByCondition(String str, String str2);

        List<OrderMarketingProduct> getCollections();

        List<OrderMarketingProduct> listAll();

        void recordFrequency(OrderMarketingProduct orderMarketingProduct);
    }

    /* renamed from: com.ai.cmcchina.crm.util.OrderMarketingDBUtil$Table */
    public class Table {
        public static final String ACCOUNT_PRODUCT = "account_product";
        public static final String PRODUCT = "product";

        public Table() {
        }
    }

    /* renamed from: com.ai.cmcchina.crm.util.OrderMarketingDBUtil$Column */
    public class Column {
        public static final String ACCOUNT = "account";
        public static final String CATEGORY = "CATEGORY";
        public static final String CODE = "code";
        public static final String COLLECT = "collect";
        public static final String DATA_STATUS = "data_status";
        public static final String DESC = "desc";
        public static final String FREQUENCY = "frequency";

        /* renamed from: ID */
        public static final String f944ID = "_id";
        public static final String NAME = "name";
        public static final String PID = "pid";
        public static final String TYPE = "type";

        public Column() {
        }
    }

    public OrderMarketingDBUtil(Context context) {
        super(context, "G3YYT", (SQLiteDatabase.CursorFactory) null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE product (_id INTEGER, CATEGORY VARCHAR(12), code VARCHAR(12), name VARCHAR(12), 'desc' VARCHAR(12), type VARCHAR(12), data_status INTEGER, CONSTRAINT pk_product PRIMARY KEY (_id,CATEGORY) ) ");
        db.execSQL(" CREATE TABLE account_product (account VARCHAR(12), pid INTEGER, frequency INTEGER, collect INTEGER, data_status INTEGER  ) ");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS account_product");
        onCreate(db);
    }

    /* renamed from: com.ai.cmcchina.crm.util.OrderMarketingDBUtil$ProductDaoImpl */
    public static class ProductDaoImpl implements ProductDao {
        private String account;
        private OrderMarketingDBUtil dbUtil;

        public ProductDaoImpl(Context context) {
            this.dbUtil = new OrderMarketingDBUtil(context);
            this.account = PreferenceManager.getDefaultSharedPreferences(context).getString("account", "");
        }

        public int collect(OrderMarketingProduct product) {
            int exists = exists(product);
            if (exists == 0) {
                addProduct(product);
            } else if (-1 == exists) {
            }
            int status = existsWithAccount(product);
            if (status == 0) {
                if (countCollections() >= 15) {
                    return -1;
                }
                addProductWithAccount1stTime(product, true);
                return 1;
            } else if (1 == status) {
                if (countCollections() >= 15) {
                    return -1;
                }
                turn2Collections(product);
                return 1;
            } else if (2 == status) {
                return 0;
            } else {
                return 0;
            }
        }

        public void addProduct(OrderMarketingProduct product) {
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("_id", product.getId());
            values.put("code", product.getCode());
            values.put("desc", product.getDesc());
            values.put("name", product.getName());
            values.put("type", product.getType());
            values.put("CATEGORY", product.getCategory());
            values.put("data_status", 1);
            db.insert("product", (String) null, values);
            db.close();
        }

        private void addProductWithAccount1stTime(OrderMarketingProduct product, boolean inCollect) {
            int i = 0;
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("account", this.account);
            values.put("pid", product.getId());
            values.put("collect", Integer.valueOf(inCollect ? 1 : 0));
            if (!inCollect) {
                i = 1;
            }
            values.put("frequency", Integer.valueOf(i));
            values.put("data_status", 1);
            db.insert("account_product", (String) null, values);
            db.close();
        }

        private int countCollections() {
            SQLiteDatabase db = this.dbUtil.getReadableDatabase();
            StringBuffer sql = new StringBuffer(" SELECT COUNT(*) FROM ");
            sql.append("account_product").append(" WHERE ").append("collect").append("=1 AND ").append("account").append(" =? ");
            Cursor cursor = db.rawQuery(sql.toString(), new String[]{this.account});
            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        public void recordFrequency(OrderMarketingProduct product) {
            int exists = exists(product);
            if (exists == 0) {
                addProduct(product);
            } else if (-1 == exists) {
            }
            if (existsWithAccount(product) == 0) {
                addProductWithAccount1stTime(product, false);
                return;
            }
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            db.execSQL("UPDATE account_product SET frequency = frequency+1 WHERE pid=? AND account =? ", new String[]{product.getId(), this.account});
            db.close();
        }

        public List<OrderMarketingProduct> getCollections() {
            ArrayList<OrderMarketingProduct> list = new ArrayList<>();
            SQLiteDatabase db = this.dbUtil.getReadableDatabase();
            StringBuffer sql = new StringBuffer(" SELECT * FROM ");
            sql.append("account_product").append(" a, ").append("product").append(" b WHERE a.").append("pid").append(" = b.").append("_id").append(" AND ").append(" a.").append("account").append(" =? AND a.").append("data_status").append("=1 AND b.").append("data_status").append("=1 AND a.").append("collect").append("=1 order by a.").append("frequency").append(" DESC ");
            Cursor cursor = db.rawQuery(sql.toString(), new String[]{this.account});
            while (cursor.moveToNext()) {
                OrderMarketingProduct product = new OrderMarketingProduct();
                product.setId(cursor.getString(cursor.getColumnIndex("_id")));
                product.setCode(cursor.getString(cursor.getColumnIndex("code")));
                product.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setType(cursor.getString(cursor.getColumnIndex("type")));
                list.add(product);
            }
            cursor.close();
            db.close();
            return list;
        }

        private int exists(OrderMarketingProduct product) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = this.dbUtil.getReadableDatabase();
                StringBuffer sql = new StringBuffer("SELECT * FROM ");
                sql.append("product").append(" WHERE ").append("_id").append(" =? ");
                cursor = db.rawQuery(sql.toString(), new String[]{product.getId()});
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    db.close();
                    return 0;
                } else if (1 != cursor.getInt(cursor.getColumnIndex("data_status"))) {
                    return -1;
                } else {
                    cursor.close();
                    db.close();
                    return 1;
                }
            } finally {
                cursor.close();
                db.close();
            }
        }

        private int existsWithAccount(OrderMarketingProduct product) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = this.dbUtil.getReadableDatabase();
                StringBuffer sql = new StringBuffer("SELECT * FROM ");
                sql.append("account_product").append(" WHERE ").append("pid").append(" =? AND ").append("account").append(" =? ");
                cursor = db.rawQuery(sql.toString(), new String[]{product.getId(), this.account});
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    db.close();
                    return 0;
                } else if (1 != cursor.getInt(cursor.getColumnIndex("data_status"))) {
                    return -1;
                } else if (1 != cursor.getInt(cursor.getColumnIndex("collect"))) {
                    cursor.close();
                    db.close();
                    return 1;
                } else {
                    cursor.close();
                    db.close();
                    return 2;
                }
            } finally {
                cursor.close();
                db.close();
            }
        }

        public void deleteFromCollections(OrderMarketingProduct product) {
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("collect", 0);
            db.update("account_product", values, "pid=? AND account =? ", new String[]{product.getId(), this.account});
            db.close();
        }

        private void turn2Collections(OrderMarketingProduct product) {
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("collect", 1);
            db.update("account_product", values, "pid=? AND account =? ", new String[]{product.getId(), this.account});
            db.close();
        }

        public List<OrderMarketingProduct> listAll() {
            ArrayList<OrderMarketingProduct> list = new ArrayList<>();
            SQLiteDatabase db = this.dbUtil.getReadableDatabase();
            StringBuffer sql = new StringBuffer(" SELECT * FROM ");
            sql.append("product");
            Cursor cursor = db.rawQuery(sql.toString(), new String[0]);
            while (cursor.moveToNext()) {
                OrderMarketingProduct product = new OrderMarketingProduct();
                product.setId(cursor.getString(cursor.getColumnIndex("_id")));
                product.setCode(cursor.getString(cursor.getColumnIndex("code")));
                product.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setType(cursor.getString(cursor.getColumnIndex("type")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
                list.add(product);
            }
            cursor.close();
            db.close();
            return list;
        }

        public void clear() {
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            db.execSQL("DELETE FROM product");
            db.execSQL("VACUUM;");
            db.close();
        }

        public List<OrderMarketingProduct> getByCondition(String code, String name) {
            ArrayList<OrderMarketingProduct> list = new ArrayList<>();
            SQLiteDatabase db = this.dbUtil.getReadableDatabase();
            StringBuffer sql = new StringBuffer(" SELECT * FROM ");
            sql.append("product").append(" a ").append(" WHERE a.").append("code").append(" LIKE ? ").append(" AND a.").append("name").append(" LIKE ? ");
            Cursor cursor = db.rawQuery(sql.toString(), new String[]{"%" + code + "%", "%" + name + "%"});
            while (cursor.moveToNext()) {
                OrderMarketingProduct product = new OrderMarketingProduct();
                product.setId(cursor.getString(cursor.getColumnIndex("_id")));
                product.setCode(cursor.getString(cursor.getColumnIndex("code")));
                product.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
                product.setName(cursor.getString(cursor.getColumnIndex("name")));
                product.setType(cursor.getString(cursor.getColumnIndex("type")));
                product.setCategory(cursor.getString(cursor.getColumnIndex("CATEGORY")));
                list.add(product);
            }
            cursor.close();
            db.close();
            return list;
        }

        public void addAll(List<OrderMarketingProduct> list) {
            SQLiteDatabase db = this.dbUtil.getWritableDatabase();
            StringBuilder sql = new StringBuilder("insert into ");
            sql.append("product").append("(").append("_id").append(Constant.PARAMS_SQE).append("code").append(Constant.PARAMS_SQE).append("desc").append(Constant.PARAMS_SQE).append("name").append(Constant.PARAMS_SQE).append("type").append(Constant.PARAMS_SQE).append("CATEGORY").append(Constant.PARAMS_SQE).append("data_status").append(") values(?,?,?,?,?,?,?)");
            SQLiteStatement statement = db.compileStatement(sql.toString());
            db.beginTransaction();
            for (OrderMarketingProduct product : list) {
                statement.bindString(1, product.getId());
                statement.bindString(2, product.getCode());
                statement.bindString(3, product.getDesc());
                statement.bindString(4, product.getName());
                statement.bindString(5, product.getType());
                statement.bindString(6, product.getCategory());
                statement.bindString(7, "1");
                try {
                    statement.executeInsert();
                } catch (Exception e) {
                    e.printStackTrace();
                   // G3Logger.debug(e);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }
}