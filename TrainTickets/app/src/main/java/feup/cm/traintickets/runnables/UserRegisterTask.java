package feup.cm.traintickets.runnables;

import android.os.AsyncTask;
import android.util.Log;

import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.UserModel;

public abstract class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

    private final UserModel user;

    public UserRegisterTask(UserModel user) {
        this.user = user;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        String res = userController.createUser(user);
        Log.d("hello", "InBackground");

        if(res != null) {
            try {
                return Integer.parseInt(res) != -1;
            }
            catch(NumberFormatException ignored) {
                ignored.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected abstract void onCancelled();
}