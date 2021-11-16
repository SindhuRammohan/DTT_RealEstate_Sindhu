package com.real_estate.realestate_dtt_sindhu.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class HouseViewModel extends AndroidViewModel {
    private final HouseRepository houseRepository;


    public HouseViewModel(@NonNull Application application) {
        super(application);
        houseRepository = new HouseRepository();

    }

    public LiveData<ArrayList<HouseDataModel>> getUserMutableLiveData() {
        return houseRepository.getMutableLiveData();
    }

}