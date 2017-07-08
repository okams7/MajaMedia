package com.okams.majamedia.models;

/**
 * Created by okams on 7/6/17.
 */

public class Token {

    private boolean success;
    private long token;
    private long ttl;
    private String msg;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getToken() {
        return token;
    }

    public long getTtl() {
        return ttl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
