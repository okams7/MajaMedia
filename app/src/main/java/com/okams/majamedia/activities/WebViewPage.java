package com.okams.majamedia.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.okams.majamedia.R;
import com.okams.majamedia.methods.Gmc;

public class WebViewPage extends AppCompatActivity {

    String TAG = Gmc.TagApp+getClass().getSimpleName();
    WebView webview;
    String passedURL;
    boolean isRedirected;
    ProgressBar pb_loader;
    int sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);
        setToolBar();
        getPassedParameters();
        setViews();
        setWebSettings();
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
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private void getPassedParameters() {
        passedURL = getIntent().getStringExtra(Gmc.p_Link);
    }

    void setToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void setWebSettings(){
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pb_loader.setProgress(progress);
                if(progress == 100) {
                    Gmc.toggleVisibility(false,pb_loader);
                }
            }
        });
        Gmc.toggleVisibility(true,pb_loader);
        webview.loadUrl(passedURL);
    }

    void setViews(){
        webview = (WebView) findViewById(R.id.webview);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);

        Gmc.toggleVisibility(false,pb_loader);
    }
}
