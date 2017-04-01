package com.example.station;

import com.example.security.Secured;
import com.example.users.UserRole;

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

    private final StationManager stationManager = new StationManager();

    @GET
    @Path("stations")
    @Secured({UserRole.USER, UserRole.INSPECTOR})
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationModel> getStations() {
        return stationManager.getStations();
    }
}
