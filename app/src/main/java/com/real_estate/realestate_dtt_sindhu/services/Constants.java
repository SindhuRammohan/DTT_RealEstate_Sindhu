package com.real_estate.realestate_dtt_sindhu.services;

public class Constants {
    public static final String BASE_URL = "https://intern.docker-dev.d-tt.nl";
    public static final String SUB_URL = "/api/house";
    public static final String KEY_HEADER = "Access-Key: 98bww4ezuzfePCYFxJEWyszbUXc7dxRx";

    //60 is the number of minutes in a degree
    //1.1515 is the number of statute miles in a nautical mile
    //1.609344 is the number of kilometres in a mile
    public static final int minutes = 60;
    public static final double miles = 1.1515;
    public static final double kilometer = 1.609344;
    public static final double degree = 180.0;

    //request code for Location Permission
    public static final int PERMISSION_ID = 44;

    public static final String KEY = "houseList";
}