package com.real_estate.realestate_dtt_sindhu;
import com.google.gson.annotations.SerializedName;
public class DataModel {
    @SerializedName("price")
    String price;
    @SerializedName("zip")
    String zip;
    @SerializedName("city")
    String city;
    @SerializedName("bedrooms")
    String bedrooms;
    @SerializedName("bathrooms")
    String bathrooms;
    @SerializedName("size")
    String sizes;
    @SerializedName("image")
    String pic_path;
    @SerializedName("latitude")
    String lat;
    @SerializedName("longitude")
    String longi;
    @SerializedName("description")
    String description;

    double distance;

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

    public void setdistance(double d) {
        this.distance = d;
    }
    public double getdistance() {
        return distance;
    }
    public String getDescription() {
        return description;
    }
    public String getZip() {
        return zip+city;
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