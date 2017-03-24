package com.example.timetable;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;

/**
 * Created by mercurius on 24/03/17.
 */
@XmlRootElement
public class TimetableModel {
    private String startStation;
    private String endStation;
    @JsonProperty("idTrip")
    private int id;
    private int departureStationId;
    private int arrivalStationId;
    private Time departureTime;
    private Time arrivalTime;

    /**
     * Getters
     */
    public String getStartStation() {
        return startStation;
    }
    public String getEndStation() {
        return endStation;
    }
    public int getId() {
        return id;
    }
    public int getDepartureStationId() {
        return departureStationId;
    }
    public int getArrivalStationId() {
        return arrivalStationId;
    }
    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime() {
        return arrivalTime;
    }


    /**
     * Constructors
     */
    public TimetableModel() {
    }
    public TimetableModel(String startStation, String endStation, int id, int departureStationId, int arrivalStationId, Time departureTime, Time arrivalTime) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.id = id;
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
