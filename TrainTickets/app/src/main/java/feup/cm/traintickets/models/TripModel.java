package feup.cm.traintickets.models;

import java.util.List;

/**
 * Created by pedro on 26/03/17.
 */
public class TripModel {

    private int id;
    private String description;
    private String direction;
    private String increment;
    private List<StepModel> steps;
    private TrainModel train;

    public TripModel(int id, String description, String direction, String increment, TrainModel train,
                     List<StepModel> steps) {
        this.id = id;
        this.description = description;
        this.direction = direction;
        this.increment = increment;
        this.train = train;
        this.steps = steps;
    }
    public TripModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
