package com.example.payment;

/**
 * Created by mercurius on 15/03/17.
 */

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This class serves the purpose of simulating a payment service.
 * Must reject invalid and outdated card numbers.
 */
@Path("payment")
public class PaymentController {

    /**
     * Concrete buyout operation
     * @return true if success false if no success
     */
    @POST
    @Path("buy-ticket")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean buyTicket(CreditCardModel creditCard){
        PaymentControllerValidator.setCreditCard(creditCard);
        return PaymentControllerValidator.validateCardNumber();
    }


}


