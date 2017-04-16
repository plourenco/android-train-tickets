package com.example.endpoints;

import com.example.Main;
import com.example.exceptions.ParseExceptionMapper;
import com.example.models.AvailableTicketModel;
import com.example.models.TicketModel;
import com.example.dao.TicketManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Root resource (exposed at "tickets" path)
 */
@Path("tickets")
public class TicketController {

    private final TicketManager ticketManager = new TicketManager();

    @GET
    @Path("user-tickets-exp/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketModel> getExpiredUserTickets(@PathParam("id")int id) {
        return ticketManager.getExpiredUserTickets(id);
    }

    @GET
    @Path("download/{direction}/{trip}/{date}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public List<TicketModel> downloadTickets(@PathParam("direction")String direction,
                                             @PathParam("trip") String trip,
                                             @PathParam("date")String date) {

        Date date2 = Date.valueOf(date);
        return ticketManager.downloadTickets(direction, trip, date2);
    }

    @POST
    @Path("sync")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUsedTicket(List<TicketModel> tickets) {
        int rows = ticketManager.syncTickets(tickets);
        if (rows > 0){
            return rows + " rows updated";
        } else {
            return "!update";
        }
    }

    /**
     * Gets the available tickets for a given date, departure station, arrival station and trip.
     * @param dateToCheck the date to check for
     * @param depStation the id of the departure station
     * @param arrStation the id of the arrival station
     * @param trip the id of the trip
     * @return
     */
    @GET
    @Path("available-tickets/{dateToCheck}/{depStation}/{arrStation}/{trip}")
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableTicketModel getAvailableTickets(@PathParam("dateToCheck") String dateToCheck,
                                                    @PathParam("depStation") int depStation,
                                                    @PathParam("arrStation") int arrStation,
                                                    @PathParam("trip") int trip) {

        return ticketManager.getAvailableTickets(stringToDate(dateToCheck), depStation, arrStation, trip);
    }


    /**
     * This generates a ticket, not yet validated nor saved on the database, just to demonstrate
     * the parameters to the client.
     * @param tripId the id of the trip chosen
     * @param startStationId  the id of the starting station
     * @param arrivalStationId the id of the arrival station
     * @return TicketModel the ticket, with id 0, no uniqueId, no ticketDate nor purchaseDate and also no isUsed flag
     */
    @GET
    @Path("gen-ticket-price/{tripId}/{startStationId}/{arrivalStationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TicketModel generateTicketPrice(@PathParam("tripId") int tripId,
                                           @PathParam("startStationId") int startStationId,
                                           @PathParam("arrivalStationId") int arrivalStationId) {
        return ticketManager.generateTicketPrice(tripId, startStationId, arrivalStationId);
    }


    /**
     * Get the tickets for the reviser to a certain trip on a certain date
     * @param dateToCheck the date to check for
     * @param tripId the id of the trip
     * @return List<TicketModel> the list of the tickets
     */
    @GET
    @Path("get-reviser-tickets/{dateToCheck}/{tripId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketModel> getAllTickets(@PathParam("dateToCheck") String dateToCheck,
                                           @PathParam("tripId") int tripId){

        return ticketManager.getAllTicket(stringToDate(dateToCheck), tripId);
    }

    /**
     * Returns all the active tickets available for the given user id
     * @param userId the id of the user
     * @return List<TicketModel> the active tickets of the user
     */
    @GET
    @Path("user-tickets/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketModel> getUserTickets(@PathParam("userId") int userId){
        return ticketManager.getUserTickets(userId);
    }

    /**
     * Method allowing and end user to buy a ticket exposed at /tickets/buy-ticket
     *
     * @return Json string stating the information about the ticket buyout
     */
    @POST
    @Path("buy-ticket")
    @Produces(MediaType.APPLICATION_JSON)
    public String buyTicket() {
        return "Not implemented exception!";
    }

    /**
     * Transforms a String to a SQL Date
     * @param dateToCheck the date in a string type
     * @return Date, the date in SQL format
     */
    private Date stringToDate(String dateToCheck) {
        Date sqlDate;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateToCheck);
            sqlDate = new Date(date.getTime());
            return sqlDate;
        } catch (ParseException e) {
            Main.getLogger().severe(e.getMessage());
            throw new ParseExceptionMapper(e.getMessage());
        }
    }
}
