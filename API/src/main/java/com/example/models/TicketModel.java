package com.example.models;

/**
 * Created by mercurius on 15/03/17.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

/**
 * This class serves the purpose of being the model of a train ticket.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class TicketModel {
    private int id;
    private UUID uniqueId;
    private int duration;
    private Time departureTime;
    private Time arrivalTime;
    private float price;
    private Date ticketDate;
    private Date purchaseDate;

    @JsonIgnore
    private boolean isUsed;

    private StationModel departureStation;
    private StationModel arrivalStation;
    private TripModel trip;
    private SeatModel seat;

    public StationModel getDepartureStation() {
        return departureStation;
    }

    public StationModel getArrivalStation() {
        return arrivalStation;
    }
    public java.sql.Date getTicketDate() {
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
    @JsonProperty(value = "isUsed")
    public boolean isUsed() {
        return isUsed;
    }

    public int getDuration() {
        return duration;
    }

    public Time getDepartureTime() {
        return departureTime;
    }
    public Time getArrivalTime() {
        return arrivalTime;
    }
    public SeatModel getSeat() {
        return seat;
    }
    public int getId() {
        return id;
    }
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public TicketModel() {

    }

    public TicketModel(int id, UUID uniqueId, StationModel departureStation, StationModel arrivalStation,
                       Date ticketDate, TripModel trip, boolean isUsed) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.trip = trip;
        this.isUsed = isUsed;
    }
    public TicketModel(int id, UUID uuid, java.sql.Date ticketDate, StationModel departureStation, StationModel arrivalStation) {
        this.id = id;
        this.uniqueId = uuid;
        this.ticketDate = ticketDate;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }
    public TicketModel(int id, UUID uuid, StationModel depStation, StationModel arrStation, Time departureTime,
                       Time arrivalTime, SeatModel seat, TripModel trip) {
        this.id = id;
        this.uniqueId = uuid;
        this.departureStation = depStation;
        this.arrivalStation = arrStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seat = seat;
        this.trip = trip;
    }
    public TicketModel(int id, UUID uniqueId, StationModel departureStation, StationModel arrivalStation,
                       java.sql.Date ticketDate, float price, Date purchaseDate, TripModel trip,
                       boolean isUsed, int duration, Time departureTime, Time arrivalTime) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.ticketDate = ticketDate;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.trip = trip;
        this.isUsed = isUsed;
        this.duration = duration;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
