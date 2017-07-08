package com.okams.majamedia.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.okams.majamedia.app.AppController;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.methods.Parser;
import com.okams.majamedia.models.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by okams on 6/4/2016.
 */
public class VolleyPost {
    Context context;
    String TAG = Gmc.TagApp+"VolleyPostMessage";
    VolleyPostCallBack volleyListener;
    Map<String, String> data_params;
    Map<String, String> data_headers;
    int sender;

    public VolleyPost(Context context, int sender,Map<String, String> data_params,
                      String url){
        this.context = context;
        this.data_params = data_params;
        this.sender = sender;
        send_Json_Post(data_params,null,url);
    }

    public VolleyPost(Context context, int sender,Map<String, String> data_params,
                      Map<String, String> data_headers,String url){
        this.context = context;
        this.data_params = data_params;
        this.data_headers = data_headers;
        this.sender = sender;
        send_Json_Post(data_params,data_headers,url);
    }

    public void setVolleyPostCallBack(VolleyPostCallBack listener)
    {
        this.volleyListener = listener;
    }


    void send_Json_Post(final Map<String, String> data_params,
                        final Map<String, String> data_headers, String url)
    {
        String tag_string_req = "post_"+sender;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        UpdateResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            Log.e(TAG,"jsonError:"+jsonError);
                            Token errorToken = Parser.parseTokenResponse(jsonError);
                            if(errorToken!=null) {
                                failed(errorToken.getMsg());
                            }
                            else{
                                failed(error.toString());
                            }
                        }
                        else{
                            Log.e(TAG,"jsonError:nnn");
                            failed(error.toString());
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params = data_params;
                printContent(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                if(data_headers!=null){
                    params = data_headers;
                    printContent(params);
                }
//                params.put("Authorization", "Nintendo Gameboy");
//                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }
        };

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        postRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequest, tag_string_req);
    }

    void printContent(Map<String, String> map){
        String key;
        String value;
        for (Map.Entry<String,String> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            Log.v(TAG,"key:"+key+" value:"+value);
        }
    }

    private void failed(String errorString) {
        volleyListener.volleyPostCallBack(sender,true,errorString);
    }

    private void UpdateResponse(String response) {
        volleyListener.volleyPostCallBack(sender,false,response);
    }


    public interface VolleyPostCallBack{
        void volleyPostCallBack(int sender, boolean error, String response);
    }
}
