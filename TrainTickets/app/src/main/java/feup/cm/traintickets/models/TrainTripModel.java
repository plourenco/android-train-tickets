package feup.cm.traintickets.models;

import java.sql.Time;

public class TrainTripModel {

    private int id;
    private String description;
    private Time departureTime;
    private Time arrivalTime;
    private int duration;


    public TrainTripModel(int id,String description,Time departureTime,Time arrivalTime,int duration){
        this.id=id;
        this.description=description;
        this.departureTime=departureTime;
        this.arrivalTime=arrivalTime;
        this.duration=duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
