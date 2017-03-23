package com.example.timetable;

import com.example.Main;
import com.example.mysql.MySQLManager;
import com.example.station.StationModel;
import com.example.steps.StepModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mercurius on 15/03/17.
 */
public class TimetableManager {

    PreparedStatement ps;
    ResultSet rs;

    /**
     * Returns a database set of schedules for the given departure station to the arrival station.
     * @param departureStation the id of the departure station
     * @param arrivalStation the id of the arrival station
     * @return a list of StepModel
     */
    public List<StepModel> getSchedule(int departureStation, int arrivalStation) {
        try {
            List<StepModel> steps = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getSchedule(?,?)");
            ps.setInt(1, departureStation);
            ps.setInt(2, arrivalStation);

            rs = ps.executeQuery();
            while (rs.next()) {
                int stepId = rs.getInt("id");
                String depStation = rs.getString("startStation");
                String arrStation = rs.getString("endStation");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");
                steps.add(new StepModel(stepId, stepId, -1, -1d, -1, -1, depTime, arrTime,
                        new StationModel(0, 0, depStation), new StationModel(0, 0, arrStation)));
            }
            return steps;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
        }
        return null;
    }
}
