package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.StationController;
import feup.cm.traintickets.models.StationModel;

public abstract class StationGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;

    protected List<StationModel> stations;

    public StationGetTask(String token) {
        this.token = token;
        this.stations = new ArrayList<StationModel>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        StationController stationController = new StationController();
        String res = stationController.getStations(token);

        if(res != null) {
            try {
                JSONArray array = new JSONArray(res);
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    stations.add(new StationModel(obj.getInt("id"),
                            obj.getInt("stationNumber"), obj.getString("stationName")));
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
    protected void onCancelled() {}

    protected List<StationModel> getStations() {
        return stations;
    }
}
