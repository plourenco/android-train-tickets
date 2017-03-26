package feup.cm.traintickets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pedro on 26/03/17.
 */

public class StationModel {

    private int id;
    private int stationNumber;
    private String stationName;

    public StationModel(int id, int stationNumber, String stationName) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.stationName = stationName;
    }

    public int getId() {
        return id;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public String getStationName() {
        return stationName;
    }
}
