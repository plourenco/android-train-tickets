package feup.cm.traintickets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pedro on 26/03/17.
 */

public class SeatModel {

    private int id;
    private String seatNumber;

    public int getId() {
        return id;
    }
    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatModel(int id, String seatNumber) {
        this.id = id;
        this.seatNumber = seatNumber;
    }
}
