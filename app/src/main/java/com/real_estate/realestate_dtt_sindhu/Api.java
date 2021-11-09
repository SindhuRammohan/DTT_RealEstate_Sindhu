package com.real_estate.realestate_dtt_sindhu;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Api {

    String BASE_URL = "https://intern.docker-dev.d-tt.nl";

    /**
     * The return type is important here
     * The class structure that you've defined in Call<T>
     * should exactly match with your json response
     * If you are not using another api, and using the same as mine
     * then no need to worry, but if you have your own API, make sure
     * you change the return type appropriately
     **/
    @Headers({"Access-Key: 98bww4ezuzfePCYFxJEWyszbUXc7dxRx"})
    @GET("/api/house")
    Call<List<DataModel>> getHouseList();
}