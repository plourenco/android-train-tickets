package feup.cm.traintickets.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class TicketModel implements Serializable {

    private int id;
    private UUID uniqueId;
    private StationModel departureStation;
    private StationModel arrivalStation;
    private Date ticketDate;
    private float price;
    private Date purchaseDate;
    private TripModel trip;
    private boolean isUsed;
    private Time departureTime;
    private Time arrivalTime;
    private SeatModel seatModel;
    private int duration;

    /**
     * Constructors
     */
    public TicketModel(int id, UUID uniqueId, StationModel departureStation,
                       StationModel arrivalStation, Date ticketDate, float price, Date purchaseDate,
                       TripModel trip, boolean isUsed, Time departureTime, Time arrivalTime,
                       SeatModel seatModel, int duration) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.trip = trip;
        this.isUsed = isUsed;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatModel = seatModel;
        this.duration = duration;
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

    public TicketModel(int id, UUID uniqueId, StationModel depStation, StationModel arrStation,
                       Date ticketDate, boolean isUsed, TripModel trip) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.departureStation = depStation;
        this.arrivalStation = arrStation;
        this.ticketDate = ticketDate;
        this.isUsed = isUsed;
        this.trip = trip;
    }

    /**
     * Getters
     */
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
    public boolean getIsUsed() {
        return isUsed;
    }
    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime() {
        return arrivalTime;
    }
    public SeatModel getSeatModel() {
        return seatModel;
    }
    public int getDuration() {
        return duration;
    }
}
