package feup.cm.traintickets.controllers;

import org.json.JSONObject;

public class UserController {

    public JSONObject getUserByName(String username) {
        return ServiceHandler.makeGet("users/" + username);
    }
}
