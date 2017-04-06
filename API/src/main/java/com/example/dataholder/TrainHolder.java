package com.example.dataHolder;

import com.example.models.TrainModel;

import java.util.HashMap;

/**
 * Created by mercurius on 30/03/17.
 */
public class TrainHolder {
    public static HashMap<Integer, TrainModel> trains;

    public static HashMap<Integer, TrainModel> getTrains() {
        return trains;
    }
    public static void setTrains(HashMap<Integer, TrainModel> trains) {
        TrainHolder.trains = trains;
    }
}
