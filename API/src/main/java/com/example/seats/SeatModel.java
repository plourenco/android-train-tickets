package com.example.seats;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mercurius on 22/03/17.
 */
@XmlRootElement
public class SeatModel {
    private int id;
    private String seatNumber;
    private int trainId;

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
