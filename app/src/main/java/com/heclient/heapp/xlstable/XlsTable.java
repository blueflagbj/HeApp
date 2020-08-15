package com.heclient.heapp.xlstable;

import java.util.ArrayList;
import java.util.List;

public class XlsTable {
    private Long       id;
    private String XlsPath;
    private String fileName;
    private String tag;
    private Integer sheetNum;// 第几个sheet
    private String sheetName ;//sheet 名称
   private List<XlsCells>cells = new ArrayList<XlsCells>();

    public String getFileName() {
        return fileName;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public String getXlsPath() {
        return XlsPath;
    }

    public void setXlsPath(String xlsPath) {
        XlsPath = xlsPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(Integer sheetNum) {
        this.sheetNum = sheetNum;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }


    public List<XlsCells> getCells() {
        return cells;
    }

    public void setCells(List<XlsCells> cells) {
        this.cells = cells;
    }
    public void addCells(XlsCells cell) {
        cells.add(cell);
    }

}
