package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;

/**
 * Created by mercurius on 22/03/17.
 */
@XmlRootElement
public class StepModel {
    private int id;
    private int stepNumber;
    private int distance;
    private double price;
    private int waitingTime;
    private int duration;
    private Time departureTime;
    private Time arrivalTime;
    private StationModel departureStation;
    private StationModel arrivalStation;
    private int tripId;

    /**
     * Getters
     */
    public int getId() {
        return id;
    }
    public int getStepNumber() {
        return stepNumber;
    }
    public int getDistance() {
        return distance;
    }
    public double getPrice() {
        return price;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public int getDuration() {
        return duration;
    }
    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime() {
        return arrivalTime;
    }
    public StationModel getDepartureStation() {
        return departureStation;
    }
    public StationModel getArrivalStation() {
        return arrivalStation;
    }
    public int getTripId() {
        return tripId;
    }

    /**
     * Constructors
     */
    public StepModel() {

    }
    public StepModel(int id, int stepNumber, int distance, double price, int waitingTime, int duration,
                     Time departureTime, Time arrivalTime, StationModel departureStation,
                     StationModel arrivalStation, int tripId) {
        this.id = id;
        this.stepNumber = stepNumber;
        this.distance = distance;
        this.price = price;
        this.waitingTime = waitingTime;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.tripId = tripId;
    }
}
