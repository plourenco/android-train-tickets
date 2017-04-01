package com.example.util;


import com.example.exceptions.InvalidCreditCardException;
import com.example.models.CreditCardModel;

import java.util.Date;

/**
 * Called to validate a credit card when user requests to buy a ticket.
 * <ul>
 *     <li>If creditCard is null throw NullPointerException</li>
 *     <li>If creditCard cvv2 is null or empty throw InvalidCreditCardException</li>
 *     <li>If creditCard cvv2 length is different from 3 throw InvalidCreditCardException</li>
 *     <li>If creditCard date has expired throw InvalidCreditCardException</li>
 * </ul>
 */
public class PaymentControllerValidator {

    private static CreditCardModel creditCard;

    /**
     * Sets the credit card to be validated
     * @param creditCard
     */
    public static void setCreditCard(CreditCardModel creditCard) {
        PaymentControllerValidator.creditCard = creditCard;
    }

    /**
     * Using LUHN's algorithm validates if the credit card number is valid or invalid
     * @return boolean based on algorithm outcome
     */
    public static boolean validateCardNumber() {

        // creditCard instance is null
        if (creditCard == null) { throw new NullPointerException("Credit card property not set in PaymentControllerValidator"); }

        // creditCard cvv2 is null or empty
        if (creditCard.getCvv2() == null || creditCard.getCvv2().isEmpty()) { throw new InvalidCreditCardException("CVV2 is empty"); }

        // creditCard cvv2 length differs from 3
        if (creditCard.getCvv2().length() != 3) { throw new InvalidCreditCardException("CVV2 is invalid"); }

        // creditCard has an expired date
        if (creditCard.getExpiryDate().before(new Date())) { throw new InvalidCreditCardException("Credit card has expired"); }

        int sum = 0;
        boolean alternate = false;
        for (int i = creditCard.getCreditCardNumber().length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(creditCard.getCreditCardNumber().substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) { n = (n % 10) + 1; }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
