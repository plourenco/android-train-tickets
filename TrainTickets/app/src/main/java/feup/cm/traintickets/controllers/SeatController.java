package feup.cm.traintickets.controllers;

public class SeatController {
    public String getSeats(String token) {
        return ServiceHandler.makeGet("seat/seats", token);
    }
}
