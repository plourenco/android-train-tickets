package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import java.util.List;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.TicketModel;

public abstract class SyncPostTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private List<TicketModel> tickets;

    public SyncPostTask(String token, List<TicketModel> tickets) {
        this.token = token;
        this.tickets = tickets;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TicketController ticketController = new TicketController();
        String res = ticketController.syncWithServer(tickets, token);

        if (!res.equals("!update"))
            return true;
        else
            return false;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected void onCancelled() {

    }
}
