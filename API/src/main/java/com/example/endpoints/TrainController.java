package com.example.endpoints;

import com.example.Main;
import com.example.dataholder.StationHolder;
import com.example.dataholder.TrainHolder;
import com.example.models.StationModel;
import com.example.models.StepModel;
import com.example.models.TrainModel;
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

@Path("train")
public class TrainController {

    @Path("trains")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainModel> getTrains() {

        PreparedStatement ps;
        ResultSet rs;

        try {
            List<TrainModel> trains = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM trains");
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                int maxCap = rs.getInt("maxCapacity");
                String desc = rs.getString("description");
                trains.add(new TrainModel(id, maxCap, desc, null));
            }
            return trains;
        } catch (SQLException e) {
            Main.getLogger().severe(e.getMessage());
            return null;
        }
    }
}
