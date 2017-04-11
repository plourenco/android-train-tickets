package feup.cm.traintickets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pedro on 26/03/17.
 */

public class SeatModel {

    private int id;
    private String seatNumber;
    private int trainId;

    public SeatModel(int id) {
        this.id = id;
    }
    public SeatModel(int id, String seatNumber) {
        this.id = id;
        this.seatNumber = seatNumber;
    }
    public SeatModel(int id, String seatNumber, int trainId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.trainId = trainId;
    }

    public int getId() {
        return id;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public int getTrainId() {
        return trainId;
    }
}
