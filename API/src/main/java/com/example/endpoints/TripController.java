package com.example.endpoints;

import com.example.Main;
import com.example.dataholder.TripHolder;
import com.example.models.TrainModel;
import com.example.models.TripModel;
import com.example.mysql.MySQLManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("trip")
public class TripController {

    @GET
    @Path("trips")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TripModel> getTrips() {

        PreparedStatement ps;
        ResultSet rs;

        try {
            List<TripModel> trips  = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM trips");
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String desc = rs.getString("description");
                String direction = rs.getString("direction");
                String increment = rs.getString("increment");
                int idTrain = rs.getInt("skTrain");
                trips.add(new TripModel(id, desc, direction, increment, new TrainModel(idTrain), null));
            }
            return trips;
        } catch (SQLException e) {
            Main.getLogger().severe(e.getMessage());
            return null;
        }
    }
}
