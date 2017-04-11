package com.example.endpoints;

import com.example.Main;
import com.example.dataholder.StationHolder;
import com.example.dataholder.StepHolder;
import com.example.models.StationModel;
import com.example.models.StepModel;
import com.example.mysql.MySQLManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("step")
public class StepController {

    @GET
    @Path("steps")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StepModel> getSteps() {
        PreparedStatement ps;
        ResultSet rs;

        try {
            List<StepModel> steps = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM steps");
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                int depStationId = rs.getInt("departureStationId");
                int arrStationId = rs.getInt("arrivalStationId");
                int stepNumber = rs.getInt("stepNumber");
                int distance = rs.getInt("distance");
                double price = rs.getDouble("price");
                int waitingTime = rs.getInt("waitingTime");
                int tripId = rs.getInt("fkTrip");
                int duration = rs.getInt("duration");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");

                StationModel depStation = StationHolder.getStations().values().stream().filter(p -> p.getId() == depStationId).findFirst().get();
                StationModel arrStation = StationHolder.getStations().values().stream().filter(p -> p.getId() == arrStationId).findFirst().get();

                steps.add(new StepModel(id, stepNumber, distance, price, waitingTime, duration, depTime, arrTime,
                        depStation, arrStation, tripId));
            }
            return steps;
        } catch (SQLException e) {
            Main.getLogger().severe(e.getMessage());
            return null;
        }

        //return StepHolder.getSteps().values().stream().collect(Collectors.toList());
    }
}
