package com.example.tickets;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.station.StationModel;
import com.example.trips.TripModel;
import com.example.users.UserModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

/**
 * This class serves the purpose of being the model of a train ticket.
 */
@XmlRootElement
public class TicketModel {
    private int id;
    private UUID uniqueId;
    private StationModel departureStation;
    private StationModel arrivalStation;
    private Date ticketDate;
    private float price;
    private Date purchaseDate;
    private TripModel trip;
    private boolean isUsed;


    /**
     * Eventually we will need something here to generate/hold the QR Code
     */
    public int getId() {
        return id;
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
    public UUID getUniqueId() {
        return uniqueId;
    }
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public TripModel getTrip() {
        return trip;
    }
    public boolean isUsed() {
        return isUsed;
    }

    public TicketModel() {
    }
    public TicketModel(int id, UUID uniqueId, StationModel departureStation, StationModel arrivalStation,
                       Date ticketDate, float price, Date purchaseDate, TripModel trip,
                       boolean isUsed) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.trip = trip;
        this.isUsed = isUsed;
    }
}
