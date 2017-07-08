package com.okams.majamedia.methods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by okams on 7/6/17.
 */

public class Gmc {
    public static String TagApp = "MM:";

//    http://lowcost-env.pmtiunacvu.us-east-1.elasticbeanstalk.com
    public static String MainAPI = "http://lowcost-env.pmtiunacvu.us-east-1.elasticbeanstalk.com";
    public static String app_key = "demo";
    public static String app_secret = "12345678";
    public static String AppName = "MajalMedia";
    public static String sp_Token = "token";
    public static String sp_Ttl = "ttl";
    private static String TAG = TagApp+"Gmc";

    public static final int e_success = 0;
    public static final int e_no_success = 1;
    public static final int e_no_connection = 2;
    public static final int e_general_error = 3;
    public static String p_list = "list";
    public static String p_position = "position";
    public static String p_data = "data";
    public static String p_Link = "link";

    public static boolean isNetworkAvailable(Context con) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void toggleVisibility(boolean show, View... views) {
        if (show) {
            for (View current : views) {
                current.setVisibility(View.VISIBLE);
            }
        } else {
            for (View current : views) {
                current.setVisibility(View.GONE);
            }
        }
    }

    public static boolean passedTime(long ttl){
        long now = System.currentTimeMillis();
        Log.v(TAG,"now:"+now+" ttl:"+ttl);
        return now > ttl;
    }

    public static long getTimeToPass(long ttl){
        int seconds = (int) (ttl / 1000) % 60 ;
        int minutes = (int) ((ttl / (1000*60)) % 60);
        int hours   = (int) ((ttl / (1000*60*60)) % 24);
        Log.v(TAG,"seconds:"+seconds+" minutes:"+minutes+" hours:"+hours);
        return ttl;
    }

    public static long getDateLong(String dateString){
//        الإثنين 2016.9.5 - 16:36 مساء بتوقيت ابوظبي
        try {
            DateFormat formatArabic = new SimpleDateFormat("zzzz HH:mm aa - yyyy.M.d EEEE", new Locale("ar", "AE"));
            Date date = formatArabic.parse(dateString);

            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
