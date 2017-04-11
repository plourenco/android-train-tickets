package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.TrainTripModel;

public class TrainListDataManager {

    private static List<TrainTripModel> traintrips;

    public static List<TrainTripModel> getTrainTrips() {
        return traintrips;
    }

    public static void setTrainTrips(List<TrainTripModel> traintrips) {
        TrainListDataManager.traintrips = traintrips;
    }
}