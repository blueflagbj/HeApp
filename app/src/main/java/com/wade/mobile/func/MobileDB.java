package com.wade.mobile.func;

import android.content.Context;
import com.ailk.common.data.IData;
import com.ailk.common.data.IDataset;
import com.ailk.common.data.impl.DataMap;
import com.ailk.common.data.impl.DatasetList;
import com.wade.mobile.common.db.BasicDao;
import com.wade.mobile.common.db.DBHelper;
import com.wade.mobile.frame.IWadeMobile;
import com.wade.mobile.frame.plugin.Plugin;
import com.wade.mobile.util.Constant;
import com.wade.mobile.util.Utility;
import org.json.JSONArray;


public class MobileDB extends Plugin {
    private static final String ERROR = "NO";
    private static final String SUCCESS = "YES";

    public MobileDB(IWadeMobile wademobile) {
        super(wademobile);
    }

    public void execSQL(JSONArray param) throws Exception {
        callback(execSQL(param.getString(0), param.getString(1), param.getString(2), param.getString(3), param.getString(4)));
    }

    public String execSQL(String dbName, String execSQL, String bindArgs, String limit, String offset) {
        String str;
        IDataset resultDatas;
        if (isNull(dbName) || isNull(execSQL)) {
            Utility.error("参数为空异常!");
        }
        IData sqlParams = new DataMap();
        if (!isNull(bindArgs)) {
            sqlParams = new DataMap(bindArgs);
        }
        String execSQL2 = execSQL.toUpperCase();
        String[] names = sqlParams.getNames();
        for (int i = 0; i < names.length; i++) {
            execSQL2 = execSQL2.replaceAll(":V" + names[i].toUpperCase(), "'" + sqlParams.getString(names[i]) + "'");
        }
        try {
            BasicDao dao = new BasicDao((Context) this.context, dbName);
            if (execSQL2.trim().substring(0, 6).equals("SELECT")) {
                if (!isNull(limit)) {
                    execSQL2 = execSQL2 + " LIMIT " + limit;
                    if (!isNull(offset)) {
                        execSQL2 = execSQL2 + " OFFSET " + offset;
                    }
                }
                if (execSQL2.contains("?")) {
                    resultDatas = dao.select(execSQL2, DBHelper.parseCvCond(sqlParams));
                } else {
                    resultDatas = dao.select(execSQL2, (String[]) null);
                }
                str = resultDatas.toString();
            } else {
                dao.execSQL(execSQL2);
                str = SUCCESS;
                DBHelper.close(dbName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            str = ERROR;
        } finally {
            DBHelper.close(dbName);
        }
        return str;
    }

    public void insert(JSONArray param) throws Exception {
        callback(insert(param.getString(0), param.getString(1), param.getString(2)));
    }

    public String insert(String dbName, String table, String insertData) throws Exception {
        String str;
        if (isNull(dbName) || isNull(table) || isNull(insertData)) {
            Utility.error("参数为空异常!");
        }
        try {
            new BasicDao((Context) this.context, dbName).insert(table, new DataMap(insertData));
            str = SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            str = ERROR;
        } finally {
            DBHelper.close(dbName);
        }
        return str;
    }

    public void delete(JSONArray param) throws Exception {
        callback(delete(param.getString(0), param.getString(1), param.getString(2), param.getString(3)));
    }

    public String delete(String dbName, String table, String condSQL, String conds) throws Exception {
        String str;
        if (isNull(dbName) || isNull(table)) {
            Utility.error("参数为空异常!");
        }
        IData condsParam = new DataMap();
        if (!isNull(conds)) {
            condsParam = new DataMap(conds);
        }
        if (!isNull(condSQL)) {
            condSQL = condSQL.toUpperCase();
            String[] names = condsParam.getNames();
            for (int i = 0; i < names.length; i++) {
                condSQL = condSQL.replaceAll(":V" + names[i].toUpperCase(), "'" + condsParam.getString(names[i]) + "'");
            }
        }
        BasicDao dao = new BasicDao((Context) this.context, dbName);
        try {
            if (condSQL.contains("?")) {
                dao.delete(table, condSQL, condsParam);
            } else {
                dao.delete(table, condSQL, new String[0]);
            }
            str = SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            str = ERROR;
        } finally {
            DBHelper.close(dbName);
        }
        return str;
    }

    public void update(JSONArray param) throws Exception {
        callback(update(param.getString(0), param.getString(1), param.getString(2), param.getString(3), param.getString(4)));
    }

    public String update(String dbName, String table, String updateData, String condSQL, String conds) throws Exception {
        String str;
        if (isNull(dbName) || isNull(table) || isNull(updateData)) {
            Utility.error("参数为空异常!");
        }
        IData condsParam = new DataMap();
        if (!isNull(conds)) {
            condsParam = new DataMap(conds);
        }
        if (!isNull(condSQL)) {
            condSQL = condSQL.toUpperCase();
            String[] names = condsParam.getNames();
            for (int i = 0; i < names.length; i++) {
                condSQL = condSQL.replaceAll(":V" + names[i].toUpperCase(), "'" + condsParam.getString(names[i]) + "'");
            }
        }
        BasicDao dao = new BasicDao((Context) this.context, dbName);
        try {
            if (condSQL.contains("?")) {
                dao.update(table, (IData) new DataMap(updateData), condSQL, condsParam);
            } else {
                dao.update(table, (IData) new DataMap(updateData), condSQL, (IData) new DataMap());
            }
            str = SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            str = ERROR;
        } finally {
            DBHelper.close(dbName);
        }
        return str;
    }

    public void select(JSONArray param) throws Exception {
        callback(select(param.getString(0), param.getString(1), param.getString(2), param.getString(3), param.getString(4), param.getString(5), param.getString(6)));
    }

    public String select(String dbName, String table, String columns, String condSqlReq, String conds, String limit, String offset) throws Exception {
        String page;
        String str;
        IDataset resultDatas;
        if (isNull(dbName) || isNull(table)) {
            Utility.error("参数为空异常!");
        }
        IData condsParam = new DataMap();
        if (!isNull(conds)) {
            condsParam = new DataMap(conds);
        }
        String condSql = new String();
        if (!isNull(condSqlReq)) {
            String condSqlReq2 = condSqlReq.toUpperCase();
            String[] names = condsParam.getNames();
            for (int i = 0; i < names.length; i++) {
                condSql = condSqlReq2.replaceAll(":V" + names[i].toUpperCase(), "'" + condsParam.getString(names[i]) + "'");
            }
        }
        String[] columnsArr = new String[0];
        if (!isNull(columns)) {
            String columns2 = columns.substring(1, columns.length() - 1);
            if (!isNull(columns2)) {
                columnsArr = columns2.split(Constant.PARAMS_SQE);
            }
        }
        if (isNull(limit) || isNull(offset)) {
            page = limit;
        } else {
            page = Integer.valueOf(offset.substring(1, offset.length() - 1)) + Constant.PARAMS_SQE + Integer.valueOf(limit.substring(1, offset.length() - 1));
        }
        BasicDao dao = new BasicDao((Context) this.context, dbName);
        try {
            new DatasetList();
            if (condSql.contains("?")) {
                resultDatas = dao.select(false, table, columnsArr, condSql, DBHelper.parseCvCond(condsParam), (String) null, (String) null, (String) null, page);
            } else {
                resultDatas = dao.select(false, table, columnsArr, condSql, (String[]) null, (String) null, (String) null, (String) null, page);
            }
            str = resultDatas.toString();
        } catch (Exception e) {
            e.printStackTrace();
            str = ERROR;
        } finally {
            DBHelper.close(dbName);
        }
        return str;
    }
}