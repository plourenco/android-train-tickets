package com.example.models;

import java.util.Date;

public class RawTokenModel {

    private String token;
    private String refresh;
    private Date expires;
    private int role;
    private int userId;

    public RawTokenModel() {

    }

    public RawTokenModel(String token, String refresh, Date expires, int userId, int role) {
        this.token = token;
        this.refresh = refresh;
        this.expires = expires;
        this.userId = userId;
        this.role = role;
    }

    public RawTokenModel(String token, String refresh, Date expires) {
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

    public int getRole() { return role; }
}
