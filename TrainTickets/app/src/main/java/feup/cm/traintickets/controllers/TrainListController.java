package feup.cm.traintickets.controllers;

/**
 * Created by HP on 11/04/2017.
 */

public class TrainListController {

    public String getTripList(String token) {
        return ServiceHandler.makeGet("timetable/timetables", token);
    }
}
