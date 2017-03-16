package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import feup.cm.traintickets.R;
import feup.cm.traintickets.LoginActivity;
import feup.cm.traintickets.controllers.UserController;

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private LoginActivity activity;
    private final String mEmail;
    private final String mPassword;

    public UserLoginTask(LoginActivity activity, String email, String password) {
        mEmail = email;
        mPassword = password;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        JSONObject object = userController.getUserByEmail(mEmail);

        if(object != null) {
            try {
                return object.getString("password").equals(mPassword);
            } catch (JSONException ignored) { }
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        activity.nullifyLoginTask();
        activity.showProgress(false);

        if (success) {
            activity.finish();
        } else {
            activity.getPasswordView().setError(activity.getString(R.string.error_incorrect_password));
            activity.getPasswordView().requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        activity.nullifyLoginTask();
        activity.showProgress(false);
    }
}