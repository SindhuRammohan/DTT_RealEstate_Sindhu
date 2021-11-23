package com.real_estate.realestate_dtt_sindhu.network;


import com.real_estate.realestate_dtt_sindhu.services.Constants;
import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Api {
    String BASE_URL = Constants.BASE_URL;
    @Headers({Constants.KEY_HEADER})
    @GET(Constants.SUB_URL)
    Call<ArrayList<HouseDataModel>> getHouseList();
}