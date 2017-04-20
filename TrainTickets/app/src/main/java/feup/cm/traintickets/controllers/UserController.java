package feup.cm.traintickets.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import feup.cm.traintickets.models.CreditCardModel;
import feup.cm.traintickets.models.TokenModel;
import feup.cm.traintickets.models.UserModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

public class UserController {

    public String authenticate(String email, String password) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", password);
        return ServiceHandler.makePost("users/auth", gson.toJson(map));
    }

    public String refresh(String refreshToken) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", refreshToken);
        return ServiceHandler.makePost("users/refresh", gson.toJson(map));
    }

    public boolean check(TokenModel token) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return ServiceHandler.makePostCheck("users/check", gson.toJson(token), null);
    }

    public String createUser(UserModel user) {
        Gson gson = new Gson();
        return ServiceHandler.makePost("users/register", gson.toJson(user));
    }

    public String getRole(int id) {
        return ServiceHandler.makeGet("users/role/" + id);
    }

    public String saveCreditCard(CreditCardModel creditCard, String token, int userId) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return ServiceHandler.makePost("users/cc/" + userId, gson.toJson(creditCard), token);
    }
}
