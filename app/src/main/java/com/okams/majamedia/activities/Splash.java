package com.okams.majamedia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.okams.majamedia.R;
import com.okams.majamedia.methods.CheckToken;
import com.okams.majamedia.methods.Gmc;

public class Splash extends AppCompatActivity implements CheckToken.TokenCallBack {

    private String TAG = Gmc.TagApp+getClass().getSimpleName();
    ConstraintLayout constraintLayout;
    ProgressBar pb_loader;
    SharedPreferences sPref;
    CheckToken checkToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        setViews();
        checkForToken();
    }

    private void checkForToken() {
        long ttl = sPref.getLong(Gmc.sp_Ttl,0);
        Log.v(TAG,"ttl:"+ttl);

        if(Gmc.passedTime(ttl)){
            Gmc.toggleVisibility(true,pb_loader);
            if(checkToken!= null)
                checkToken.getToken();
            else
                init();
        }
        else{
            gotoMainActivity();
        }
    }

    private void init() {
        sPref = getSharedPreferences(Gmc.AppName,0);
        checkToken = new CheckToken(this);
        checkToken.setTokenCallBack(this);
    }

    void setViews(){
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        pb_loader = (ProgressBar) findViewById(R.id.pb_loader);
        Gmc.toggleVisibility(false,pb_loader);
    }

    void showNoConnection(){
        String failmsg = getResources().getString(R.string.no_connection);
        String retry = getString(R.string.retry);
        final String retring = getString(R.string.retring);
        Log.v(TAG, failmsg);

        Snackbar snackbar = Snackbar
                .make(constraintLayout, failmsg, Snackbar.LENGTH_INDEFINITE)
                .setAction(retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(constraintLayout, retring, Snackbar.LENGTH_SHORT);
                        snackbar1.show();
//                        getToken();
                    }
                });
        snackbar.show();
        Gmc.toggleVisibility(false,pb_loader);
    }



    void gotoMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    void showTokenError(String msg){
        Log.v(TAG, msg);
        String exit = getString(R.string.exit);
        Snackbar snackbar = Snackbar
                .make(constraintLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(exit, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Splash.this.finish();
                    }
                });
        snackbar.show();
    }

    @Override
    public void checkTokenCallBack(int response, String msg) {
        switch (response){
            case Gmc.e_success:
                gotoMainActivity();
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
        Gmc.toggleVisibility(false,pb_loader);
    }
}
