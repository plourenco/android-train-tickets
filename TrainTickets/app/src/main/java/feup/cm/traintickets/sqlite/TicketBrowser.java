package feup.cm.traintickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;

/**
 * Created by mercurius on 06/04/17.
 */

public class TicketBrowser implements IOperation<TicketModel> {

    SQLiteDatabase sqLiteReadableDatabase;
    SQLiteDatabase sqLiteWritableDatabase;
    Cursor cursor;

    public TicketBrowser(Context context) {
        sqLiteReadableDatabase = new SQLiteManager(context).getReadableDatabase();
        sqLiteWritableDatabase = new SQLiteManager(context).getWritableDatabase();
    }

    @Override
    public TicketModel get(int id) {
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
            return new TicketModel(id1, UUID.fromString(uniqueId),new StationModel(depStation), new StationModel(arrStation),
                    ticketDate, price, purchaseDate, new TripModel(trip), isUsed);
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
                tickets.add(get(cursor.getInt(0)));
            }
            return tickets;
        } else {
            return null;
        }
    }

    //TO:DO change the hardcoded values and model!
    @Override
    public void create(TicketModel ticketModel) {
        ContentValues values = new ContentValues();
        values.put("uniqueId", ticketModel.getUniqueId().toString());
        values.put("duration", 10);
        values.put("departureTime", "20:00:00");
        values.put("arrivalTime", "20:10:00");
        values.put("price", ticketModel.getPrice());
        values.put("ticketDate", ticketModel.getTicketDate().toString());
        values.put("purchaseDate", ticketModel.getPurchaseDate().toString());
        values.put("isUsed", ticketModel.isUsed() ? 1 : 0);
        values.put("departureStation", ticketModel.getDepartureStation().getId());
        values.put("arrivalStation", ticketModel.getArrivalStation().getId());
        values.put("trip", ticketModel.getTrip().getId());
        values.put("seat", "1");
        try {
            sqLiteWritableDatabase.insertOrThrow("tickets", null, values);
        } catch (Exception e) {
            Log.w("SQL", e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        sqLiteWritableDatabase.delete("tickets", "id = ?", new String[]{""+id});
    }
}
