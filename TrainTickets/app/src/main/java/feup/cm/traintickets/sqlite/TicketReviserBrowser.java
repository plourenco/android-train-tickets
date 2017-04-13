package feup.cm.traintickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import feup.cm.traintickets.controllers.TicketController;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.SyncPostTask;

public class TicketReviserBrowser implements IOperation<TicketModel> {

    SQLiteDatabase sqLiteReadableDatabase;
    SQLiteDatabase sqLiteWritableDatabase;
    Cursor cursor;

    public TicketReviserBrowser(Context context) {
        sqLiteReadableDatabase = new SQLiteManager(context).getReadableDatabase();
        sqLiteWritableDatabase = new SQLiteManager(context).getWritableDatabase();
    }

    @Override
    public TicketModel get(String uuid) {
        cursor = sqLiteReadableDatabase.rawQuery("Select * from ticketsReviser where uniqueId=?", new String[]{""+uuid});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id1 = cursor.getInt(0);
            String uniqueId = cursor.getString(1);
            int depStationId = cursor.getInt(2);
            int arrStationId = cursor.getInt(3);
            Date ticketDate = Date.valueOf(cursor.getString(4));
            boolean isUsed = (cursor.getInt(5)) != 0;
            int tripId = cursor.getInt(6);

            return new TicketModel(id1, UUID.fromString(uniqueId), new StationModel(depStationId),
                    new StationModel(arrStationId), ticketDate, isUsed, new TripModel(tripId));
        } else {
            return null;
        }
    }

    @Override
    public List<TicketModel> getAll() {
        cursor = sqLiteReadableDatabase.rawQuery("Select * from ticketsReviser", null);
        if (cursor.getCount() > 0) {
            List<TicketModel> tickets = new ArrayList<>();
            while (cursor.moveToNext()) {
                tickets.add(get(cursor.getString(1)));
            }
            return tickets;
        } else
            return null;
    }

    /**
     * Ticket Model must contain uniqueId, depStationId, arrStationId,
     * ticketDate, isUsed and tripId
     * @param ticketModel
     */
    @Override
    public void create(TicketModel ticketModel) {
        ContentValues values = new ContentValues();
        try {
            values.put("uniqueId", ticketModel.getUniqueId().toString());
            values.put("departureStationId", ticketModel.getDepartureStation().getId());
            values.put("arrivalStationId", ticketModel.getArrivalStation().getId());
            values.put("ticketDate", ticketModel.getTicketDate().toString());
            values.put("isUsed", ticketModel.getIsUsed() ? 1 : 0);
            values.put("tripId", ticketModel.getTrip().getId());
        } catch (NullPointerException npe) {
            throw new NullPointerException(npe.getMessage());
        }
        try {
            sqLiteWritableDatabase.insertOrThrow("ticketsReviser", null, values);
        } catch (SQLException e) {
            Log.e("SQL", e.getMessage());
        }
    }

    @Override
    public int delete(int id) {
        return sqLiteWritableDatabase.delete("ticketsReviser", "id = ?", new String[]{""+id});
    }

    public void setTicketUsed(String uuid) {
        ContentValues cv = new ContentValues();
        cv.put("isUsed", 1);

        sqLiteWritableDatabase.update("ticketsReviser", cv, "uniqueId=?", new String[]{uuid});
    }

    public void deleteAll() {
        sqLiteWritableDatabase.delete("ticketsReviser", null, null);
    }
    /**
     * Synchronize tickets with server db
     * @param tickets
     * @param token
     */
    public void synchronize(List<TicketModel> tickets, String token) {
        SyncPostTask syncPostTask = new SyncPostTask(token, tickets) {
            @Override
            protected void onPostExecute(Boolean success) {
                // TODO Delete from SQLite
                if (success) {
                    deleteAll();
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        syncPostTask.execute((Void) null);
    }
}
