package com.okams.majamedia.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.okams.majamedia.R;
import com.okams.majamedia.adapters.EndlessRecycler;
import com.okams.majamedia.adapters.RCV_News;
import com.okams.majamedia.methods.CheckToken;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.methods.Parser;
import com.okams.majamedia.models.Data;
import com.okams.majamedia.networking.VolleyGet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CheckToken.TokenCallBack, VolleyGet.VolleyGetCallBack, RCV_News.RCVClickListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView rcv_news;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    private String TAG = Gmc.TagApp+getClass().getSimpleName();
    private SharedPreferences sPref;
    private CheckToken checkToken;
    private int i_main = 1;
    RCV_News adapter;
    ArrayList<Data> dataList = new ArrayList<>();
    private int cPage = 0;
    ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_main);
        sPref = getSharedPreferences(Gmc.AppName,0);

        setToolbar();
        setViews();
        setNavigationBar();
        setListeners();
        setListAdapter();
        checkForToken();
    }

    private void getRemoteData(int page) {
///?token=123234234&page=0
        Gmc.toggleVisibility(true,pb_loading);
        long token = sPref.getLong(Gmc.sp_Token,0);
        String url = Gmc.MainAPI+"/?token="+token+"&page="+page;
        VolleyGet volleyGet = new VolleyGet(this,i_main,url);
        volleyGet.setVolleyGetCallBack(this);
    }

    private void checkForToken() {
        long ttl = sPref.getLong(Gmc.sp_Ttl,0);
        Log.v(TAG,"ttl:"+ttl);

        if(Gmc.passedTime(ttl)){
//            Gmc.toggleVisibility(true,pb_loader);
            if(checkToken!= null) {
                Gmc.toggleVisibility(true, pb_loading);
                checkToken.getToken();
            }
            else {
                init();
            }
        }
        else{
            getRemoteData(cPage);
        }
    }

    private void init() {
        checkToken = new CheckToken(this);
        checkToken.setTokenCallBack(this);
    }

    private void setNavigationBar(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setListAdapter() {
        adapter = new RCV_News(this,dataList);
        rcv_news.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcv_news.setLayoutManager(linearLayoutManager);
        rcv_news.setItemAnimator(new DefaultItemAnimator());

        adapter.setClickListener(this);

        rcv_news.addOnScrollListener(new EndlessRecycler(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.v(TAG,"current_page:"+current_page);
                cPage = current_page;
                getRemoteData(current_page);
            }
        });
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rcv_news = (RecyclerView) findViewById(R.id.rcv_news);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        setFrescoCircle(headerView);
    }

    void setToolbar(){
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setFrescoCircle(View v){
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.mipmap.ic_okams).build();
        SimpleDraweeView draweeView = (SimpleDraweeView) v.findViewById(R.id.my_image_view);
        int color = getResources().getColor(R.color.white);
        RoundingParams roundingParams = RoundingParams.asCircle();
        roundingParams.setBorder(color, 3.0f);
        roundingParams.setRoundAsCircle(true);
        draweeView.getHierarchy().setRoundingParams(roundingParams);
        TextView txt_name = (TextView) v.findViewById(R.id.txt_name);
        txt_name.setText(getResources().getString(R.string.my_name));
        TextView txt_email = (TextView) v.findViewById(R.id.txt_email);
        txt_email.setText(getResources().getString(R.string.my_email));
        draweeView.setImageURI(imageRequest.getSourceUri());
    }

    void showNoConnection(){
        String failmsg = getResources().getString(R.string.no_connection);
        String retry = getString(R.string.retry);
        final String retring = getString(R.string.retring);
        Log.v(TAG, failmsg);

        Snackbar snackbar = Snackbar
                .make(drawer, failmsg, Snackbar.LENGTH_INDEFINITE)
                .setAction(retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(drawer, retring, Snackbar.LENGTH_SHORT);
                        snackbar1.show();
//                        getToken();
                    }
                });
        snackbar.show();
//        Gmc.toggleVisibility(false,pb_loader);
    }

    void showTokenError(String msg){
        Log.v(TAG, msg);
        String exit = getString(R.string.exit);
        Snackbar snackbar = Snackbar
                .make(drawer, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(exit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.finish();
                    }
                });
        snackbar.show();
    }

    @Override
    public void checkTokenCallBack(int response, String msg) {
        switch (response){
            case Gmc.e_success:
                getRemoteData(cPage);
                break;
            case Gmc.e_no_success:
                showTokenError(msg);
                break;
            case Gmc.e_no_connection:
                showNoConnection();
                break;
            case Gmc.e_general_error:
                showTokenError(msg);
                break;
            default: Log.v(TAG,"Response not defined");
        }
//        Gmc.toggleVisibility(false,pb_loader);
    }

    @Override
    public void volleyGetCallBack(int sender, boolean error, String response) {
        if(!error){
            new ParsingNews().execute(response);
        }
    }

    @Override
    public void itemClick(View view, int position) {

        Gson gson = new Gson();
        String jsonData = gson.toJson(dataList);

        Intent intent = new Intent(this, ScrollingActivity.class);
        intent.putExtra(Gmc.p_list, jsonData);
        intent.putExtra(Gmc.p_position,position);
//        ActivityOptions transitionActivityOptions = null;
//        String transitionName = getResources().getString(R.string.shared_fab);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, fab , transitionName);
//            startActivity(intent, transitionActivityOptions.toBundle());
//        }
//        else{
            startActivity(intent);
//        }
    }

    private class ParsingNews extends AsyncTask<Object,Void,Void>{

        String body;
        ArrayList<Data> data = new ArrayList<>();

        @Override
        protected Void doInBackground(Object... objects) {
            body = (String) objects[0];
            data = Parser.parseDataResponse(body);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(data!=null){
                addToListView(data);
            }
            Gmc.toggleVisibility(false,pb_loading);
        }
    }

    void addToListView(ArrayList<Data> items){
        dataList.addAll(items);
        adapter.notifyDataSetChanged();
    }
}
