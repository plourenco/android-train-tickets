package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

/**
 * Created by mercurius on 15/03/17.
 */

/**
 * This class serves the purpose of being a credit card model
 */
@XmlRootElement
public class CreditCardModel {

    @JsonProperty(value = "ccNumber")
    private String creditCardNumber;
    @JsonProperty(value = "expiryDate")
    private Date expiryDate;
    @JsonProperty(value = "cvv2")
    private String cvv2;

    public CreditCardModel() {}
    public CreditCardModel(String creditCardNumber, Date expiryDate, String cvv2) {
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.cvv2 = cvv2;
    }

    public String getCreditCardNumber() {

        return creditCardNumber;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public String getCvv2() {
        return cvv2;
    }
}
