package feup.cm.traintickets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class StationModel {

    private int id;
    private int stationNumber;
    private String stationName;

    public StationModel(int id, int stationNumber, String stationName) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.stationName = stationName;
    }

    public StationModel(int id) {
        this.id = id;
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

    public String toString()
    {
        return stationName;
    }
}
