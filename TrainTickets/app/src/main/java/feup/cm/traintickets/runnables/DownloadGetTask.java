package feup.cm.traintickets.runnables;

import android.os.AsyncTask;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.TicketModel;

/**
 * Created by mercurius on 13/04/17.
 */

public abstract class DownloadGetTask extends AsyncTask<Void, Void, Boolean> {

    private String token;
    private String direction;
    private String trip;
    private Date date;

    protected List<TicketModel> tickets;

    public DownloadGetTask(String token, String direction, String trip, Date date) {
        this.token = token;
        this.tickets = new ArrayList<>();
        this.direction = direction;
        this.trip = trip;
        this.date = date;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        TicketController ticketController = new TicketController();
        String res = ticketController.downloadTickets(direction, trip, date, token);

        if (res != null) {

        }
        return false;
    }

    @Override
    protected abstract void onPostExecute(Boolean success);

    @Override
    protected abstract void onCancelled();
}
