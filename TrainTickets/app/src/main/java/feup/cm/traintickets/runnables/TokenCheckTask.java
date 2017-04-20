package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.TokenModel;

public abstract class TokenCheckTask extends AsyncTask<Void, Void, Boolean> {

    private final TokenModel token;

    public TokenCheckTask(TokenModel token) {
        this.token = token;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        boolean res = userController.check(token);
        return res;
    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected void onCancelled() {

    }
}
