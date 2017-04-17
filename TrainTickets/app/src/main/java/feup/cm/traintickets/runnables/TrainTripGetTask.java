package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TrainListController;
import feup.cm.traintickets.models.TrainTripModel;
import feup.cm.traintickets.util.TimeDeserializer;

public abstract class TrainTripGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private int depStation;
    private int arrStation;

    protected List<TrainTripModel> traintrips;

    public TrainTripGetTask(String token, int depStation,int arrStation) {
        this.token = token;
        this.depStation=depStation;
        this.arrStation=arrStation;
        this.traintrips=new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TrainListController trainList = new TrainListController();
        String res = trainList.getTripList(token,depStation,arrStation);

        if (res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    int id = obj.getInt("idTrip");
                    String description=obj.getString("description");
                    //String description = obj.getString("description");
                    Time depTime = Time.valueOf(obj.getString("departureTime"));
                    Time arrTime = Time.valueOf(obj.getString("arrivalTime"));
                    //int duration = obj.getInt("duration");
                    int duration=120;

                    traintrips.add(new TrainTripModel(id, description, depTime, arrTime, duration));
                }
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
    protected void onCancelled() {

    }

    public List<TrainTripModel> getTraintrips() {

        return traintrips !=null ? traintrips:new ArrayList<TrainTripModel>();
    }
}

