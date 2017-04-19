package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SeatModel {
    private int id;
    private String seatNumber;
    private int trainId;

    public SeatModel(String seat) {
        this.seatNumber = seat;
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

    public SeatModel() {

    }
    public SeatModel(int id, String seatNumber, int trainId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.trainId = trainId;
    }
}
