package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TrainController;
import feup.cm.traintickets.controllers.TripController;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TrainModel;
import feup.cm.traintickets.models.TripModel;

/**
 * Created by mercurius on 11/04/17.
 */

public abstract class TripGetTask extends AsyncTask<Void, Void, Boolean> {
    private String token;

    protected List<TripModel> trips;

    public TripGetTask(String token) {
        this.token = token;
        this.trips = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TripController tripController = new TripController();
        String res = tripController.getTrips(token);

        if(res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("id");
                    String desc = obj.getString("description");
                    String direction = obj.getString("direction");
                    String increment = obj.getString("increment");
                    JSONObject train = obj.getJSONObject("train");
                    int idTrain = train.getInt("id");

                    TrainModel trainModel = null;

                    for (int j = 0; j < TrainDataManager.getTrains().size(); j++) {
                        if (TrainDataManager.getTrains().get(j).getId() == idTrain)
                            trainModel = TrainDataManager.getTrains().get(j);
                    }

                    trips.add(new TripModel(id, desc, direction, increment, trainModel, null));
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

    protected List<TripModel> getTrips() {
        return trips;
    }
}
