package com.example.station;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mercurius on 21/03/17.
 */

@Path("station")
public class StationController {

    StationManager stationManager;

    @GET
    @Path("getStations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationModel> getStations() {
        if (stationManager == null) { stationManager = new StationManager(); }
        return stationManager.getStations();
    }
}

