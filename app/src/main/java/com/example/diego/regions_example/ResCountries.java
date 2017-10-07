package com.example.diego.regions_example;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Diego on 10/3/2017.
 * Inerface for reading information from https://restcountries.eu/rest/v2.
 *
 * All APIs are cached.
 *
 */

@SuppressWarnings("WeakerAccess")
public class ResCountries {
    // singleton
    @SuppressWarnings("WeakerAccess")
    public static final String BASE_URL = "https://restcountries.eu/rest/v2/";

    public interface ResCountriesResult {
        void onCountriesResultOK(String result, boolean cached);
        void onCountriesResultFail(Object error);
    }

    // TODO - can I find a better way
    private static final ResCountries ourInstance = new ResCountries();
    private Context mContext;

    @SuppressWarnings("WeakerAccess")
    public static ResCountries getInstance(Context ctx) {
        if (ourInstance.mContext == null) {
            ourInstance.mContext = ctx;
        }
        return ourInstance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // public API

    public void clearCache() {
        /*
        final File folder = ...
        final File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept( final File dir, final String name ) {
                return name.matches( "dailyReport_08.*\\.txt" );
            }
        } );
        */
        String[] files = mContext.fileList();
        for (String file: files) {
                if (file.contains("region-") || file.contains("country-")) {
                    mContext.deleteFile(file);
                }
        }
    }

    public void getRegion(String regionID, ResCountriesResult result) {
        String url = ResCountries.BASE_URL + "region/" + regionID;
        String fileName = "region-" + regionID + ".json";
        get(url, fileName, result);
    }

    public void getCountry(String countryID, ResCountriesResult result) {
        String url = ResCountries.BASE_URL + "alpha/" + countryID;
        String fileName = "country-" + countryID + ".json";
        get(url, fileName, result);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // generic API - don't use directly
    public void get(String url, String cacheFile, ResCountriesResult result) {
        try {
            String s = loadTextAsset(cacheFile);
            result.onCountriesResultOK(s, true);
        } catch (IOException e) {
            fetchFileFromServer(url, cacheFile, result);
        }
    }

    private void fetchFileFromServer(String url, final String cacheFile, final ResCountriesResult result) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        saveTextAsset(cacheFile, response);
                        result.onCountriesResultOK(response, false);
                    } catch (IOException e) {
                        result.onCountriesResultFail(e);
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result.onCountriesResultFail(error);
                }
            }
        );
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);
    }

    private String loadTextAsset(String fileName) throws IOException {
        FileInputStream in = mContext.openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        inputStreamReader.close();
        in.close();
        return sb.toString();
    }

    private void saveTextAsset(String filename, String data) throws IOException {
        FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(data.getBytes());
        fos.close();
    }
}
