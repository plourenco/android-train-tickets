package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import feup.cm.traintickets.controllers.ServiceHandler;
import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.TokenModel;

public abstract class TokenRefreshTask extends AsyncTask<Void, Void, Boolean> {

    private final String refresh;
    private TokenModel token;

    public TokenRefreshTask(String refresh) {
        this.refresh = refresh;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        String res = userController.refresh(refresh);

        if(res != null) {
            try {
                JSONObject object = new JSONObject(res);
                if(object.getString("token") != null &&
                        object.getLong("expires") != 0L) {
                    token = new TokenModel(object.getString("token"), refresh,
                            object.getLong("expires"));
                    return true;
                }
            }
            catch(JSONException ignored) { }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected abstract void onCancelled();

    protected TokenModel getToken() {
        return token;
    }
}
