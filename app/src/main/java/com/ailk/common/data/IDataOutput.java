package com.ailk.common.data;

import java.io.Serializable;

public interface IDataOutput extends Serializable {
    IDataset getData();

    long getDataCount();

    IData getHead();

    String toString();
}