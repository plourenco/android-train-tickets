package com.example.endpoints;

import com.example.dataholder.TripHolder;
import com.example.models.TripModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("trip")
public class TripController {

    @GET
    @Path("trips")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TripModel> getTrips() {
        return TripHolder.getTrips().values().stream().collect(Collectors.toList());
    }
}
