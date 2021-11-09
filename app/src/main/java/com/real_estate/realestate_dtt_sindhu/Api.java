package com.real_estate.realestate_dtt_sindhu;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Api {

    String BASE_URL = "https://intern.docker-dev.d-tt.nl";

    @Headers({"Access-Key: 98bww4ezuzfePCYFxJEWyszbUXc7dxRx"})
    @GET("/api/house")
    Call<List<DataModel>> getHouseList();
}