package com.example.dataholder;

import com.example.models.SeatModel;

import java.util.HashMap;

/**
 * Created by mercurius on 30/03/17.
 */
public class SeatHolder {
    public static HashMap<Integer, SeatModel> seats;

    public static HashMap<Integer, SeatModel> getSeats() {
        return seats;
    }
    public static void setSeats(HashMap<Integer, SeatModel> seats) {
        SeatHolder.seats = seats;
    }
}
