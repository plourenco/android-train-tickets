package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.CreditCardModel;

/**
 * Created by mercurius on 17/04/17.
 */

public abstract class CreditCardSaveTask extends AsyncTask<Void, Void, Boolean> {

    private int userId;
    private CreditCardModel creditCard;
    private String token;
    private String errorString = "default error";
    private String rows = "0";

    protected CreditCardSaveTask(CreditCardModel creditCard, String token, int userId) {
        this.creditCard = creditCard;
        this.token = token;
        this.userId = userId;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();

    @Override
    protected Boolean doInBackground(Void... params) {

        if (creditCard.getCcNumber().isEmpty() || creditCard.getCvv2().isEmpty()
                || creditCard.getExpiryDate() == null) {
            errorString = "Credit card null values";
            return false;
        }

        UserController userController = new UserController();
        String res = userController.saveCreditCard(creditCard, token, userId);

        if (!res.isEmpty()) {
            String[] values = res.split(";");
            if (values[0].equals("success")) {
                rows = values[1];
                return true;
            } else {
                errorString = res;
                return false;
            }
        }
        return false;
    }

    protected String getErrorString() { return errorString; }

    protected String getRows() { return rows; }
}
