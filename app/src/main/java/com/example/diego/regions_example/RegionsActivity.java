package com.example.diego.regions_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class RegionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    RegionsAdapter mRegionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRegionAdapter = new RegionsAdapter(this);
        ListView l =  (ListView) findViewById(android.R.id.list);
        l.setAdapter(mRegionAdapter);
        l.setOnItemClickListener(this);
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
                Toast.makeText(this, "Cleared cache", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String regionID = mRegionAdapter.getCountryID(position);
        String regionName = mRegionAdapter.getItem(position);
        Intent i = new Intent(this, CountryListActivity.class);
        i.putExtra(CountryListActivity.REGION_ID, regionID);
        i.putExtra(CountryListActivity.REGION_NAME, regionName);
        i.putExtra(CountryListActivity.REGION_ENTRY, position);
        startActivity(i);
    }
}
