package com.okams.majamedia.methods;

import com.okams.majamedia.models.Countries;
import com.okams.majamedia.models.Data;
import com.okams.majamedia.models.Tags;
import com.okams.majamedia.models.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by okams on 7/6/17.
 */

public class Parser {

    public static Token parseTokenResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Token token = new Token();
            if(jsonObject.has("success")){
                boolean success = jsonObject.getBoolean("success");
                token.setSuccess(success);
                if(success){
                    token.setToken(jsonObject.getLong("token"));
                    token.setTtl(jsonObject.getLong("ttl"));
                }else{
                    if(jsonObject.has("msg")){
                        token.setMsg(jsonObject.getString("msg"));
                    }
                }
            }
            else return null;
            return token;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Data> parseDataResponse(String response){
        ArrayList<Data> dataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("success")){
                boolean success = jsonObject.getBoolean("success");
                if(success){
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsData = jsonArray.getJSONObject(i);
                        Data data = new Data();
                        data.setNid(jsData.getString("nid"));
                        data.setNode_type(jsData.getString("node_type"));
                        data.setTitle(jsData.getString("title"));
                        data.setAuthor_id(jsData.getString("author_id"));
                        data.setAuthor_img(jsData.getString("author_img"));
                        data.setFullurl(jsData.getString("fullurl"));
                        data.setDetails(jsData.getString("details"));
                        data.setComment_count(jsData.getString("comment_count"));
                        data.setCreated_date(jsData.getString("created_date"));
                        data.setView_count(jsData.getString("view_count"));
                        data.setMain_img(jsData.getString("main_img"));
                        data.setSection_name(jsData.getString("section_name"));
                        data.setSection_id(jsData.getString("section_id"));

                        if(jsData.has("tags")) {
                            if(!jsData.getString("tags").equals("null")) {
                                JSONArray jsArrayTags = jsData.getJSONArray("tags");
                                ArrayList<Tags> tagsList = new ArrayList<>();
                                for (int j = 0; j < jsArrayTags.length(); j++) {
                                    JSONObject jsObjectTags = jsArrayTags.getJSONObject(j);
                                    Tags tag = new Tags();
                                    tag.setTag_id(jsObjectTags.getString("tag_id"));
                                    tag.setTag_name(jsObjectTags.getString("tag_name"));
                                    tagsList.add(tag);
                                }
                                data.setTags(tagsList);
                            }
                        }

                        if(jsData.has("countries")) {
                            if(!jsData.getString("countries").equals("null")) {
                                //Value is not null
                                JSONArray jsArrayCountries = jsData.getJSONArray("countries");
                                ArrayList<Countries> countriesList = new ArrayList<>();
                                for (int j = 0; j < jsArrayCountries.length(); j++) {
                                    JSONObject jsObjectCountries = jsArrayCountries.getJSONObject(j);
                                    Countries country = new Countries();
                                    country.setCountry_id(jsObjectCountries.getString("country_id"));
                                    country.setCountry_name(jsObjectCountries.getString("country_name"));
                                    countriesList.add(country);
                                }
                                data.setCountries(countriesList);
                            }
                        }

                        data.setIs_followed(jsData.getBoolean("is_followed"));
                        data.setPage_name(jsData.getString("page_name"));
                        data.setPage_id(jsData.getString("page_id"));
                        data.setPage_details(jsData.getString("page_details"));
                        data.setPage_type(jsData.getString("page_type"));
                        data.setPage_logo(jsData.getString("page_logo"));

                        dataList.add(data);

                    }
                    return dataList;
                }
                return null;
            }
            return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
