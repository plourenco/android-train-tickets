package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;

@XmlRootElement
public class TimetableCompleteModel {
    private int idTrain;
    private String traindescription;
    private String tripdescription;
    private String direction;
    private int tripId;
    private String increment;
    private int idStep;
    private int idStationDep;
    private int idStationArr;
    private String stationNameDep;
    private String stationNameArr;
    private Time departureTime;
    private Time arrivalTime;
    private int duration;
    private int waitingTime;

    public TimetableCompleteModel() {}
    public TimetableCompleteModel(int idTrain, String traindescription, String tripdescription, String direction,
                                  int tripId, String increment, int idStep, int idStationDep, int idStationArr,
                                  String stationNameDep, String stationNameArr, Time departureTime, Time arrivalTime,
                                  int duration, int waitingTime) {
        this.idTrain = idTrain;
        this.traindescription = traindescription;
        this.tripdescription = tripdescription;
        this.direction = direction;
        this.tripId = tripId;
        this.increment = increment;
        this.idStep = idStep;
        this.idStationDep = idStationDep;
        this.idStationArr = idStationArr;
        this.stationNameDep = stationNameDep;
        this.stationNameArr = stationNameArr;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.waitingTime = waitingTime;
    }

    public int getIdTrain() {
        return idTrain;
    }
    public String getTraindescription() {
        return traindescription;
    }
    public String getTripdescription() {
        return tripdescription;
    }
    public String getDirection() {
        return direction;
    }
    public int getTripId() {
        return tripId;
    }
    public String getIncrement() {
        return increment;
    }
    public int getIdStep() {
        return idStep;
    }
    public int getIdStationDep() {
        return idStationDep;
    }
    public int getIdStationArr() {
        return idStationArr;
    }
    public String getStationNameDep() {
        return stationNameDep;
    }
    public String getStationNameArr() {
        return stationNameArr;
    }
    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime() {
        return arrivalTime;
    }
    public int getDuration() {
        return duration;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
}
