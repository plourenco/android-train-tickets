package com.example.dao;

import com.example.Main;
import com.example.dataholder.StationHolder;
import com.example.dataholder.TripHolder;
import com.example.exceptions.IllegalArgumentExceptionMapper;
import com.example.exceptions.SQLExceptionMapper;
import com.example.models.AvailableTicketModel;
import com.example.models.TicketModel;
import com.example.mysql.MySQLManager;
import com.example.models.StationModel;
import com.example.models.StepModel;
import com.example.models.TripModel;
import sun.security.krb5.internal.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        } finally {
            ps = null;
            rs = null;
        }
    }

    /**
     * This generates a ticket, not yet validated nor saved on the database, just to demonstrate
     * the parameters to the client.
     * @param tripId the id of the trip chosen
     * @param startStationId the id of the starting station
     * @param arrStationId the id of the arrival station
     * @return TicketModel the ticket, with id 0, no uniqueId, no ticketDate nor purchaseDate and also no isUsed flag
     */
    public TicketModel generateTicketPrice(int tripId, int startStationId, int arrStationId) {
        try {
            TicketModel ticket = null;
            ps = MySQLManager.getConnection().prepareStatement("Call getFair(?,?,?)");
            ps.setInt(1, tripId);
            ps.setInt(2, startStationId);
            ps.setInt(3, arrStationId);

            rs = ps.executeQuery();
            if (rs.next()) {
                int fkTripId = rs.getInt("fkTrip");
                int depStationId = rs.getInt("departureStationId");
                int arrivalStationId = rs.getInt("arrivalStationId");
                //int distance = rs.getInt("distance");
                int duration = rs.getInt("duration");
                Time depTime = rs.getTime("departureTime");
                Time arrTime = rs.getTime("arrivalTime");
                float price = rs.getFloat("price");

                TripModel trip = TripHolder.getTrips().get(fkTripId);
                List<StepModel> steps =  trip.getSteps().stream()
                        .filter(s -> s.getDepartureStation().getId() >= depStationId
                                && s.getArrivalStation().getId() <= arrivalStationId)
                        .collect(Collectors.toList());
                trip.setSteps(steps);


                ticket = new TicketModel(0, null, StationHolder.getStations().get(depStationId),
                        StationHolder.getStations().get(arrivalStationId), null, price, null,
                        trip, false, duration, depTime, arrTime);

            }
            return ticket;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        } finally {
            ps = null;
            rs = null;
        }
    }

    /**
     * This method returns all the tickets available for the given date and the trip id
     * @param date the date
     * @param tripId the id of the trip
     * @return List<TicketModel> the list of the tickets, with only the parameters to verify its authenticity
     */
    public List<TicketModel> getAllTicket(Date date, int tripId) {
        try {
            List<TicketModel> tickets = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getTicketsControl(?,?)");
            ps.setDate(1, date);
            ps.setInt(2, tripId);

            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String uId = rs.getString("uniqueId");
                Date ticketDate = rs.getDate("ticketDate");
                int departureStationId = rs.getInt("departureStationId");
                int arrivalStationId = rs.getInt("arrivalStationId");

                tickets.add(new TicketModel(id, UUID.fromString(uId), ticketDate,
                        StationHolder.getStations().get(departureStationId),
                        StationHolder.getStations().get(arrivalStationId)));
            }
            return tickets;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        } catch (IllegalArgumentException arg) {
            Main.getLogger().severe(arg.getMessage());
            throw new IllegalArgumentExceptionMapper(arg.getMessage());
        }
    }

    /**
     * This method returns all the active tickets available for the given user id
     * @param userId the id of the user
     * @return List<TicketModel> the list of the active tickets
     */
    public List<TicketModel> getUserTickets(int userId) {
        try {
            List<TicketModel> tickets = new ArrayList<>();
            ps = MySQLManager.getConnection().prepareStatement("Call getUserTickets(?)");
            ps.setInt(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uniqueId");
                int id = rs.getInt("id");
                Time departureTime = rs.getTime("departureTime");
                Time arrivalTime = rs.getTime("arrivalTime");
                String departureStation = rs.getString("fromStation");
                String arrivalStation = rs.getString("toStation");
                int seatNumber = rs.getInt("seatNumber");
                String direction = rs.getString("direction");
                int tripId = rs.getInt("idTrip");

                StationModel depStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getStationName().equals(departureStation)).findFirst().get();
                StationModel arrStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getStationName().equals(arrivalStation)).findFirst().get();

                TripModel trip = TripHolder.getTrips().get(tripId);
                trip.setSteps(null);
                trip.setTrain(null);
                trip.setDirection(direction);

                //SeatModel seat = SeatHolder.getSeats().get(seatId);

                tickets.add(new TicketModel(id, UUID.fromString(uuid), depStation, arrStation, departureTime, arrivalTime, null, trip));
            }
            return tickets;
        } catch (SQLException sql){
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }

    /**
     * This method creates the ticket in the database and returns it as a valid ticket
     * @param ticket the ticket to be validated
     * @param userId the id of the user
     * @return TicketModel the validated ticket to be used
     */
    public TicketModel buyTicket(TicketModel ticket, int userId) {
        try {
            ticket.setUniqueId(UUID.randomUUID());
            ps = MySQLManager.getConnection().prepareStatement("Call buyTicket(?,?,?,?,?,?,?,?)");
            ps.setString(1, ticket.getUniqueId().toString());
            ps.setInt(2, ticket.getDepartureStation().getId());
            ps.setInt(3, ticket.getArrivalStation().getId());
            ps.setDate(4, ticket.getTicketDate());
            ps.setFloat(5, ticket.getPrice());
            ps.setDate(6, ticket.getPurchaseDate());
            ps.setInt(7, userId);
            ps.setInt(8, ticket.getTrip().getId());

            rs = ps.executeQuery();
            TicketModel ticketToRetrieve;
            if (rs.next()) {
                int ticketId = rs.getInt("id");
                String uniqueId = rs.getString("uniqueId");
                int depStationId = rs.getInt("departureStationId");
                int arrStationId = rs.getInt("arrivalStationId");
                Date ticketDate = rs.getDate("ticketDate");
                float price = rs.getFloat("price");
                Date purchaseDate = rs.getDate("purchaseDate");
                int idUser = rs.getInt("fkUser");
                int tripId = rs.getInt("fkTrip");
                boolean used = rs.getBoolean("isUsed");

                // TODO TICKET
            }

        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
        return null;
    }

    public int syncTickets(List<TicketModel> tickets) {
        try {
            int rows = 0;
            for (TicketModel t : tickets) {
                ps = MySQLManager.getConnection().prepareStatement("Call setStateToUsed(?)");
                ps.setString(1, t.getUniqueId().toString());

               rows = ps.executeUpdate();
            }
            return rows;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }

    public List<TicketModel> downloadTickets(String direction, String trip, Date date) {
       try {
           List<TicketModel> tickets = new ArrayList<>();
           ps = MySQLManager.getConnection().prepareStatement("Call getTicketsRevisor(?,?,?)");
           ps.setString(1, direction);
           ps.setString(2, trip);
           ps.setDate(3, date);

           rs = ps.executeQuery();
           while (rs.next()) {
               int id = rs.getInt("idTicket");
               UUID uniqueId = UUID.fromString(rs.getString("uniqueId"));
               int depStationId = rs.getInt("departureStationId");
               int arrStationId = rs.getInt("arrivalStationId");
               Date ticketDate = rs.getDate("ticketDate");
               int idTrip = rs.getInt("idTrip");
               boolean isUsed = rs.getBoolean("isUsed");

               tickets.add(new TicketModel(id, uniqueId, new StationModel(depStationId), new StationModel(arrStationId),
                       ticketDate, new TripModel(idTrip), isUsed));
           }
           return tickets;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }

    public List<TicketModel> getAllUserTicket(int userId) {
        List<TicketModel> tickets = new ArrayList<>();
        try {
            ps = MySQLManager.getConnection().prepareStatement("SELECT * FROM tickets WHERE fkUser=?");
            ps.setInt(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String uuid = rs.getString("uniqueId");
                int departureStationId = rs.getInt("departureStationId");
                int arrivalStationId = rs.getInt("arrivalStationId");
                Date ticketDate = rs.getDate("ticketDate");
                //Time depTime = rs.getTime("departureTime");
                //Time arrTime = rs.getTime("arrivalTime");
                float price = rs.getFloat("price");
                Date purchaseDate = rs.getDate("purchaseDate");
                int tripId = rs.getInt("fkTrip");
                boolean isUsed = rs.getBoolean("isUsed");

                StationModel depStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getId() == departureStationId).findFirst().get();
                StationModel arrStation = StationHolder.getStations().values().stream()
                        .filter(s -> s.getId() == arrivalStationId).findFirst().get();

                TripModel trip = TripHolder.getTrips().get(tripId);

                try {
                    tickets.add(new TicketModel(id, UUID.fromString(uuid), depStation, arrStation, ticketDate, price,
                            purchaseDate, trip, isUsed, 0, null, null));
                }
                catch(IllegalArgumentException ignored) { } // ignore invalid ticket
            }

            return tickets;
        } catch (SQLException sql) {
            Main.getLogger().severe(sql.getMessage());
            throw new SQLExceptionMapper(sql.getMessage());
        }
    }
}
