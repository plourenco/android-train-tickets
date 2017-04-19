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

import feup.cm.traintickets.controllers.StepController;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.util.DateDeserializer;
import feup.cm.traintickets.util.TimeDeserializer;

/**
 * Created by mercurius on 11/04/17.
 */

public abstract class StepGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    private List<StepModel> steps;

    public StepGetTask(String token) {
        this.token = token;
        this.steps = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<StepModel> cached = StepDataManager.getSteps();
        if(cached != null && !cached.isEmpty()) {
            this.steps = cached;
            return true;
        }
        StepController stepController = new StepController();
        String res = stepController.getSteps(token);

        if(res != null) {
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(java.util.Date.class, new DateDeserializer())
                        .registerTypeAdapter(Time.class, new TimeDeserializer()).create();
                Type type = new TypeToken<ArrayList<StepModel>>() {}.getType();
                this.steps = gson.fromJson(res, type);
                return true;
            }
            catch (JsonParseException | NullPointerException ignored) {
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

    public List<StepModel> getSteps() { return steps; }
}

