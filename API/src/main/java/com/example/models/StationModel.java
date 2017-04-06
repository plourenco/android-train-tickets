package com.example.models;

/**
 * Created by mercurius on 15/03/17.
 */

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class serves the purpose of being a train station model
 */
@XmlRootElement
public class StationModel {
    private int id;
    private int stationNumber;
    private String stationName;

    public int getId() {
        return id;
    }
    public String getStationName() {
        return stationName;
    }
    public int getStationNumber() {
        return stationNumber;
    }

    public StationModel() {

    }
    public StationModel(int id, int stationNumber, String stationName) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.stationName = stationName;
    }
}
