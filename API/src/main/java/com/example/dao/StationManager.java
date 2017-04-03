package com.example.dao;

import com.example.Main;
import com.example.models.StationModel;
import com.example.mysql.MySQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mercurius on 21/03/17.
 */
public class StationManager {

    PreparedStatement ps;
    ResultSet rs;

    /**
     * Queries the database to return all the stations existent
     * @return A new list of stations
     */
    public List<StationModel> getStations(){
        try {
            List<StationModel> stations = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("CALL getStations()");
            rs = ps.executeQuery();
            while (rs.next()){
                int idStation = rs.getInt("id");
                int stationNumber = rs.getInt("stationNumber");
                String stationName = rs.getString("stationName");
                stations.add(new StationModel(idStation, stationNumber, stationName));
            }
            return stations;
        } catch (SQLException e) {
            Main.getLogger().severe(e.getMessage());
            return null;
        }
    }
}
