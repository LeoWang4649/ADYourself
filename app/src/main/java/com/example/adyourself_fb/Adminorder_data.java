package com.example.adyourself_fb;

public class Adminorder_data {
    private String tmp_AD;
    private String tmp_BoardID;
    private String tmp_Date;
    private String tmp_Pending;
    private String tmp_Price;
    private String tmp_Time;
    private String tmp_User;
    private String tmp_OrderID;
    private int tmp_OrderNum;

    public Adminorder_data(String tmp_AD, String tmp_BoardID, String tmp_Date, String tmp_Pending, String tmp_Price, String tmp_Time,String tmp_User,String tmp_OrderID,int tmp_OrderNum) {
        this.tmp_AD = tmp_AD;
        this.tmp_BoardID = tmp_BoardID;
        this.tmp_Date = tmp_Date;
        this.tmp_Pending = tmp_Pending;
        this.tmp_Price = tmp_Price;
        this.tmp_Time = tmp_Time;
        this.tmp_User = tmp_User;
        this.tmp_OrderID = tmp_OrderID;
        this.tmp_OrderNum = tmp_OrderNum;

    }

    public String gettmp_AD() {
        return tmp_AD;
    }

    public String gettmp_BoardID() {
        return tmp_BoardID;
    }

    public String gettmp_Date() {
        return tmp_Date;
    }

    public String gettmp_Pending() {
        return tmp_Pending;
    }

    public String gettmp_Price() {
        return tmp_Price;
    }

    public String gettmp_Time() {
        return tmp_Time;
    }

    public String gettmp_User() {
        return tmp_User;
    }

    public String gettmp_OrderID() {
        return tmp_OrderID;
    }

    public int gettmp_OrderNum() {
        return tmp_OrderNum;
    }



    public void settmp_AD(String tmp_AD) {
        this.tmp_AD = tmp_AD;
    }

    public void settmp_BoardID(String tmp_BoardID) {
        this.tmp_BoardID = tmp_BoardID;
    }

    public void settmp_Date(String tmp_Date) {
        this.tmp_Date = tmp_Date;
    }

    public void settmp_Pending(String tmp_Pending) {
        this.tmp_Pending = tmp_Pending;
    }

    public void settmp_Price(String tmp_Price) {
        this.tmp_Price = tmp_Price;
    }

    public void settmp_Time(String tmp_Time) {
        this.tmp_Time = tmp_Time;
    }

    public void settmp_User(String tmp_User) {
        this.tmp_User = tmp_User;
    }

    public void settmp_OrderID(String tmp_OrderID) {
        this.tmp_OrderID = tmp_OrderID;
    }

    public void settmp_OrderNum(int tmp_OrderNum) {
        this.tmp_OrderNum = tmp_OrderNum;
    }


}