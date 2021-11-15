package com.real_estate.realestate_dtt_sindhu.services;

import com.real_estate.realestate_dtt_sindhu.model.DataModel;

import java.util.Comparator;

public class HouseSortByPrice implements Comparator<DataModel>
{
    @Override
    public int compare(DataModel price1, DataModel price2) {
        return Integer.valueOf(price1.getPrice()).compareTo(Integer.valueOf(price2.getPrice()));
    }
}