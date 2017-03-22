package com.example.tickets;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.station.StationModel;
import com.example.users.UserModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

/**
 * This class serves the purpose of being the model of a train ticket.
 */
@XmlRootElement
public class TicketModel {
    private int idTicket;
    private UUID uniqueId;
    private StationModel departureStation;
    private StationModel arrivalStation;
    private Date ticketDate;
    private float price;
    private int distance;
    private Date purchaseDate;
    private UserModel user;

    /**
     * Seat on the train
     */
    private int seat;

    /**
     * Eventually we will need something here to generate/hold the QR Code
     */

    public int getIdTicket() {
        return idTicket;
    }
    public StationModel getDepartureStation() {
        return departureStation;
    }
    public StationModel getArrivalStation() {
        return arrivalStation;
    }
    public Date getTicketDate() {
        return ticketDate;
    }
    public float getPrice() {
        return price;
    }
    public int getDistance() {
        return distance;
    }
    public int getSeat() {
        return seat;
    }
    public UUID getUniqueId() {
        return uniqueId;
    }
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public UserModel getUser() {
        return user;
    }

    public TicketModel(int ticketID, StationModel departureStation, StationModel arrivalStation, Date ticketDate,
                       float price, int distance, int seat, UUID uniqueId, Date purchaseDate, UserModel user) {
        this.idTicket = ticketID;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.price = price;
        this.distance = distance;
        this.seat = seat;
        this.uniqueId = uniqueId;
        this.purchaseDate = purchaseDate;
        this.user = user;
    }
}
