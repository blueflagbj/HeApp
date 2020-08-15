package com.ailk.common.data;

import com.ailk.common.data.impl.Pagination;
import java.io.Serializable;

public interface IDataInput extends Serializable {
    IData getData();

    IData getHead();

    Pagination getPagination();

    void setPagination(Pagination pagination);

    String toString();
}