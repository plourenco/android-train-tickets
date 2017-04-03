package com.example.endpoints;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.models.TimetableModel;
import com.example.dao.TimetableManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class serves the purpose of exposing the train timetables.
 * Root resource exposed at /timetable
 */
@Path("timetable")
public class TimetableController {

    private final TimetableManager timetableManager = new TimetableManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTimetableByTrain(){
        return "Timetables here!";
    }

    /**
     * Get a list of schedules for the given departureStation and arrival station
     * @param depStation id of departure station
     * @param arrStation id of arrival station
     * @return list of StepModel
     */
    @GET
    @Path("timetables/{depStation}/{arrStation}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TimetableModel> getSchedulesByStation(@PathParam("depStation") int depStation,
                                                      @PathParam("arrStation") int arrStation) {

        return timetableManager.getSchedule(depStation, arrStation); // Needs completion!!
    }
}

