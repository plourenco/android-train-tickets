package com.example.endpoints;

import com.example.annotations.Secured;
import com.example.dataholder.StepHolder;
import com.example.models.StepModel;
import com.example.models.UserRole;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("step")
public class StepController {

    @GET
    @Secured({ UserRole.USER, UserRole.INSPECTOR })
    @Path("steps")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StepModel> getSteps() {
        return StepHolder.getSteps().values().stream().collect(Collectors.toList());
    }
}
