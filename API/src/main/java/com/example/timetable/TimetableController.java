package com.example.timetable;

/**
 * Created by mercurius on 15/03/17.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This class serves the purpose of exposing the train timetables.
 * Root resource exposed at /timetable
 */
@Path("timetable")
public class TimetableController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTimetableByTrain(){
        return "Timetables here!";
    }
}
