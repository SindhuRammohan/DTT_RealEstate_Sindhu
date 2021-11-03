package com.real_estate.realestate_dtt_sindhu;

public class DataModel {

    String name;
    String type;
    String version_number;
    String feature;
    String sizes;
    String pic_path;

    public DataModel(String id, String zip, String bedrooms, String bathrooms,String size,String pic_path ) {
        this.name=id;
        this.type=zip;
        this.version_number=bedrooms;
        this.feature=bathrooms;
        this.sizes=size;
        this.pic_path=pic_path;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getFeature() {
        return feature;
    }
    public String getSizes() {
        return sizes;
    }public String getPicture_path() {
        return pic_path;
    }
}