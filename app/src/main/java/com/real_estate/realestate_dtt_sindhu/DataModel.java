package com.real_estate.realestate_dtt_sindhu;

public class DataModel {

    String price;
    String zip;
    String bedrooms;
    String bathrooms;
    String sizes;
    String pic_path;
    String lat;
    String longi;


    double distance;
    String description;
    public DataModel(String price, String zip, String bedrooms, String bathrooms,String size,String pic_path ,String description,double distance,String lat ,String longi) {
        this.price=price;
        this.zip=zip;
        this.bedrooms=bedrooms;
        this.bathrooms=bathrooms;
        this.sizes=size;
        this.pic_path=pic_path;
        this.description=description;
        this.distance=distance;
        this.lat=lat;
        this.longi=longi;

    }

    public String getPrice() {
        return price;
    }


    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }

    public double getdistance() {
        return distance;
    }
    public String getDescription() {
        return description;
    }
    public String getZip() {
        return zip;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public String getBathroom() {
        return bathrooms;
    }
    public String getSizes() {
        return sizes;
    }
    public String getPicture_path() {
        return pic_path;
    }
}