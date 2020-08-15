package com.ai.cmcchina.crm.bean;

import java.io.Serializable;

/* renamed from: com.ai.cmcchina.crm.bean.Message */
public class Message implements Serializable {
    private static final long serialVersionUID = 1;
    String campSn;
    String endTime;
    String introduction;
    String messageSn;
    String offerCode;
    String offerId;
    String promptId;
    String smsPrompt;
    String startTime;
    String title;

    public String getOfferCode() {
        return this.offerCode;
    }

    public void setOfferCode(String offerCode2) {
        this.offerCode = offerCode2;
    }

    public String getOfferId() {
        return this.offerId;
    }

    public void setOfferId(String offerId2) {
        this.offerId = offerId2;
    }

    public String getCampSn() {
        return this.campSn;
    }

    public void setCampSn(String campSn2) {
        this.campSn = campSn2;
    }

    public String getSmsPrompt() {
        return this.smsPrompt;
    }

    public void setSmsPrompt(String smsPrompt2) {
        this.smsPrompt = smsPrompt2;
    }

    public String getPromptId() {
        return this.promptId;
    }

    public void setPromptId(String promptId2) {
        this.promptId = promptId2;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public void setIntroduction(String introduction2) {
        this.introduction = introduction2;
    }

    public String getMessageSn() {
        return this.messageSn;
    }

    public void setMessageSn(String messageSn2) {
        this.messageSn = messageSn2;
    }

    public String getStartTime() {
        if (!this.startTime.equals("")) {
            return this.startTime;
        }
        return "--";
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        if (!this.endTime.equals("")) {
            return this.endTime;
        }
        return "--";
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }
}