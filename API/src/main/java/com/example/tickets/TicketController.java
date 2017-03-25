package com.example.tickets;

import com.example.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Root resource (exposed at "tickets" path)
 */
@Path("tickets")
public class TicketController {

    private TicketManager ticketManager = new TicketManager();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
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
        if (ticketManager == null) { ticketManager = new TicketManager(); }
        Date sqlDate = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(dateToCheck);
            sqlDate = new Date(date.getTime());
        } catch (ParseException e) {
            Main.getLogger().severe(e.getMessage());
            return null;
        }
        return ticketManager.getAvailableTickets(sqlDate, depStation, arrStation, trip);
    }
}
