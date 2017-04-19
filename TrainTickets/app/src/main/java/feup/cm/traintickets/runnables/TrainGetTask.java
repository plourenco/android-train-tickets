package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TrainController;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.models.SeatModel;
import feup.cm.traintickets.models.TrainModel;

/**
 * Created by mercurius on 11/04/17.
 */

public abstract class TrainGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    private List<TrainModel> trains;

    public TrainGetTask(String token) {
        this.token = token;
        this.trains = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<TrainModel> cached = TrainDataManager.getTrains();
        if(cached != null && !cached.isEmpty()) {
            this.trains = cached;
            return true;
        }
        TrainController trainController = new TrainController();
        String res = trainController.getTrains(token);

        if(res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int id = obj.getInt("id");
                    int maxCap = obj.getInt("maxCapacity");
                    String desc = obj.getString("description");
                    JSONArray seats = obj.getJSONArray("seats");
                    List<SeatModel> seatsList = new ArrayList<>();
                    for (int j = 0; j < seats.length(); j++) {
                        JSONObject obj2 = seats.getJSONObject(j);
                        seatsList.add(new SeatModel(
                                obj2.getInt("id"),
                                obj2.getString("seatNumber"),
                                obj2.getInt("trainId")
                        ));
                    }

                    trains.add(new TrainModel(id, maxCap, desc, seatsList));
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
    protected void onCancelled() {

    }

    protected List<TrainModel> getTrains() {
        return trains;
    }
}
