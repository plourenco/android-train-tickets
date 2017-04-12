package feup.cm.traintickets.controllers;

public class TicketController {
    public String getPrice(String token, int tripId, int depStationId, int arrStationId) {
        return ServiceHandler.makeGet("tickets/gen-ticket-price/" + tripId
                + "/" + depStationId + "/" + arrStationId, token);
    }
}
