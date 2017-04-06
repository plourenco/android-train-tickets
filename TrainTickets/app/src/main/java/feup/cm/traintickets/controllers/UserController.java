package feup.cm.traintickets.controllers;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import feup.cm.traintickets.models.UserModel;

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

    public String createUser(UserModel user) {
        Gson gson = new Gson();
        return ServiceHandler.makePost("users/register", gson.toJson(user));
    }
}
