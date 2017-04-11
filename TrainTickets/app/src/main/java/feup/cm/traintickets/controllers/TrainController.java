package feup.cm.traintickets.controllers;

/**
 * Created by mercurius on 11/04/17.
 */

public class TrainController {
    public String getTrains(String token) {
        return ServiceHandler.makeGet("train/trains", token);
    }
}
