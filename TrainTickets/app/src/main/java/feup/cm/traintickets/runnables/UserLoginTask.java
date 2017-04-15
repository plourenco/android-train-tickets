package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.TokenModel;

public abstract class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;

    protected TokenModel token;

    public UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        String res = userController.authenticate(mEmail, mPassword);

        if(res != null) {
            try {
                JSONObject object = new JSONObject(res);
                if(object.getString("token") != null &&
                        object.getLong("expires") != 0L) {
                    token = new TokenModel(object.getInt("userId"), object.getString("token"),
                            object.getString("refresh"),
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
    protected void onCancelled() {

    }
}