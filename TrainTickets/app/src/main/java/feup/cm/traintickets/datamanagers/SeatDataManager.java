package feup.cm.traintickets.datamanagers;

import java.util.List;

import feup.cm.traintickets.models.SeatModel;
import feup.cm.traintickets.models.StepModel;

public class SeatDataManager {

    private static List<SeatModel> seats;

    public static List<SeatModel> getSeats() {
        return seats;
    }

    public static void setSeats(List<SeatModel> seats) {
        SeatDataManager.seats = seats;
    }
}
