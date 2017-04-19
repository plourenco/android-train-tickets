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

@Path("station")
public class StationController {

    private final StationManager stationManager = new StationManager();

    @GET
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationModel> getStations() {
        return stationManager.getStations();
    }
}
