package com.example.dataHolder;

import com.example.models.TripModel;

import java.util.HashMap;

/**
 * Created by mercurius on 29/03/17.
 */
public class TripHolder {

    public static HashMap<Integer, TripModel> trips;

    public static HashMap<Integer, TripModel> getTrips() {
        return trips;
    }
    public static void setTrips(HashMap<Integer, TripModel> trips) {
        TripHolder.trips = trips;
    }
}
