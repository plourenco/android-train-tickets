package com.example.endpoints;

import com.example.models.StationModel;
import com.example.annotations.Secured;
import com.example.dao.StationManager;
import com.example.models.UserRole;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
