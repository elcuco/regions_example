package com.example.diego.regions_example;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CountryDetailsActivity extends AppCompatActivity {
    public static final String COUNTRY_ID = "COUNTRY_ID";

    String mCountryID;
    CountryDefinition mCountry;
    TextView mCountryTitle;
    TextView mCapitalName;
    TextView mCurrencyNames;
    TextView mBorders;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);
        mCountryTitle = (TextView) findViewById(R.id.country_name);
        mCapitalName = (TextView) findViewById(R.id.capital_name);
        mCurrencyNames = (TextView) findViewById(R.id.currency_name);
        mBorders = (TextView) findViewById(R.id.borders_name);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent i = getIntent();
        if (i!=null) {
            mCountryID = i.getStringExtra(COUNTRY_ID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCountry == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            ResCountries.getInstance(this).getCountry(mCountryID, new ResCountries.ResCountriesResult() {
                @Override
                public void onCountriesResultOK(String result, boolean cached) {
                    if (!cached) {
                        Toast.makeText(CountryDetailsActivity.this, "Fetched result online", Toast.LENGTH_SHORT).show();
                    }
                    mProgressBar.setVisibility(View.GONE);
                    displayCountry(result);
                }

                @Override
                public void onCountriesResultFail(Object error) {
                    Log.d("LIST", "Could not load country: " + error.toString());
                    displayFailedCountryFetch();
                }
            });
        } else {
            displayTheCountry();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                ResCountries.getInstance(this).clearCache();
                Toast.makeText(CountryDetailsActivity.this, "Cleared cache", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }


    private void displayCountry(String result) {
        try {
            JSONObject o = new JSONObject(result);
            mCountry = CountryDefinition.create(o);
            displayTheCountry();
        } catch (JSONException e) {
            e.printStackTrace();
            displayFailedCountryFetch();
        }
    }

    void displayTheCountry() {
        mCountryTitle.setText(mCountry.name);
        mCapitalName.setText(mCountry.capital);
        mBorders.setText(mCountry.borders.toString());

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CurrencyDetails currencyDetails: mCountry.currencies) {
            if (i!=0) {
                sb.append(",");
            }
            sb.append(currencyDetails.name);
            i++;
        }
        mCurrencyNames.setText(sb.toString());
    }

    private void displayFailedCountryFetch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Failed getting response");
        builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

