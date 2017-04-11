package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.TrainModel;

/**
 * Created by mercurius on 11/04/17.
 */

public class TrainDataManager {
    private static List<TrainModel> trains;

    public static List<TrainModel> getTrains() {
        return trains;
    }

    public static void setTrains(List<TrainModel> trains) {
        TrainDataManager.trains = trains;
    }
}
