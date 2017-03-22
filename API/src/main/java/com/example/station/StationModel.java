package com.example.station;

/**
 * Created by mercurius on 15/03/17.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class serves the purpose of being a train station model
 */
@XmlRootElement
public class StationModel {
    @JsonProperty("idStations")
    private int stationID;
    private int stationNumber;
    private String stationName;

    public int getStationID() {
        return stationID;
    }
    public String getStationName() {
        return stationName;
    }
    public int getStationNumber() {
        return stationNumber;
    }

    public StationModel(int stationID, int stationNumber, String stationName) {
        this.stationID = stationID;
        this.stationNumber = stationNumber;
        this.stationName = stationName;
    }
    public StationModel() {
    }
}
