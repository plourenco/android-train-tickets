package com.example;

import com.example.dataHolder.*;
import com.example.seats.SeatModel;
import com.example.station.StationModel;
import com.example.steps.StepModel;
import com.example.trains.TrainModel;
import com.example.trips.TripModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("test")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("stations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationModel> getStations() {
        return StationHolder.getStations().values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("seats")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SeatModel> getSeats() {
        return SeatHolder.getSeats().values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("trains")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainModel> getTrains() {
        return TrainHolder.getTrains().values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("steps")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StepModel> getSteps() {
        return StepHolder.getSteps().values().stream().collect(Collectors.toList());
    }

    @GET
    @Path("trips")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TripModel> getTrips() {
        return TripHolder.getTrips().values().stream().collect(Collectors.toList());
    }
}
