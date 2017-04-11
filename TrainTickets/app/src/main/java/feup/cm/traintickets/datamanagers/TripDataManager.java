package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.TripModel;

/**
 * Created by mercurius on 11/04/17.
 */

public class TripDataManager {

    private static List<TripModel> trips;

    public static List<TripModel> getTrips() {
        return trips;
    }

    public static void setTrips(List<TripModel> trips) {
        TripDataManager.trips = trips;
    }
}
