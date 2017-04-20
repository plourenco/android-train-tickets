package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.CreditCardModel;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

public abstract class TicketBuyTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private int userId;
    private StationModel departure;
    private StationModel arrival;
    private Date ticketDate;
    private double price;
    private int trip;

    private TicketModel result;

    public TicketBuyTask(String token, int userId, StationModel departure, StationModel arrival,
                         Date ticketDate, double price, int trip) {
        this.token = token;
        this.userId = userId;
        this.departure = departure;
        this.arrival = arrival;
        this.ticketDate = ticketDate;
        this.price = price;
        this.trip = trip;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    @Override
    protected Boolean doInBackground(Void... params) {
        TicketController ticketController = new TicketController();
        String res = ticketController.payment(token, userId, departure, arrival, ticketDate,
                price, trip);

        if (res != null) {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.util.Date.class, new DateDeserializer())
                        .registerTypeAdapter(Time.class, new TimeDeserializer()).create();
                this.result = gson.fromJson(res, TicketModel.class);
                return true;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    protected TicketModel getResult() {
        return result;
    }
}
