package com.example.bookings;

import com.example.seats.SeatModel;
import com.example.tickets.TicketModel;
import com.example.trips.TripModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by mercurius on 22/03/17.
 */
@XmlRootElement
public class BookingModel {
    private int id;
    private Date dateBooking;
    private TicketModel ticket;
    private SeatModel seat;
    private TripModel trip;

    /**
     * Getters
     */
    public int getId() {
        return id;
    }
    public Date getDateBooking() {
        return dateBooking;
    }
    public TicketModel getTicket() {
        return ticket;
    }
    public SeatModel getSeat() {
        return seat;
    }
    public TripModel getTrip() {
        return trip;
    }

    /**
     * Constructors
     */
    public BookingModel() {
    }
    public BookingModel(int id, Date dateBooking, TicketModel ticket, SeatModel seat, TripModel trip) {
        this.id = id;
        this.dateBooking = dateBooking;
        this.ticket = ticket;
        this.seat = seat;
        this.trip = trip;
    }
}
