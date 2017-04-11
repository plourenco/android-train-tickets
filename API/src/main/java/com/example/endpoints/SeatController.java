package com.example.endpoints;

import com.example.dataholder.SeatHolder;
import com.example.models.SeatModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("seat")
public class SeatController {

    @GET
    @Path("seats")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SeatModel> getSeats() {
        return SeatHolder.getSeats().values().stream().collect(Collectors.toList());
    }
}
