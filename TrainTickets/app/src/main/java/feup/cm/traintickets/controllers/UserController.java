package feup.cm.traintickets.controllers;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import feup.cm.traintickets.models.UserModel;

public class UserController {

    public JSONObject getUserByEmail(String email) {
        return ServiceHandler.makeGet("users/" + email);
    }

    public String createUser(UserModel user) {
        Gson gson = new Gson();
        return ServiceHandler.makePost("users/register", gson.toJson(user));
    }
}
