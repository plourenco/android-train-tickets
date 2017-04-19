package feup.cm.traintickets.models;

import java.util.Date;

public class TokenModel {

    private int userId;
    private int role;
    private String token;
    private String refresh;
    private Date expires;

    public TokenModel(int userId, String token, String refresh, long expires, int role) {
        this.userId = userId;
        this.token = token;
        this.refresh = refresh;
        this.expires = new Date(expires);
        this.role = role;
    }

    public TokenModel(int userId, String token, String refresh, long expires) {
        this.userId = userId;
        this.token = token;
        this.refresh = refresh;
        this.expires = new Date(expires);
    }

    public TokenModel(int userId, String token, String refresh, Date expires) {
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public int getRole() {
        return role;
    }
}
