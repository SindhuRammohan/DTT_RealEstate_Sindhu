package com.real_estate.realestate_dtt_sindhu.model;

import com.google.gson.annotations.SerializedName;


public class HouseDataModel {
    @SerializedName("price")
    String strPrice;
    @SerializedName("zip")
    String strZip;
    @SerializedName("city")
    String strCity;
    @SerializedName("bedrooms")
    String strBedrooms;
    @SerializedName("bathrooms")
    String strBathrooms;
    @SerializedName("size")
    String strSizes;
    @SerializedName("image")
    String strPic_path;
    @SerializedName("latitude")
    String strLatitude;
    @SerializedName("longitude")
    String strLongitude;
    @SerializedName("description")
    String strDescription;


    public HouseDataModel(String strPrice, String strZip, String strCity, String strBedrooms, String strBathrooms, String strSizes, String strPic_path, String strDescription, String strLatitude, String strLongitude) {
        this.strPrice = strPrice;
        this.strZip = strZip;
        this.strCity = strCity;
        this.strBedrooms = strBedrooms;
        this.strBathrooms = strBathrooms;
        this.strSizes = strSizes;
        this.strPic_path = strPic_path;
        this.strDescription = strDescription;
        this.strLatitude = strLatitude;
        this.strLongitude = strLongitude;
    }

    public String getPrice() {
        return strPrice;
    }

    public String getCity() {
        return strCity;
    }

    public String getLat() {
        return strLatitude;
    }

    public String getLongi() {
        return strLongitude;
    }

    public String getDescription() {
        return strDescription;
    }

    public String getZip() {
        return strZip;
    }

    public String getBedrooms() {
        return strBedrooms;
    }

    public String getBathroom() {
        return strBathrooms;
    }

    public String getSizes() {
        return strSizes;
    }

    public String getPicture_path() {
        return strPic_path;
    }
}