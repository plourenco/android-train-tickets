package com.example.tickets;

import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "tickets" path)
 */
@Path("tickets")
public class TicketController {

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
}
