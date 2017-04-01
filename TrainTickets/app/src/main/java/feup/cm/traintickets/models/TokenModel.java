package feup.cm.traintickets.models;

import java.util.Date;

public class TokenModel {

    private String token;
    private String refresh;
    private Date expires;

    public TokenModel(String token, String refresh, long expires) {
        this.token = token;
        this.refresh = refresh;
        this.expires = new Date(expires);
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
}
