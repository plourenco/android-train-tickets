package feup.cm.traintickets.controllers;

import org.json.JSONObject;

public class UserController {

    public JSONObject getUserByEmail(String email) {
        return ServiceHandler.makeGet("users/" + email);
    }
}
