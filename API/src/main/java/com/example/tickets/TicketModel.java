package com.example.tickets;

/**
 * Created by mercurius on 15/03/17.
 */

import com.example.station.StationModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

/**
 * This class serves the purpose of being the model of a train ticket.
 */
@XmlRootElement
public class TicketModel {
    private int ticketID;
    private UUID ticketUniqueID;
    private StationModel departureStation;
    private StationModel arrivalStation;
    private Date ticketDate;
    private float ticketPrice;
    private int distance;

    /**
     * Seat on the train
     */
    private int seat;

    /**
     * Eventually we will need something here to generate/hold the QR Code
     */

    public int getTicketID() {
        return ticketID;
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
    public float getTicketPrice() {
        return ticketPrice;
    }
    public int getDistance() {
        return distance;
    }
    public int getSeat() {
        return seat;
    }
    public UUID getTicketUniqueID() {
        return ticketUniqueID;
    }

    public TicketModel(int ticketID, StationModel departureStation, StationModel arrivalStation, Date ticketDate, float ticketPrice, int distance, int seat, UUID ticketUniqueID) {
        this.ticketID = ticketID;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.ticketPrice = ticketPrice;
        this.distance = distance;
        this.seat = seat;
        this.ticketUniqueID = ticketUniqueID;
    }
}
