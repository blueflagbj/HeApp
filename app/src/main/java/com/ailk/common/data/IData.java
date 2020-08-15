package com.ailk.common.data;

import java.io.Serializable;
import java.util.Map;

public interface IData extends Map<String, Object>, Serializable {
    boolean getBoolean(String str);

    boolean getBoolean(String str, boolean z);

    IData getData(String str);

    IData getData(String str, IData iData);

    IDataset getDataset(String str);

    IDataset getDataset(String str, IDataset iDataset);

    double getDouble(String str);

    double getDouble(String str, double d);

    int getInt(String str);

    int getInt(String str, int i);

    long getLong(String str);

    long getLong(String str, long j);

    String[] getNames();

    String getString(String str);

    String getString(String str, String str2);

    boolean isNoN(String str);

    IData subData(String str) throws Exception;

    IData subData(String str, boolean z) throws Exception;

    String toString();

}
