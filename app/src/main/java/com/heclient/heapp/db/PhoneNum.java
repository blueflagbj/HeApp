package com.heclient.heapp.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PhoneNum {
    @Id(autoincrement = true) ////这个注解就表明下面那个Id是个主键，必须用Long，否则会报错，autoincrement=true自增
    private long Id;
    private int phoneid;//序号
    private String phonenum;//手机号
    private int isDone = 0; //是否处理标识
    private String  doDate; //处理时间
    @Generated(hash = 1751342974)
    public PhoneNum(long Id, int phoneid, String phonenum, int isDone, String doDate) {
        this.Id = Id;
        this.phoneid = phoneid;
        this.phonenum = phonenum;
        this.isDone = isDone;
        this.doDate = doDate;
    }
    @Generated(hash = 1135809699)
    public PhoneNum() {
    }
    public long getId() {
        return this.Id;
    }
    public void setId(long Id) {
        this.Id = Id;
    }
    public int getPhoneid() {
        return this.phoneid;
    }
    public void setPhoneid(int phoneid) {
        this.phoneid = phoneid;
    }
    public String getPhonenum() {
        return this.phonenum;
    }
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }
    public int getIsDone() {
        return this.isDone;
    }
    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
    public String getDoDate() {
        return this.doDate;
    }
    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }
 
}
