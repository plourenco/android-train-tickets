package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

public abstract class TicketGetPriceTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private int tripId;
    private int depStationId;
    private int arrStationId;

    private TicketModel result;

    public TicketGetPriceTask(String token, int tripId, int depStationId, int arrStationId) {
        this.token = token;
        this.tripId = tripId;
        this.depStationId = depStationId;
        this.arrStationId = arrStationId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TicketController ticketController = new TicketController();
        String res = ticketController.getPrice(token, tripId, depStationId, arrStationId);

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

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected void onCancelled() {

    }

    protected TicketModel getResult() { return result; }
}
