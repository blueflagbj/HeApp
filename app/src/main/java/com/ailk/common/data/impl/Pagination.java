package com.ailk.common.data.impl;

import java.io.Serializable;

public class Pagination implements Serializable {
    public static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_FETCH_SIZE = 2000;
    public static final int MAX_PAGE_SIZE = 500;
    public static final int MAX_RECODE_SIZE = Integer.MAX_VALUE;
    public static final String X_PAGINCOUNT = "X_PAGINCOUNT";
    public static final String X_PAGINCURRENT = "X_PAGINCURRENT";
    public static final String X_PAGINSELCOUNT = "X_PAGINSELCOUNT";
    public static final String X_PAGINSIZE = "X_PAGINSIZE";
    public static final String X_RESULTCOUNT = "X_RESULTCOUNT";
    private static final long serialVersionUID = 1;
    private long count;
    private int current = 1;
    private int currentSize = 0;
    private int fetchSize;
    private boolean needCount = true;
    private boolean onlyCount = false;
    private int originPageSize;
    private int pagesize;

    public Pagination() {
    }

    public Pagination(boolean isbatch, int pagesize2) {
        if (isbatch) {
            this.pagesize = pagesize2;
        }
    }

    public Pagination(int pagesize2) {
        this.pagesize = pagesize2;
    }

    public Pagination(int pagesize2, int current2) {
        this.pagesize = pagesize2;
        this.current = current2;
    }

    public boolean next() {
        if (((long) this.current) >= getPageCount()) {
            return false;
        }
        this.current++;
        return true;
    }

    public int getFetchSize() {
        if (this.fetchSize != 0 || this.pagesize <= 0) {
            this.fetchSize = 20;
        } else {
            this.fetchSize = this.pagesize;
        }
        return this.fetchSize;
    }

    public void setFetchSize(int fetchSize2) {
        if (fetchSize2 > 2000 || fetchSize2 < 0) {
            this.fetchSize = getDefaultPageSize();
        } else {
            this.fetchSize = fetchSize2;
        }
    }

    public static int getMaxPageSize() {
        return 500;
    }

    public static int getDefaultPageSize() {
        return 20;
    }

    public boolean isNeedCount() {
        return this.needCount;
    }

    public void setNeedCount(boolean needCount2) {
        this.needCount = needCount2;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count2) {
        this.count = count2;
    }

    public int getPageSize() {
        return this.pagesize;
    }

    public void setPageSize(int pagesize2) {
        this.pagesize = pagesize2;
    }

    public int getOriginPageSize() {
        return this.originPageSize;
    }

    public void setOriginPageSize(int originPageSize2) {
        this.originPageSize = originPageSize2;
    }

    public long getPageCount() {
        long pageCount = getCount() / ((long) getPageSize());
        if (pageCount == 0 || getCount() % ((long) getPageSize()) != 0) {
            return pageCount + 1;
        }
        return pageCount;
    }

    public boolean isOnlyCount() {
        return this.onlyCount;
    }

    public void setOnlyCount(boolean onlyCount2) {
        this.onlyCount = onlyCount2;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current2) {
        this.current = current2;
    }

    public int getStart() {
        if (this.current <= 1) {
            return 0;
        }
        return (this.current - 1) * this.pagesize;
    }

    public int getEnd() {
        if (this.current <= 1) {
            return this.pagesize;
        }
        return this.current * this.pagesize;
    }

    public int getCurrentSize() {
        return this.currentSize;
    }

    public void setCurrentSize(int currentSize2) {
        this.currentSize = currentSize2;
    }
}