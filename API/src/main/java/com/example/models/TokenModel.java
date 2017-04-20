package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenModel {

    private String token;
    private String refresh;
    private Date expires;
    private int userId;

    public TokenModel() {

    }

    public TokenModel(String token, String refresh, Date expires, int userId) {
        this.token = token;
        this.refresh = refresh;
        this.expires = expires;
        this.userId = userId;
    }

    public TokenModel(String token, String refresh, Date expires) {
        this.token = token;
        this.refresh = refresh;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public String getRefresh() { return refresh; }

    public Date getExpires() {
        return expires;
    }

    public int getUserId() { return userId; }
}
