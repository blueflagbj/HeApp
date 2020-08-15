package com.ailk.common.data.impl;

import com.ailk.common.data.IData;
import com.ailk.common.data.IDataOutput;
import com.ailk.common.data.IDataset;
import com.wade.mobile.util.Constant;

public class DataOutput implements IDataOutput {
    private static final long serialVersionUID = 1;
    private IDataset data;
    private IData head;

    public DataOutput() {
        this.head = new DataMap();
        this.data = new DatasetList();
    }

    public DataOutput(IData head2, IDataset data2) {
        this.head = head2;
        this.data = data2;
    }

    public IDataset getData() {
        return this.data;
    }

    public void setData(IDataset data2) {
        this.data = data2;
    }

    public IData getHead() {
        return this.head;
    }

    public void setHead(IData head2) {
        this.head = head2;
    }

    public long getDataCount() {
        return this.head.getLong(Pagination.X_RESULTCOUNT, 0);
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