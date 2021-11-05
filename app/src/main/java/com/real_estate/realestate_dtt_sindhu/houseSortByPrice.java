package com.real_estate.realestate_dtt_sindhu;

import java.util.Comparator;

public class houseSortByPrice implements Comparator<DataModel>
{
    @Override
    public int compare(DataModel price1, DataModel price2) {
        return Integer.valueOf(price1.getPrice()).compareTo(Integer.valueOf(price2.getPrice()));
    }
}