package com.example.trains;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.seats.SeatModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This class serves the purpose of being the train model.
 */
@XmlRootElement
public class TrainModel {
    private int id;
    private int maxCapacity;
    private String description;
    private List<SeatModel> seats;

    /**
     * Getters
     */
    public int getId() {
        return id;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public String getDescription() {
        return description;
    }
    public List<SeatModel> getSeats() {
        return seats;
    }

    /**
     * Constructors
     */
    public TrainModel() {
    }
    public TrainModel(int id, int maxCapacity, String description, List<SeatModel> seats) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.description = description;
        this.seats = seats;
    }
}
