package com.okams.majamedia.methods;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.okams.majamedia.R;
import com.okams.majamedia.models.Token;
import com.okams.majamedia.networking.VolleyPost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by okams on 7/6/17.
 */

public class CheckToken implements VolleyPost.VolleyPostCallBack {

    private Context context;
    private String TAG = Gmc.TagApp+"CheckToken";
    private SharedPreferences sPref;
    private int i_token = 0;
    private TokenCallBack callBack;

    public CheckToken(Context context){
        this.context = context;
        sPref = context.getSharedPreferences(Gmc.AppName,0);
    }

    public void setTokenCallBack(TokenCallBack callBack){
        this.callBack = callBack;
    }

    public void getToken(){
        if(Gmc.isNetworkAvailable(context)){
            requestTokenAPI();
        }
        else{
//            showNoConnection();
            callBack.checkTokenCallBack(Gmc.e_no_connection,null);
        }
    }

    private void requestTokenAPI(){
        //send token to your server or what you want to do
        String url = Gmc.MainAPI;
        Log.v(TAG,"url:"+url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("app_key",Gmc.app_key);
        params.put("app_secret", Gmc.app_secret);

        VolleyPost volleyPost = new VolleyPost(context,i_token,params,url);
        volleyPost.setVolleyPostCallBack(this);
    }

    @Override
    public void volleyPostCallBack(int sender, boolean error, String response) {
        if(!error){
            Token token = Parser.parseTokenResponse(response);
            if(token!=null){
                if(token.isSuccess()){
                    saveTokenInfo(token);
                    callBack.checkTokenCallBack(Gmc.e_success,null);
                }
                else{
//                    showTokenError(token.getMsg());
                    callBack.checkTokenCallBack(Gmc.e_no_success,token.getMsg());
                }
            }
            else{
                callBack.checkTokenCallBack(Gmc.e_general_error,context.getResources().getString(R.string.error_token));
//                showTokenError(getResources().getString(R.string.error_token));
            }
        }
        else{
//            showTokenError(response);
            callBack.checkTokenCallBack(Gmc.e_general_error,response);
        }
//
//        Gmc.toggleVisibility(false,pb_loader);
    }

    private void saveTokenInfo(Token token) {
        Gmc.getTimeToPass(token.getTtl());
        long now = System.currentTimeMillis();
        long timeToDie = token.getTtl()+now;
        sPref.edit().putLong(Gmc.sp_Token,token.getToken()).apply();
        sPref.edit().putLong(Gmc.sp_Ttl,timeToDie).apply();
    }

    public interface TokenCallBack {
        void checkTokenCallBack(int response,String msg);
    }
}
