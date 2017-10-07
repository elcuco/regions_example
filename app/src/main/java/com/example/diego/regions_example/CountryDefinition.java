package com.example.diego.regions_example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 10/3/2017.
 * Create a country definition from a json object returned from https://restcountries.eu/rest/v2/regionalbloc/eu
 */

class CurrencyDetails {
    String code;
    String name;
    String symbol;

    static CurrencyDetails create(JSONObject object) throws JSONException {
        CurrencyDetails currency = new CurrencyDetails();
        currency.code = object.getString("code");
        currency.name = object.getString("name");
        currency.symbol = object.getString("symbol");
        return currency;
    }

    static ArrayList<CurrencyDetails> createList(String json) throws JSONException {
        ArrayList<CurrencyDetails> currencies = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0; i <  jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            currencies.add(CurrencyDetails.create(object));
        }
        return currencies;
    }
}

class CountryDefinition {
    String name;
    Double area;
    String alpha3Code;
    String capital;
    ArrayList<CurrencyDetails> currencies;
    ArrayList<String> borders;

    // SHUT UP JAVA
    @SuppressWarnings("WeakerAccess")
    public static CountryDefinition create(JSONObject object) throws JSONException {
        CountryDefinition country = new CountryDefinition();
        country.name = object.getString("name");
        country.capital = object.getString("capital");
        country.area = getDouble(object, "area", Double.NaN);
        country.alpha3Code = object.getString("alpha3Code");
        country.currencies = CurrencyDetails.createList(object.getString("currencies"));

        JSONArray arrJson = object.getJSONArray("borders");
        country.borders = new ArrayList<>(arrJson.length());
        for(int i = 0; i < arrJson.length(); i++) {
            country.borders.add(arrJson.getString(i));
        }
        return country;
    }

    // SHUT UP JAVA
    @SuppressWarnings("WeakerAccess")
    public static List<CountryDefinition> createList(JSONArray a) throws JSONException {
        ArrayList<CountryDefinition> countries = new ArrayList<>();
        for(int i = 0; i <  a.length(); i++) {
            JSONObject object = a.getJSONObject(i);
            CountryDefinition country = CountryDefinition.create(object);
            countries.add(country);
        }
        return countries;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // this should be in Globals.java or something

    // SHUT UP JAVA
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static long getInt(JSONObject object, String name, int defaultValue ) {
        if (!object.has(name)) {
            return defaultValue;
        }
        try {
            String s = object.getString(name);
            return Long.valueOf(s);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    // SHUT UP JAVA
    @SuppressWarnings("WeakerAccess")
    public static double getDouble(JSONObject object, String name, Double defaultValue ) {
        try {
            String s = object.getString(name);
            return Double.valueOf(s);
        } catch (JSONException | NullPointerException | NumberFormatException e) {
//            e.printStackTrace();
            return defaultValue;
        }
    }
}
