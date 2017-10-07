package com.example.diego.regions_example;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CountryListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String REGION_ID = "countryID";
    public static final String REGION_ENTRY = "countryEntry";
    public static final String REGION_NAME = "countryName";

    @SuppressWarnings("FieldCanBeLocal")
    private String mRegionName;
    private String mRegionID;

    @SuppressWarnings("FieldCanBeLocal")
    private TextView mLabel;
    private ListView mListView;
    private CountryListAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mListView = (ListView) findViewById(android.R.id.list);
        mLabel = (TextView) findViewById(android.R.id.title);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mListView.setOnItemClickListener(this);
        Intent i = getIntent();
        if (i!=null) {
            mRegionName = i.getStringExtra(REGION_NAME);
            mRegionID = i.getStringExtra(REGION_ID);
            mLabel.setText(mRegionName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRegion(mRegionID);
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
                Toast.makeText(this, "Cleared cache", Toast.LENGTH_SHORT).show();
                ResCountries.getInstance(this).clearCache();
                return true;
            default:
                return false;
        }
    }

    void loadRegion(String region) {
        mProgressBar.setVisibility(View.VISIBLE);
        ResCountries.getInstance(this).getRegion(region, new ResCountries.ResCountriesResult() {
            @Override
            public void onCountriesResultOK(String result, boolean cached) {
                if (!cached) {
                    Toast.makeText(CountryListActivity.this, "Fetched result online",Toast.LENGTH_SHORT).show();
                }
                displayRegion(result);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCountriesResultFail(Object error) {
                Log.d("LIST", "Could not load region: " + error.toString());
                displayFailedRegion();
            }
        });
    }

    private void displayFailedRegion() {
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

    private void displayRegion(String regiosnJSonText) {
        try {
            JSONArray a = new JSONArray(regiosnJSonText);
            List<CountryDefinition> countries = CountryDefinition.createList(a);
            Collections.sort(countries, new Comparator<CountryDefinition>() {
                @Override
                public int compare(CountryDefinition c1, CountryDefinition c2) {
                    if (Math.abs(c1.area - c2.area) < 0.01) {
                        return 0;
                    }
                    return c1.area > c2.area ? -1 : 1;
                }
            });
            mAdapter = new CountryListAdapter(this, countries);
            mListView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        CountryDefinition country = (CountryDefinition) mAdapter.getItem(position);
        Intent i = new Intent(this, CountryDetailsActivity.class);
        i.putExtra(CountryDetailsActivity.COUNTRY_ID, country.alpha3Code);
        startActivity(i);
    }
}
