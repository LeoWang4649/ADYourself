package com.example.adyourself_fb;

public class LoveEboard_data {
    private String tmp_BoardID;
    private String tmp_Area;
    private String tmp_Photo;
    private String tmp_User;

    public LoveEboard_data(String tmp_BoardID, String tmp_Area,String tmp_Photo,String tmp_User) {
        this.tmp_BoardID = tmp_BoardID;
        this.tmp_Area = tmp_Area;
        this.tmp_Photo = tmp_Photo;
        this.tmp_User = tmp_User;


    }

    public String gettmp_BoardID() {
        return tmp_BoardID;
    }

    public String gettmp_Area() {
        return tmp_Area;
    }

    public String gettmp_Photo() {
        return tmp_Photo;
    }

    public String gettmp_User() {
        return tmp_User;
    }



    public void settmp_BoardID(String tmp_BoardID) {
        this.tmp_BoardID = tmp_BoardID;
    }

    public void settmp_Area(String tmp_Area) {
        this.tmp_Area = tmp_Area;
    }

    public void settmp_Photo(String tmp_Photo) {
        this.tmp_Photo = tmp_Photo;
    }

    public void settmp_User(String tmp_User) {
        this.tmp_User = tmp_User;
    }


}