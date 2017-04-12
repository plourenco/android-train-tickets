package feup.cm.traintickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;

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
}
