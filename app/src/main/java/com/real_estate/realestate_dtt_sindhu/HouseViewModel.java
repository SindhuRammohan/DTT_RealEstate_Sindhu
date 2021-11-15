package com.real_estate.realestate_dtt_sindhu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseViewModel extends ViewModel {
    private MutableLiveData<ArrayList<DataModel>> users;
    String imageurl = Api.BASE_URL;
    ArrayList<DataModel> dataModels;
    Call<ArrayList<DataModel>> call = RetrofitClient.getInstance().getMyApi().getHouseList();

    public MutableLiveData<ArrayList<DataModel>> getUserMutableLiveData() {
        return users;
    }

    public HouseViewModel() {
        users = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }
    public void init(){
        users = new MutableLiveData<>();
        loadUsers();
        users.setValue(dataModels);
    }


    public ArrayList<DataModel> loadUsers() {

        dataModels = new ArrayList<>();
        call.clone().enqueue(new Callback<ArrayList<DataModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DataModel>> call, @NonNull Response<ArrayList<DataModel>> response) {

                List<DataModel> houseList = response.body();
            if(houseList != null) {
                for (int i = 0; i < Objects.requireNonNull(houseList).size(); i++) {

                    dataModels.add(new DataModel(
                    houseList.get(i).getPrice(),
                    houseList.get(i).getZip(),
                    houseList.get(i).getCity(),
                    houseList.get(i).getBedrooms(),
                    houseList.get(i).getBathroom(),
                    houseList.get(i).getSizes(),
                    imageurl + houseList.get(i).getPicture_path(),
                    houseList.get(i).getDescription(),
                    houseList.get(i).getLat(),
                    houseList.get(i).getLongi()));
                    Collections.sort(dataModels, new HouseSortByPrice());
                }

            }

        }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DataModel>> call, @NonNull Throwable t) {
                //handle error or failure cases here
            }
        });
        return dataModels;
    }
}