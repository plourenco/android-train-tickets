package com.example.endpoints;

import com.example.annotations.Secured;
import com.example.dao.StationManager;
import com.example.dataholder.TripHolder;
import com.example.models.TripModel;
import com.example.models.UserRole;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("trip")
public class TripController {

    @GET
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("trips")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TripModel> getTrips() {
        return TripHolder.getTrips().values().stream().collect(Collectors.toList());
    }

    @GET
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("direction")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDirections() {
        StationManager stationManager = new StationManager();
        List<String> directions = stationManager.getDirections();
        return directions;
    }
}
