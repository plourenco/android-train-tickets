package feup.cm.traintickets.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
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
import feup.cm.traintickets.util.DateDeserializer;

public class TicketBrowser implements IOperation<TicketModel> {

    private SQLiteDatabase sqLiteReadableDatabase;
    private SQLiteDatabase sqLiteWritableDatabase;
    private Cursor cursor;

    public TicketBrowser(Context context) {
        sqLiteReadableDatabase = new SQLiteManager(context).getReadableDatabase();
        sqLiteWritableDatabase = new SQLiteManager(context).getWritableDatabase();
    }

    @Override
    public TicketModel get(String id) {
        cursor = sqLiteReadableDatabase.rawQuery("SELECT * FROM TICKETS WHERE ID = ?", new String[]{ id });
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return buildModel(cursor);
        }
        return null;
    }

    @Override
    public List<TicketModel> getAll() {
        cursor = sqLiteReadableDatabase.rawQuery("SELECT * FROM TICKETS", null);
        List<TicketModel> tickets = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                TicketModel model = buildModel(cursor);
                if(model != null) {
                    tickets.add(model);
                }
            }
        }
        return tickets;
    }

    @Override
    public void create(TicketModel ticketModel) throws SQLException {
        ContentValues values = new ContentValues();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            values.put("uniqueId", ticketModel.getUniqueId().toString());
            values.put("duration", ticketModel.getDuration());
            values.put("departureTime", (ticketModel.getDepartureTime() == null ? null :
                    ticketModel.getDepartureTime().toString()));
            values.put("arrivalTime", (ticketModel.getArrivalTime() != null ? null :
                    ticketModel.getArrivalTime().toString()));
            values.put("price", ticketModel.getPrice());
            values.put("ticketDate", format.format(ticketModel.getTicketDate()));
            values.put("purchaseDate", format.format(ticketModel.getPurchaseDate()));
            values.put("isUsed", ticketModel.getIsUsed() ? 1 : 0);
            values.put("departureStationId", ticketModel.getDepartureStation().getId());
            values.put("departureStationName", ticketModel.getDepartureStation().getStationName());
            values.put("arrivalStationId", ticketModel.getArrivalStation().getId());
            values.put("arrivalStationName", ticketModel.getArrivalStation().getStationName());
            values.put("tripId", ticketModel.getTrip().getId());
            values.put("tripDescription", ticketModel.getTrip().getDescription());
            values.put("tripDirection", ticketModel.getTrip().getDirection());
            values.put("seat", ticketModel.getSeatModel().getSeatNumber());
        }
        catch (NullPointerException | IllegalArgumentException npe) {
            Log.d("Null or Illegal", npe.getMessage());
            throw new SQLException(npe.getMessage());
        }
        try {
            sqLiteWritableDatabase.insert("tickets", null, values);
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
        return sqLiteWritableDatabase.delete("tickets", "id = ?", new String[]{ String.valueOf(id) });
    }

    @Override
    public void deleteAll() {
        sqLiteWritableDatabase.delete("tickets", null, null);
    }

    private TicketModel buildModel(Cursor cursor) {
        try {
            int id1 = cursor.getInt(0);
            String uniqueId = cursor.getString(1);
            int duration = cursor.getInt(2);
            Time departureTime = cursor.getString(3) == null ?
                    null : Time.valueOf(cursor.getString(3));
            Time arrivalTime = cursor.getString(4) == null ?
                    null : Time.valueOf(cursor.getString(4));
            float price = cursor.getFloat(5);
            DateDeserializer parser = new DateDeserializer();
            Date ticketDate = parser.deserialize(cursor.getString(6));
            Date purchaseDate = parser.deserialize(cursor.getString(7));
            boolean isUsed = (cursor.getInt(8)) != 0;
            int depStation = cursor.getInt(9);
            String depStationName = cursor.getString(10);
            int arrStation = cursor.getInt(11);
            String arrStationName = cursor.getString(12);
            int trip = cursor.getInt(13);
            String tripDescription = cursor.getString(14);
            String tripDirection = cursor.getString(15);
            String seat = cursor.getString(16);
            return new TicketModel(id1, UUID.fromString(uniqueId),
                    new StationModel(depStation, depStation, depStationName),
                    new StationModel(arrStation, arrStation, arrStationName), ticketDate, price,
                    purchaseDate, new TripModel(trip, tripDescription, tripDirection, null, null, null),
                    isUsed, departureTime, arrivalTime, new SeatModel(seat),
                    duration);
        }
        catch(NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }

    public void close() {
        sqLiteWritableDatabase.close();
        sqLiteReadableDatabase.close();
    }
}
