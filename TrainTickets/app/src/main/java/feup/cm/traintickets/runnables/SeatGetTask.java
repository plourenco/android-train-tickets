package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.SeatController;
import feup.cm.traintickets.models.SeatModel;

/**
 * Created by mercurius on 11/04/17.
 */

public abstract class SeatGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    List<SeatModel> seats;

    public SeatGetTask(String token) {
        this.token = token;
        this.seats = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        SeatController seatController = new SeatController();
        String res = seatController.getSeats(token);

        if(res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    seats.add(new SeatModel(
                            obj.getInt("id"),
                            obj.getString("seatNumber"),
                            obj.getInt("trainId")));
                }
                return true;
            }
            catch (JSONException | NullPointerException ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected abstract void onCancelled();

    public List<SeatModel> getSeats() {
        return seats;
    }
}
