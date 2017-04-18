package feup.cm.traintickets.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

import feup.cm.traintickets.models.CreditCardModel;
import feup.cm.traintickets.models.TicketModel;

public class TicketController {

    public String payment(CreditCardModel model) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();
        return ServiceHandler.makeGet("payment/buy-ticket", gson.toJson(model));
    }

    public String getUserTickets(String token, int userid) {
        return ServiceHandler.makeGet("tickets/user-tickets/" + userid, token);
    }

    public String getPrice(String token, int tripId, int depStationId, int arrStationId) {
        return ServiceHandler.makeGet("tickets/gen-ticket-price/" + tripId
                + "/" + depStationId + "/" + arrStationId, token);
    }

    public String syncWithServer(List<TicketModel> tickets, String token) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();
        return ServiceHandler.makePost("tickets/sync", gson.toJson(tickets), token);
    }

    public String downloadTickets(String direction, String trip, Date date, String token) {
        return ServiceHandler.makeGet("tickets/download/" + direction + "/" + trip + "/" + date.toString(), token);
    }
}
