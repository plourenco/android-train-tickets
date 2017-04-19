package feup.cm.traintickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import feup.cm.traintickets.models.SeatModel;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;

public class TicketBrowser implements IOperation<TicketModel> {

    SQLiteDatabase sqLiteReadableDatabase;
    SQLiteDatabase sqLiteWritableDatabase;
    Cursor cursor;

    public TicketBrowser(Context context) {
        sqLiteReadableDatabase = new SQLiteManager(context).getReadableDatabase();
        sqLiteWritableDatabase = new SQLiteManager(context).getWritableDatabase();
    }

    @Override
    public TicketModel get(String id) {
        cursor = sqLiteReadableDatabase.rawQuery("Select * from tickets where id = ?", new String[]{""+id});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id1 = cursor.getInt(0);
            String uniqueId = cursor.getString(1);
            int duration = cursor.getInt(2);
            Time departureTime = Time.valueOf(cursor.getString(3));
            Time arrivalTime = Time.valueOf(cursor.getString(4));
            float price = cursor.getFloat(5);
            Date ticketDate = Date.valueOf(cursor.getString(6));
            Date purchaseDate = Date.valueOf(cursor.getString(7));
            boolean isUsed = (cursor.getInt(8)) != 0;
            int depStation = cursor.getInt(9);
            int arrStation = cursor.getInt(10);
            int trip = cursor.getInt(11);
            int seat = cursor.getInt(12);
            return new TicketModel(id1, UUID.fromString(uniqueId), new StationModel(depStation),
                    new StationModel(arrStation), ticketDate, price, purchaseDate,
                    new TripModel(trip), isUsed, departureTime, arrivalTime, new SeatModel(seat),
                    duration);
        } else {
            return null;
        }
    }

    @Override
    public List<TicketModel> getAll() {
        cursor = sqLiteReadableDatabase.rawQuery("Select * from tickets", new String[]{});
        if (cursor.getCount() > 0) {
            List<TicketModel> tickets = new ArrayList<>();
            while (cursor.moveToNext()) {
                tickets.add(get(""+cursor.getInt(0)));
            }
            return tickets;
        } else {
            return null;
        }
    }

    @Override
    public void create(TicketModel ticketModel) {
        ContentValues values = new ContentValues();
        try {
            values.put("uniqueId", ticketModel.getUniqueId().toString());
            values.put("duration", ticketModel.getDuration());
            //values.put("departureTime", ticketModel.getDepartureTime().toString());
            values.put("departureTime", "08:00:00");
            //values.put("arrivalTime", ticketModel.getArrivalTime().toString());
            values.put("arrivalTime", "10:00:00");
            values.put("price", ticketModel.getPrice());
            values.put("ticketDate", ticketModel.getTicketDate().toString());
            values.put("purchaseDate", ticketModel.getPurchaseDate().toString());
            values.put("isUsed", ticketModel.getIsUsed() ? 1 : 0);
            values.put("departureStation", ticketModel.getDepartureStation().getId());
            values.put("arrivalStation", ticketModel.getArrivalStation().getId());
            values.put("trip", ticketModel.getTrip().getId());
            //values.put("seat", ticketModel.getSeatModel().getId());
        } catch (NullPointerException npe) {
            throw new NullPointerException(npe.getMessage());
        }
        try {
            sqLiteWritableDatabase.insertOrThrow("tickets", null, values);
        } catch (SQLException e) {
            Log.d("SQL", e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Delete a record in the sqlite database
     * @param id the id to be deleted
     * @return int - returns the number of affected rows
     */
    @Override
    public int delete(int id) {
        return sqLiteWritableDatabase.delete("tickets", "id = ?", new String[]{""+id});
    }

    public void close() {
        sqLiteWritableDatabase.close();
        sqLiteReadableDatabase.close();
    }
}
