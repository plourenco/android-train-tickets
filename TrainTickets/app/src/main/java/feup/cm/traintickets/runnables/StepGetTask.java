package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.StepController;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.StepModel;

/**
 * Created by mercurius on 11/04/17.
 */

public abstract class StepGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    protected List<StepModel> steps;

    public StepGetTask(String token) {
        this.token = token;
        this.steps = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        StepController stepController = new StepController();
        String res = stepController.getSteps(token);

        if(res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("id");
                    int depStationId = obj.getInt("departureStationId");
                    int arrStationId = obj.getInt("arrivalStationId");
                    int stepNumber = obj.getInt("stepNumber");
                    int distance = obj.getInt("distance");
                    float price = (float) obj.get("price");
                    int waitingTime = obj.getInt("waitingTime");
                    int tripId = obj.getInt("fkTrip");
                    int duration = obj.getInt("duration");
                    Time depTime = Time.valueOf(obj.getString("departureTime"));
                    Time arrTime = Time.valueOf(obj.getString("arrivalTime"));

                    StationModel depStation = null;
                    StationModel arrStation = null;

                    for(int j = 0; j < StationDataManager.getStations().size(); j++) {
                        if (StationDataManager.getStations().get(j).getId() == depStationId) {
                            depStation = StationDataManager.getStations().get(j);
                        }

                        if (StationDataManager.getStations().get(j).getId() == arrStationId) {
                            arrStation = StationDataManager.getStations().get(j);
                        }
                    }

                    steps.add(new StepModel(id, stepNumber, distance, price, waitingTime, duration,
                            depTime, arrTime, depStation, arrStation));
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
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    public List<StepModel> getSteps() { return steps; }
}

