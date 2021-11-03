package com.real_estate.realestate_dtt_sindhu;

import java.util.Comparator;

public class NameSorter implements Comparator<DataModel>
{
    @Override
    public int compare(DataModel o1, DataModel o2) {
        return Integer.valueOf(o1.getName()).compareTo(Integer.valueOf(o2.getName()));
    }
}