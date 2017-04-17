package feup.cm.traintickets.runnables;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import feup.cm.traintickets.controllers.UserController;

public abstract class UserGetRoleTask extends AsyncTask<Void, Void, Boolean> {

    private int id;
    private int role;

    public UserGetRoleTask(int id) {
        this.id = id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        UserController userController = new UserController();
        String res = userController.getRole(id);

        if (res != null && !res.isEmpty()){
            try {
                role = Integer.valueOf(res);
            } catch (NumberFormatException nfe) {
                Log.e("Parsing Role", nfe.getMessage());
                nfe.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected abstract void onCancelled();

    @Override
    protected abstract void onPostExecute(Boolean success);

    public int getRole() { return role; }

}
