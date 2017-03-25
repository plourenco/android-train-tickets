package com.example.tickets;

import com.example.Main;
import com.example.exceptions.IllegalArgumentExceptionMapper;
import com.example.exceptions.SQLExceptionMapper;
import com.example.mysql.MySQLManager;

import java.sql.*;
import java.text.ParseException;

/**
 * Created by Pedro on 12/03/17.
 */
public class TicketManager {

    PreparedStatement ps;
    ResultSet rs;

    /**
     * Get the available tickets given a date, departure station, arrival station, and the trip
     * @param dateToCheck
     * @param depStation
     * @param arrStation
     * @param trip
     * @return
     */
    public AvailableTicketModel getAvailableTickets(Date dateToCheck, int depStation, int arrStation, int trip){
        if (dateToCheck == null | depStation <= 0 || arrStation <= 0 || trip <= 0) {
            throw new IllegalArgumentExceptionMapper("Error in arguments number, type or value. Perhaps some argument is wrong.");
        }

        try {
            AvailableTicketModel availableTickets = null;
            ps = MySQLManager.getConnection().prepareStatement("Call availableTickets(?,?,?,?)");
            ps.setDate(1, dateToCheck);
            ps.setInt(2, depStation);
            ps.setInt(3, arrStation);
            ps.setInt(4, trip);

            rs = ps.executeQuery();
            if (rs.next()) {
                int sold = rs.getInt("sold");
                int maxCapacity = rs.getInt("maxCapacity");
                availableTickets = new AvailableTicketModel(sold, maxCapacity);
            }
            return availableTickets;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }
}
