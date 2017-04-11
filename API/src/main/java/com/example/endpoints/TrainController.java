package com.example.endpoints;

import com.example.dataholder.TrainHolder;
import com.example.models.TrainModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("train")
public class TrainController {

    @Path("trains")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainModel> getTrains() {
        return TrainHolder.getTrains().values().stream().collect(Collectors.toList());
    }
}
