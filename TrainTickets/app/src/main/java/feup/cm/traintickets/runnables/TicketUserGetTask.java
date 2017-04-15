package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

public abstract class TicketUserGetTask extends AsyncTask<Void, Void, Boolean> {

    private int userId;
    private String token;

    protected List<TicketModel> tickets;

    public TicketUserGetTask(String token, int userId) {
        this.userId = userId;
        this.token = token;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TicketController ticketController = new TicketController();
        String res = ticketController.getUserTickets(token, userId);

        if (res != null) {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.util.Date.class, new DateDeserializer())
                        .registerTypeAdapter(Time.class, new TimeDeserializer()).create();
                Type type = new TypeToken<ArrayList<TicketModel>>() {}.getType();
                this.tickets = gson.fromJson(res, type);
                return true;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected void onCancelled() {

    }

    protected List<TicketModel> getTickets() {
        return tickets != null ? tickets : new ArrayList<TicketModel>();
    }
}
