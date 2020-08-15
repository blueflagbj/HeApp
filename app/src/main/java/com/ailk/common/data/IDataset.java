package com.ailk.common.data;

import java.io.Serializable;
import java.util.List;

public interface IDataset extends List<Object>, Serializable {
    public static final int MAX_RECORD = 2000;
    public static final int ORDER_ASCEND = 0;
    public static final int ORDER_DESCEND = 1;
    public static final int TYPE_DOUBLE = 4;
    public static final int TYPE_INTEGER = 3;
    public static final int TYPE_STRING = 2;

    IData first();

    Object get(int i);

    Object get(int i, String str);

    Object get(int i, String str, Object obj);

    IData getData(int i);

    IDataset getDataset(int i);

    String[] getNames();

    IData toData();
}