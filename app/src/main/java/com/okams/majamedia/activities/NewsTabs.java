package com.okams.majamedia.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okams.majamedia.R;
import com.okams.majamedia.adapters.NewsPagerAdapter;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.models.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewsTabs extends AppCompatActivity {

    NewsPagerAdapter adapter;
    ViewPager viewPager;
    Toolbar toolbar;
    ArrayList<Data> data = new ArrayList<>();
    private String TAG = Gmc.TagApp+getClass().getSimpleName();
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_tabs);
        setViews();
        setToolbar();
        getPassedParameters();
        setPagerAdapter();
    }

    private void getPassedParameters() {
        String dataListAsString = getIntent().getStringExtra(Gmc.p_list);
        position= getIntent().getIntExtra(Gmc.p_position,0);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Data>>(){}.getType();
        data = gson.fromJson(dataListAsString, type);
        for (Data item : data){
            Log.i(TAG, "item Data:"+item.getTitle());
        }
    }

    private void setPagerAdapter() {
        adapter = new NewsPagerAdapter(this,getSupportFragmentManager(),data);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }


    }
}
