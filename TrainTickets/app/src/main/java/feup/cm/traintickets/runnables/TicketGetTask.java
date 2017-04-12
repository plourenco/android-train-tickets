package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.TicketModel;

public abstract class TicketGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private int tripId;
    private int depStationId;
    private int arrStationId;
    private double price;

    protected TicketModel ticket;

    public TicketGetTask(String token, int tripId, int depStationId, int arrStationId) {
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
                JSONObject obj = new JSONObject(res);
                price = obj.getDouble("price");
                return true;
            } catch (JSONException | NullPointerException ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    protected double getPrice() {
        return price;
    }

}
