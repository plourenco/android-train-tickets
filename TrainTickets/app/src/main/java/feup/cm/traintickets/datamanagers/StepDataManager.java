package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.StepModel;

/**
 * Created by mercurius on 11/04/17.
 */

public class StepDataManager {
    private static List<StepModel> steps;

    public static List<StepModel> getSteps() {
        return steps;
    }

    public static void setSteps(List<StepModel> steps) {
        StepDataManager.steps = steps;
    }
}
