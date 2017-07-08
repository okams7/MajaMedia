package com.okams.majamedia.models;

import java.util.ArrayList;

/**
 * Created by okams on 7/7/17.
 */

public class Data {

    private String nid;
    private String node_type;
    private String title;
    private String author_id;
    private String author_img;
    private String fullurl;
    private String details;
    private String comment_count;
    private String created_date;
    private String view_count;
    private String main_img;
    private String section_name;
    private String section_id;
    private ArrayList<Tags> tags;
    private ArrayList<Countries> countries;
    private boolean is_followed;
    private String page_name;
    private String page_id;
    private String page_details;
    private String page_type;
    private String page_logo;

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public void setCountries(ArrayList<Countries> countries) {
        this.countries = countries;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public void setMain_img(String main_img) {
        this.main_img = main_img;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public void setNode_type(String node_type) {
        this.node_type = node_type;
    }

    public void setPage_details(String page_details) {
        this.page_details = page_details;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public void setPage_logo(String page_logo) {
        this.page_logo = page_logo;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    public void setPage_type(String page_type) {
        this.page_type = page_type;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public ArrayList<Countries> getCountries() {
        return countries;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public String getComment_count() {
        return comment_count;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getDetails() {
        return details;
    }

    public String getFullurl() {
        return fullurl;
    }

    public String getMain_img() {
        return main_img;
    }

    public String getNid() {
        return nid;
    }

    public String getNode_type() {
        return node_type;
    }

    public String getPage_details() {
        return page_details;
    }

    public String getPage_id() {
        return page_id;
    }

    public String getPage_logo() {
        return page_logo;
    }

    public String getPage_name() {
        return page_name;
    }

    public String getPage_type() {
        return page_type;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getTitle() {
        return title;
    }

    public String getView_count() {
        return view_count;
    }

    public boolean is_followed() {
        return is_followed;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSection_id() {
        return section_id;
    }
}
