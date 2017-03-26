package feup.cm.traintickets.models;

import java.util.List;

/**
 * Created by pedro on 26/03/17.
 */

public class TrainModel {

    private int id;
    private int maxCapacity;
    private String description;
    private List<SeatModel> seats;

    /**
     * Constructors
     */
    public TrainModel(int id, int maxCapacity, String description, List<SeatModel> seats) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.description = description;
        this.seats = seats;
    }

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
}
