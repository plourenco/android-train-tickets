package feup.cm.traintickets.controllers;

/**
 * Created by mercurius on 11/04/17.
 */

public class StepController {
    public String getSteps(String token) {
        return ServiceHandler.makeGet("step/steps", token);
    }
}
