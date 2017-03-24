package com.example.timetable;

import com.example.Main;
import com.example.exceptions.SQLExceptionMapper;
import com.example.mysql.MySQLManager;

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
    public List<TimetableModel> getSchedule(int departureStation, int arrivalStation) {
        try {
            List<TimetableModel> schedule = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getSchedule(?,?)");
            ps.setInt(1, departureStation);
            ps.setInt(2, arrivalStation);

            rs = ps.executeQuery();
            while (rs.next()) {
                String depStation = rs.getString("startStation");
                String arrStation = rs.getString("endStation");
                int tripId = rs.getInt("id");
                int depStationId = rs.getInt("departureStationI");
                int arrStationId = rs.getInt("arrivalStationId");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");
                schedule.add(new TimetableModel(depStation, arrStation, tripId, depStationId,
                        arrStationId, depTime, arrTime));
            }
            return schedule;
        } catch (SQLException sql) { // Needs to rethrow
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }
}
