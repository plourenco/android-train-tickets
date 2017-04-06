package feup.cm.traintickets.models;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pedro on 26/03/17.
 */

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

    public int getId() {
        return id;
    }

    public UUID getUniqueId() {
        return uniqueId;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public TripModel getTrip() {
        return trip;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
