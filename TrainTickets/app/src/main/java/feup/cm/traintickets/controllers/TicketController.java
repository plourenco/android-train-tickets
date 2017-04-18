package feup.cm.traintickets.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import feup.cm.traintickets.models.CreditCardModel;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;

public class TicketController {

    public String payment(int userid, StationModel departure, StationModel arrival,
                          Date ticketDate, double price, int tripid) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd").create();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("departureStation", departure);
        model.put("arrivalStation", arrival);
        model.put("ticketDate", ticketDate);
        model.put("price", price);
        model.put("purchaseDate", new Date());
        model.put("trip", new TripModel(tripid));
        Log.d("json", gson.toJson(model));

        return ServiceHandler.makePost("tickets/buy-ticket/" + userid, gson.toJson(model));
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
