package com.okams.majamedia.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okams.majamedia.R;
import com.okams.majamedia.adapters.NewsPagerAdapter;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.models.Data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class ScrollingActivity extends AppCompatActivity {

    NewsPagerAdapter adapter;
    ViewPager viewPager;
    ArrayList<Data> data = new ArrayList<>();
    private String TAG = Gmc.TagApp+getClass().getSimpleName();
    private int position = 0;
//    NestedScrollView scrollView;
    FloatingActionButton fab;
    CollapsingToolbarLayout toolbar_layout;
    SimpleDraweeView img_news;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_scrolling);

        setToolbar();
        setViews();
        getPassedParameters();
        setPagerAdapter();
        setListener();
    }

    private void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                toolbar_layout.setTitle(data.get(position).getTitle());
                Uri imageUri = Uri.parse(data.get(position).getMain_img());
                img_news.setImageURI(imageUri);
                txt_title.setText(data.get(position).getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    void setViews(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        img_news = (SimpleDraweeView) findViewById(R.id.img_news);
        toolbar_layout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbar_layout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//        scrollView = (NestedScrollView) findViewById (R.id.nest_scrollview);
        txt_title = (TextView) findViewById(R.id.txt_title);
//        scrollView.setFillViewport (true);
    }

    private void setPagerAdapter() {
        adapter = new NewsPagerAdapter(this,getSupportFragmentManager(),data);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
//        toolbar_layout.setTitle(data.get(position).getTitle());
        Uri imageUri = Uri.parse(data.get(position).getMain_img());
        img_news.setImageURI(imageUri);
        txt_title.setText(data.get(position).getTitle());

    }
}
