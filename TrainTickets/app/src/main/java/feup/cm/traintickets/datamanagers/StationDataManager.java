package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.StationModel;

public class StationDataManager {

    private static List<StationModel> stations;

    public static List<StationModel> getStations() {
        return stations;
    }

    public static void setStations(List<StationModel> stations) {
        StationDataManager.stations = stations;
    }
}
