package com.example.diego.regions_example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by diego2 on 01/10/2017.
 *
 * A simple wrapper for ArrayAdapter that handles the display of regions,
 * by loading the list from resources.
 */

class RegionsAdapter extends ArrayAdapter<String> {
    private String regionsNames[];
    private String regionsIDs[];

    RegionsAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_list_item_1);
        regionsNames = context.getResources().getStringArray(R.array.region_names);
        regionsIDs = context.getResources().getStringArray(R.array.region_ids);
        addAll(regionsNames);
    }

    String getCountryID(int position) {
        return regionsIDs[position];
    }
};
