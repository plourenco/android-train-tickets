package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

public abstract class StationGetTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... params) {

    }

    @Override
    protected abstract void onPostExecute(final Boolean success);

    @Override
    protected abstract void onCancelled();
}
