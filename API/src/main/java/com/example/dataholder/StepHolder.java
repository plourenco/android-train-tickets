package com.example.dataHolder;

import com.example.models.StepModel;

import java.util.HashMap;

/**
 * Created by mercurius on 30/03/17.
 */
public class StepHolder {
    public static HashMap<Integer, StepModel> steps;

    public static HashMap<Integer, StepModel> getSteps() {
        return steps;
    }
    public static void setSteps(HashMap<Integer, StepModel> steps) {
        StepHolder.steps = steps;
    }
}
