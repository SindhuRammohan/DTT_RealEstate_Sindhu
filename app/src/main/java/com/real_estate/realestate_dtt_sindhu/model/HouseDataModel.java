package com.real_estate.realestate_dtt_sindhu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HouseDataModel implements Parcelable {
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
    String picturePath;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("description")
    String description;

    public HouseDataModel(String price, String zip, String city, String bedrooms,
                          String bathrooms, String sizes, String picturePath,
                          String description, String latitude, String longitude) {
        this.price = price;
        this.zip = zip;
        this.city = city;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.sizes = sizes;
        this.picturePath = picturePath;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected HouseDataModel(Parcel in) {
        price = in.readString();
        zip = in.readString();
        city = in.readString();
        bedrooms = in.readString();
        bathrooms = in.readString();
        sizes = in.readString();
        picturePath = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        description = in.readString();
    }

    public static final Creator<HouseDataModel> CREATOR = new Creator<HouseDataModel>() {
        @Override
        public HouseDataModel createFromParcel(Parcel in) {
            return new HouseDataModel(in);
        }

        @Override
        public HouseDataModel[] newArray(int size) {
            return new HouseDataModel[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public String getCity() {
        return city;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
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

    public String getPicturePath() {
        return picturePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(price);
        parcel.writeString(zip);
        parcel.writeString(city);
        parcel.writeString(bedrooms);
        parcel.writeString(bathrooms);
        parcel.writeString(sizes);
        parcel.writeString(picturePath);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(description);
    }
}