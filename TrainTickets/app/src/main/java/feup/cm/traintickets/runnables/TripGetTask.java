package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TrainController;
import feup.cm.traintickets.controllers.TripController;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TrainModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

public abstract class TripGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    private List<TripModel> trips;

    public TripGetTask(String token) {
        this.token = token;
        this.trips = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<TripModel> cached = TripDataManager.getTrips();
        if(cached != null && !cached.isEmpty()) {
            this.trips = cached;
            return true;
        }
        TripController tripController = new TripController();
        String res = tripController.getTrips(token);

        if(res != null) {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.util.Date.class, new DateDeserializer())
                        .registerTypeAdapter(Time.class, new TimeDeserializer()).create();
                Type type = new TypeToken<ArrayList<TripModel>>() {}.getType();
                this.trips = gson.fromJson(res, type);
                return true;
            }
            catch (JsonParseException | NullPointerException ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected void onCancelled() {

    }

    protected List<TripModel> getTrips() {
        return trips;
    }
}
