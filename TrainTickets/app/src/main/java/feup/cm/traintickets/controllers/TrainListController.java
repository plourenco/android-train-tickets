package feup.cm.traintickets.controllers;

/**
 * Created by HP on 11/04/2017.
 */

public class TrainListController {

    public String getTripList(String token, int depStation, int arrStation) {
        return ServiceHandler.makeGet("timetable/timetables/" + depStation
                + "/" + arrStation, token);
    }
}
