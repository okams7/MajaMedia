package com.okams.majamedia.models;

/**
 * Created by okams on 7/7/17.
 */

public class Countries {
    private String country_name;
    private String country_id;

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getCountry_name() {
        return country_name;
    }
}
