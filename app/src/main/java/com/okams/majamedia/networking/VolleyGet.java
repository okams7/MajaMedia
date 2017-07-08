package com.okams.majamedia.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.okams.majamedia.app.AppController;
import com.okams.majamedia.methods.Gmc;

/**
 * Created by okams on 5/24/16.
 */
public class VolleyGet {

    Context context;
    String TAG = Gmc.TagApp+"VGet";
    VolleyGetCallBack volleyListener;
    public static String TYPE_UTF8_CHARSET = "charset=UTF-8";
    public static final String CONTENT_TYPE = "Content-Type";

    public VolleyGet(Context context, int sender , String url){
        Log.v(TAG,"sender:"+sender+" url:"+url);
        this.context = context;
        Volley_Read_URL(sender,url);
    }

    public void setVolleyGetCallBack(VolleyGetCallBack listener)
    {
        this.volleyListener = listener;
    }


    public void Volley_Read_URL(final int sender, String url) {
        // Tag used to cancel the request
        String tag_string_req = "get_"+sender;
        AppController.getInstance().getRequestQueue().getCache().invalidate(url, true);

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.v(TAG + "StringRequest:", response);
//                    new UpdateString().execute(sender,response);
                    ResponseUpdate(sender, response);
                } catch (Exception ex) {
//		             Log.d("StringRequest error", ex.getMessage().toString());
                    failed(sender, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("StringRequest", "Error: " + error.getMessage());
                failed(sender, error.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String type = response.headers.get(CONTENT_TYPE);
                    if (type == null) {
                        Log.d("parseNetworkResponse", "content type was null");

                        response.headers.put(CONTENT_TYPE, type);
                    } else if (!type.contains("UTF-8")) {
                        Log.d("parseNetworkResponse", "content type had UTF-8 missing");
                        type += ";" + TYPE_UTF8_CHARSET;
                        response.headers.put(CONTENT_TYPE, type);
                    }
                } catch (Exception e) {
                    //print stacktrace e.g.
                }
                return super.parseNetworkResponse(response);
            }


//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(
//                        "Authorization", "");
//                return params;
//            }

        };

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void failed(int sender, String error) {
        volleyListener.volleyGetCallBack(sender,true,error);
    }

    private void ResponseUpdate(int sender,String response) {
        volleyListener.volleyGetCallBack(sender,false,response);
    }


    public interface VolleyGetCallBack{
        void volleyGetCallBack(int sender, boolean error, String response);
    }
}
