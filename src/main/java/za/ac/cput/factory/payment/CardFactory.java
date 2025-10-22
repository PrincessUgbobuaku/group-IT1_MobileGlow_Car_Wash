package za.ac.cput.factory.payment;

import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.util.Helper;

import java.time.YearMonth;

public class CardFactory {

    /**
     * Creates a Card without a linked Customer.
     * Useful for quick tests or system-assigned cards.
     */
    public static Card createCard(String cardNumber,
                                  String cardHolderName,
                                  String cvv,
                                  YearMonth expiryDate) {
        return createCard(cardNumber, cardHolderName, cvv, expiryDate, null);
    }

    /**
     * Creates a Card optionally linked to a specific Customer.
     */
    public static Card createCard(String cardNumber,
                                  String cardHolderName,
                                  String cvv,
                                  YearMonth expiryDate,
                                  Customer customer) {

        if (!Helper.isValidString(cardHolderName)
                || !Helper.isValidString(cardNumber)
                || !Helper.isValidString(cvv)
                || !Helper.isValidCardNumber(cardNumber)
                || !Helper.passesLuhnCheck(cardNumber)
                || !Helper.isValidCVV(cvv)
                || !Helper.isValidExpiryDate(expiryDate)) {
            System.out.println("⚠️ Invalid card creation attempt detected in CardFactory.");
            return null;
        }

        Card card = new Card.Builder()
                .setCardNumber(cardNumber.trim())
                .setCardHolderName(cardHolderName.trim())
                .setCvv(cvv.trim())
                .setExpiryDate(expiryDate)
                .setCustomer(customer)
                .build();

        System.out.println("✅ CardFactory created card: " + card);
        return card;
    }
}