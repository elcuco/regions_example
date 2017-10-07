package com.example.diego.regions_example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Diego on 10/3/2017.
 * Adapter to display a list of countries
 */

class CountryListAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private List<CountryDefinition> mCountries;
    private Context mContext;

    CountryListAdapter(@NonNull Context context, List<CountryDefinition> countries) {
        mCountries = countries;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCountries.size();
    }

    @Override
    public Object getItem(int i) {
        return mCountries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CountryDefinition country = (CountryDefinition) getItem(i);
        View convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView txt = convertView.findViewById(android.R.id.text1);
        String displayText = String.format(Locale.getDefault(), "%s - %3.0f (%s)", country.name, country.area, mContext.getString(R.string.KM));
        txt.setText(displayText);
        return convertView;
    }
}
