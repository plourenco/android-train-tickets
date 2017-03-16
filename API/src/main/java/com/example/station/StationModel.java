package com.example.station;

/**
 * Created by mercurius on 15/03/17.
 */

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class serves the purpose of being a train station model
 */
@XmlRootElement
public class StationModel {
    private int stationID;
    private String stationName;

    public int getStationID() {
        return stationID;
    }
    public String getStationName() {
        return stationName;
    }

    public StationModel(int stationID, String stationName) {
        this.stationID = stationID;
        this.stationName = stationName;
    }
}
