package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import feup.cm.traintickets.models.CreditCardModel;

public abstract class PaymentConfirmTask extends AsyncTask<Void, Void, Boolean> {

    private CreditCardModel model;

    protected PaymentConfirmTask(CreditCardModel model) {
        this.model = model;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    @Override
    protected Boolean doInBackground(Void... params) {
        return true;
    }
}
