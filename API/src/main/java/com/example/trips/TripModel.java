package com.example.trips;

import com.example.steps.StepModel;
import com.example.trains.TrainModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by mercurius on 21/03/17.
 */
@XmlRootElement
public class TripModel {
    private int id;
    private String description;
    private String direction;
    private String increment;
    private List<StepModel> steps;
    private TrainModel train;

    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getDirection() {
        return direction;
    }
    public String getIncrement() {
        return increment;
    }
    public TrainModel getTrain() {
        return train;
    }
    public List<StepModel> getSteps() {
        return steps;
    }

    public TripModel() {
    }
    public TripModel(int id, String description, String direction, String increment, TrainModel train, List<StepModel> steps) {
        this.id = id;
        this.description = description;
        this.direction = direction;
        this.increment = increment;
        this.train = train;
        this.steps = steps;
    }
}
