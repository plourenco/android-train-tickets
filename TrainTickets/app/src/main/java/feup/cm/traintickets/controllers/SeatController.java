package feup.cm.traintickets.controllers;

/**
 * Created by mercurius on 11/04/17.
 */

public class SeatController {
    public String getSeats(String token) {
        return ServiceHandler.makeGet("seat/seats", token);
    }
}
