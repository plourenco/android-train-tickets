package feup.cm.traintickets.models;

import java.util.Date;

public class CreditCardModel {
    private String ccNumber;
    private String cvv2;
    private Date expiryDate;

    public CreditCardModel(String ccNumber, String cvv2, Date expiryDate) {
        this.ccNumber = ccNumber;
        this.cvv2 = cvv2;
        this.expiryDate = expiryDate;
    }

    public String getCcNumber() {
        return ccNumber;
    }
    public String getCvv2() {
        return cvv2;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
}
