package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import feup.cm.traintickets.controllers.TripController;

/**
 * Created by mercurius on 18/04/17.
 */

public abstract class DirectionGetTask extends AsyncTask<Void, Void, Boolean> {

    String token;
    List<String> directions;

    public DirectionGetTask(String token) {
        this.token = token;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    @Override
    protected Boolean doInBackground(Void... params) {
        TripController tripController = new TripController();
        String res = tripController.getDirections(token);

        if (res != null && !res.isEmpty()) {
            Gson gson = new Gson();
            directions = gson.fromJson(res, List.class);
            return true;
        }
        return null;
    }

    public List<String> getDirections() {
        return directions;
    }
}
