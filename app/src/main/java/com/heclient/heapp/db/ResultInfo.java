package com.heclient.heapp.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ResultInfo {
    @Id(autoincrement = true) //自增id
    private Long _id;
    private String X_RESULTINFO; //网络访问是否成功 "OK"
    private String returnCode; //业务返回值 0000 成功 9999 失败
    private String user_name; //业务简写名称 如"韩**"
    private String returnMsg; //业务处理情况返回值 如"处理成功"
    private String user_full_name; //用户完整名称
    private String phone;//"13598964803"
    private String isVip;//"1" "0"
    private String preSave;//"1125.19" 账户结余
    private String currentOfferId;//"100168001557" 服务人员ID
    private String state;//"正常"
    private String starLevelCreditLimit;//"50元"
    private String brandName;//"无"
    private String brandId;//"100011100001"
    private String username;//"韩东伟"
    private String balOrgName;//"西关服务厅"
    private String brandCode;//"G00"
    private String historyCost;//"0.00"
    private String validateLevel;//"0"
    private String createDate;//"2004-07-10" 开户日期
    private String offerName;//"移动30G套餐-98元套餐"
    private String regionId;//"K"
    private String noticeFlag;//"可催可停"
    private String banlance;//"1125.19"
    private String balOrgId;//"126771"
    private String regionName;//"许昌"
    private String cost;//"0.00"
    private String is4G;//"4G"
    private String starLevel;//"3"
    private String point;//"1492.00"
    private String currentOweFee;//"0.00"
    private String starLevelValue;//"三星级"
    private String age;//"188个月"
    private String broadbandSpeed;//"宽带速度：未开通"
    private String amountCredit;//"0"
    private String X_RESULTCODE;//"0"
    private String X_RECORDNUM;//1
    @Generated(hash = 2036260590)
    public ResultInfo(Long _id, String X_RESULTINFO, String returnCode,
            String user_name, String returnMsg, String user_full_name, String phone,
            String isVip, String preSave, String currentOfferId, String state,
            String starLevelCreditLimit, String brandName, String brandId,
            String username, String balOrgName, String brandCode,
            String historyCost, String validateLevel, String createDate,
            String offerName, String regionId, String noticeFlag, String banlance,
            String balOrgId, String regionName, String cost, String is4G,
            String starLevel, String point, String currentOweFee,
            String starLevelValue, String age, String broadbandSpeed,
            String amountCredit, String X_RESULTCODE, String X_RECORDNUM) {
        this._id = _id;
        this.X_RESULTINFO = X_RESULTINFO;
        this.returnCode = returnCode;
        this.user_name = user_name;
        this.returnMsg = returnMsg;
        this.user_full_name = user_full_name;
        this.phone = phone;
        this.isVip = isVip;
        this.preSave = preSave;
        this.currentOfferId = currentOfferId;
        this.state = state;
        this.starLevelCreditLimit = starLevelCreditLimit;
        this.brandName = brandName;
        this.brandId = brandId;
        this.username = username;
        this.balOrgName = balOrgName;
        this.brandCode = brandCode;
        this.historyCost = historyCost;
        this.validateLevel = validateLevel;
        this.createDate = createDate;
        this.offerName = offerName;
        this.regionId = regionId;
        this.noticeFlag = noticeFlag;
        this.banlance = banlance;
        this.balOrgId = balOrgId;
        this.regionName = regionName;
        this.cost = cost;
        this.is4G = is4G;
        this.starLevel = starLevel;
        this.point = point;
        this.currentOweFee = currentOweFee;
        this.starLevelValue = starLevelValue;
        this.age = age;
        this.broadbandSpeed = broadbandSpeed;
        this.amountCredit = amountCredit;
        this.X_RESULTCODE = X_RESULTCODE;
        this.X_RECORDNUM = X_RECORDNUM;
    }
    @Generated(hash = 1829158754)
    public ResultInfo() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getX_RESULTINFO() {
        return this.X_RESULTINFO;
    }
    public void setX_RESULTINFO(String X_RESULTINFO) {
        this.X_RESULTINFO = X_RESULTINFO;
    }
    public String getReturnCode() {
        return this.returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getReturnMsg() {
        return this.returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public String getUser_full_name() {
        return this.user_full_name;
    }
    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIsVip() {
        return this.isVip;
    }
    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }
    public String getPreSave() {
        return this.preSave;
    }
    public void setPreSave(String preSave) {
        this.preSave = preSave;
    }
    public String getCurrentOfferId() {
        return this.currentOfferId;
    }
    public void setCurrentOfferId(String currentOfferId) {
        this.currentOfferId = currentOfferId;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getStarLevelCreditLimit() {
        return this.starLevelCreditLimit;
    }
    public void setStarLevelCreditLimit(String starLevelCreditLimit) {
        this.starLevelCreditLimit = starLevelCreditLimit;
    }
    public String getBrandName() {
        return this.brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getBrandId() {
        return this.brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getBalOrgName() {
        return this.balOrgName;
    }
    public void setBalOrgName(String balOrgName) {
        this.balOrgName = balOrgName;
    }
    public String getBrandCode() {
        return this.brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    public String getHistoryCost() {
        return this.historyCost;
    }
    public void setHistoryCost(String historyCost) {
        this.historyCost = historyCost;
    }
    public String getValidateLevel() {
        return this.validateLevel;
    }
    public void setValidateLevel(String validateLevel) {
        this.validateLevel = validateLevel;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getOfferName() {
        return this.offerName;
    }
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
    public String getRegionId() {
        return this.regionId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public String getNoticeFlag() {
        return this.noticeFlag;
    }
    public void setNoticeFlag(String noticeFlag) {
        this.noticeFlag = noticeFlag;
    }
    public String getBanlance() {
        return this.banlance;
    }
    public void setBanlance(String banlance) {
        this.banlance = banlance;
    }
    public String getBalOrgId() {
        return this.balOrgId;
    }
    public void setBalOrgId(String balOrgId) {
        this.balOrgId = balOrgId;
    }
    public String getRegionName() {
        return this.regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    public String getCost() {
        return this.cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getIs4G() {
        return this.is4G;
    }
    public void setIs4G(String is4G) {
        this.is4G = is4G;
    }
    public String getStarLevel() {
        return this.starLevel;
    }
    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }
    public String getPoint() {
        return this.point;
    }
    public void setPoint(String point) {
        this.point = point;
    }
    public String getCurrentOweFee() {
        return this.currentOweFee;
    }
    public void setCurrentOweFee(String currentOweFee) {
        this.currentOweFee = currentOweFee;
    }
    public String getStarLevelValue() {
        return this.starLevelValue;
    }
    public void setStarLevelValue(String starLevelValue) {
        this.starLevelValue = starLevelValue;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getBroadbandSpeed() {
        return this.broadbandSpeed;
    }
    public void setBroadbandSpeed(String broadbandSpeed) {
        this.broadbandSpeed = broadbandSpeed;
    }
    public String getAmountCredit() {
        return this.amountCredit;
    }
    public void setAmountCredit(String amountCredit) {
        this.amountCredit = amountCredit;
    }
    public String getX_RESULTCODE() {
        return this.X_RESULTCODE;
    }
    public void setX_RESULTCODE(String X_RESULTCODE) {
        this.X_RESULTCODE = X_RESULTCODE;
    }
    public String getX_RECORDNUM() {
        return this.X_RECORDNUM;
    }
    public void setX_RECORDNUM(String X_RECORDNUM) {
        this.X_RECORDNUM = X_RECORDNUM;
    }



}
