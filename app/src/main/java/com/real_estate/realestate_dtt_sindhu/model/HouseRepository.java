package com.real_estate.realestate_dtt_sindhu.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.real_estate.realestate_dtt_sindhu.network.Api;
import com.real_estate.realestate_dtt_sindhu.network.RetrofitClient;
import com.real_estate.realestate_dtt_sindhu.services.HouseSortByPrice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseRepository {
    private final ArrayList<HouseDataModel> dataModels = new ArrayList<>();
    private final MutableLiveData<ArrayList<HouseDataModel>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<HouseDataModel>> getMutableLiveData() {
        Call<ArrayList<HouseDataModel>> call = RetrofitClient.getInstance().getMyApi().getHouseList();
        call.clone().enqueue(new Callback<ArrayList<HouseDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HouseDataModel>> call, @NonNull Response<ArrayList<HouseDataModel>> response) {
                List<HouseDataModel> houseList = response.body();
                if (houseList != null) {
                    for (int i = 0; i < Objects.requireNonNull(houseList).size(); i++) {

                        dataModels.add(new HouseDataModel(
                                houseList.get(i).getPrice(),
                                houseList.get(i).getZip(),
                                houseList.get(i).getCity(),
                                houseList.get(i).getBedrooms(),
                                houseList.get(i).getBathroom(),
                                houseList.get(i).getSizes(),
                                Api.BASE_URL + houseList.get(i).getPicture_path(),
                                houseList.get(i).getDescription(),
                                houseList.get(i).getLat(),
                                houseList.get(i).getLongi()));
                        mutableLiveData.setValue(dataModels);
                        Collections.sort(dataModels, new HouseSortByPrice());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<HouseDataModel>> call, @NonNull Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}