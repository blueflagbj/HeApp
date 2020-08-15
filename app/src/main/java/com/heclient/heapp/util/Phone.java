package com.heclient.heapp.util;

public class Phone {
    private int id;
    private String num;
    private String returnMsg;


    public Phone(int id,String num,String returnMsg){
        this.id=id;
        this.num=num;
        this.returnMsg=returnMsg;

    }

    public  int getId(){
        return id;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
