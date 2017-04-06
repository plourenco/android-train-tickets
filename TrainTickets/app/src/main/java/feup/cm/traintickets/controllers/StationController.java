package feup.cm.traintickets.controllers;

import org.json.JSONObject;

public class StationController {

    public JSONObject getStations() {
        return ServiceHandler.makeGet("station/stations");
    }
}
