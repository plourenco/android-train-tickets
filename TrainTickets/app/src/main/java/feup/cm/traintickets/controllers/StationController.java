package feup.cm.traintickets.controllers;

public class StationController {

    public String getStations(String token) {
        return ServiceHandler.makeGet("station/stations", token);
    }
}
