package feup.cm.traintickets.controllers;

/**
 * Created by mercurius on 11/04/17.
 */

public class TripController {
    public String getTrips(String token) {
        return ServiceHandler.makeGet("trip/trips", token);
    }

    public String getDirections(String token) {
        return ServiceHandler.makeGet("trip/direction", token);
    }
}
