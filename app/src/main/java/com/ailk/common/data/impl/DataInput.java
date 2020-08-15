package com.ailk.common.data.impl;

import com.ailk.common.data.IData;
import com.ailk.common.data.IDataInput;
import com.wade.mobile.util.Constant;

public class DataInput implements IDataInput {
    private static final long serialVersionUID = 1;
    private IData data;
    private IData head;
    private Pagination pagin;

    public DataInput() {
        this.pagin = null;
        this.head = new DataMap();
        this.data = new DataMap();
    }

    public DataInput(IData head2, IData data2) {
        this.pagin = null;
        this.head = head2;
        this.data = data2;
    }

    public IData getHead() {
        return this.head;
    }

    public void setHead(IData head2) {
        this.head = head2;
    }

    public IData getData() {
        return this.data;
    }

    public void setData(IData data2) {
        this.data = data2;
    }

    public Pagination getPagination() {
        return this.pagin;
    }

    public void setPagination(Pagination pagin2) {
        this.pagin = pagin2;
        if (pagin2 != null) {
            this.head.put(Pagination.X_PAGINCOUNT, String.valueOf(pagin2.getCount()));
            this.head.put(Pagination.X_PAGINCURRENT, String.valueOf(pagin2.getCurrent()));
            this.head.put(Pagination.X_PAGINSELCOUNT, String.valueOf(pagin2.isNeedCount()));
            this.head.put(Pagination.X_PAGINSIZE, String.valueOf(pagin2.getPageSize()));
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder(100);
        str.append("{");
        if (this.head != null) {
            str.append("\"head\":" + this.head.toString());
        }
        if (this.data != null) {
            if (this.head != null) {
                str.append(Constant.PARAMS_SQE);
            }
            str.append("\"data\":" + this.data.toString());
        }
        str.append("}");
        return str.toString();
    }
}