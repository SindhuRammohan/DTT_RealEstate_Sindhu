package com.real_estate.realestate_dtt_sindhu.services;

import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;

import java.util.Comparator;

public class HouseSortByPrice implements Comparator<HouseDataModel> {
    @Override
    public int compare(HouseDataModel price1, HouseDataModel price2) {
        return Integer.valueOf(price1.getPrice()).compareTo(Integer.valueOf(price2.getPrice()));
    }
}