package com.example.dao;

import com.example.Main;
import com.example.exceptions.SQLExceptionMapper;
import com.example.models.TimetableCompleteModel;
import com.example.models.TimetableModel;
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
                int depStationId = rs.getInt("departureStationId");
                int arrStationId = rs.getInt("arrivalStationId");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");
                String description=rs.getString("description");
                schedule.add(new TimetableModel(depStation, arrStation, tripId, depStationId,
                        arrStationId, depTime, arrTime,description));
            }
            return schedule;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }

    public List<TimetableCompleteModel> getFullTimetable() {
        try {
            List<TimetableCompleteModel> completeSchedule = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getTimeTable()");

            rs = ps.executeQuery();
            while (rs.next()) {
                int idTrain = rs.getInt("idTrain");
                String trainDesc = rs.getString("traindescription");
                String tripDesc = rs.getString("tripdescription");
                String direction = rs.getString("direction");
                int tripId = rs.getInt("tripId");
                String increment = rs.getString("increment");
                int idStep = rs.getInt("idStep");
                int idStationDep = rs.getInt("idStationDep");
                int idStationArr = rs.getInt("idStationArr");
                String stationNameDep = rs.getString("stationNameDep");
                String stationNameArr = rs.getString("stationNameArr");
                Time departureTime = rs.getTime("departureTime");
                Time arrivalTime = rs.getTime("arrivalTime");
                int duration = rs.getInt("duration");
                int waitingTime = rs.getInt("waitingTime");
                completeSchedule.add(new TimetableCompleteModel(idTrain, trainDesc, tripDesc, direction, tripId,
                        increment, idStep, idStationDep, idStationArr, stationNameDep, stationNameArr, departureTime,
                        arrivalTime, duration, waitingTime));
            }
            return completeSchedule;
        } catch (SQLException sql) { // Needs to rethrow
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }
}
