package com.ai.cmcchina.crm.bean;

public class Result {
    private String bean;
    private String beans;
    private String param;
    private String returnCode;
    private String returnMessage;
    private String tab;
    private String thead;

    public String getThead() {
        return this.thead;
    }

    public void setThead(String thead2) {
        this.thead = thead2;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param2) {
        this.param = param2;
    }

    public String getTab() {
        return this.tab;
    }

    public void setTab(String tab2) {
        this.tab = tab2;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode2) {
        this.returnCode = returnCode2;
    }

    public String getReturnMessage() {
        return this.returnMessage;
    }

    public void setReturnMessage(String returnMessage2) {
        this.returnMessage = returnMessage2;
    }

    public String getBean() {
        return this.bean;
    }

    public void setBean(String bean2) {
        this.bean = bean2;
    }

    public String getBeans() {
        return this.beans;
    }

    public void setBeans(String beans2) {
        this.beans = beans2;
    }
}