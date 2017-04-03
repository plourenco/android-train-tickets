package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mercurius on 25/03/17.
 */
@XmlRootElement
public class AvailableTicketModel {
    private int sold;
    private int maxCapacity;

    public int getSold() {
        return sold;
    }
    public int getMaxCapacity()
    {
        return maxCapacity;
    }

    public AvailableTicketModel() {
    }
    public AvailableTicketModel(int sold, int maxCapacity) {
        this.sold = sold;
        this.maxCapacity = maxCapacity;
    }
}
