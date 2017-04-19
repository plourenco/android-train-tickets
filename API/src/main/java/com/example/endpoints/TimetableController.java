package com.example.endpoints;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.annotations.Secured;
import com.example.models.TimetableCompleteModel;
import com.example.models.TimetableModel;
import com.example.dao.TimetableManager;
import com.example.models.UserRole;

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
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("full")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TimetableCompleteModel> getTimetables(){
        return timetableManager.getFullTimetable();
    }

    /**
     * Get a list of schedules for the given departureStation and arrival station
     * @param depStation id of departure station
     * @param arrStation id of arrival station
     * @return list of StepModel
     */
    @GET
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("timetables/{depStation}/{arrStation}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TimetableModel> getSchedulesByStation(@PathParam("depStation") int depStation,
                                                      @PathParam("arrStation") int arrStation) {

        return timetableManager.getSchedule(depStation, arrStation); // Needs completion!!
    }
}

