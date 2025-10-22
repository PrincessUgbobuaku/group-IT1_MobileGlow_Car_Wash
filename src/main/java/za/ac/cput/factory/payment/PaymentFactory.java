package za.ac.cput.factory.payment;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.util.Helper;

public class PaymentFactory {

    /**
     * Creates a Payment object, optionally linking a Card if paymentMethod == CARD.
     * Validates all inputs and ensures that a card payment is always linked to a Customer.
     */
    public static Payment createPayment(
            Booking booking,
            double paymentAmount,
            Payment.PaymentMethod paymentMethod,
            Payment.PaymentStatus paymentStatus,
            Card card // nullable; only used if paymentMethod == CARD
    ) {
        System.out.println("[DEBUG] PaymentFactory.createPayment() called");

        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }

        if (!Helper.isValidDouble(paymentAmount) || paymentAmount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount: " + paymentAmount);
        }

        if (!Helper.isValidEnumValue(paymentMethod, Payment.PaymentMethod.class)) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }

        if (!Helper.isValidEnumValue(paymentStatus, Payment.PaymentStatus.class)) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentStatus);
        }

        // 🧾 Validate card if payment method is CARD
        if (paymentMethod == Payment.PaymentMethod.CARD) {
            if (card == null) {
                throw new IllegalArgumentException("Card payment must include a valid Card object.");
            }

            if (card.getCustomer() == null || card.getCustomer().getUserId() == null) {
                throw new IllegalArgumentException("Card must be linked to a valid Customer before creating a payment.");
            }
        }

        // ✅ Build the Payment object
        Payment.Builder builder = new Payment.Builder()
                .setBooking(booking)
                .setPaymentAmount(paymentAmount)
                .setPaymentMethod(paymentMethod)
                .setPaymentStatus(paymentStatus);

        // ✅ Attach card only for card payments
        if (paymentMethod == Payment.PaymentMethod.CARD) {
            builder.setCard(card);
            System.out.println("[DEBUG] Linked card ID: " + card.getCardId() +
                    " (Customer ID: " + (card.getCustomer() != null ? card.getCustomer().getUserId() : "null") + ")");
        }

        Payment payment = builder.build();
        System.out.println("[DEBUG] Payment created successfully for booking ID: " + booking.getBookingId());
        return payment;
    }

    /**
     * Overload: Creates a payment without a card (for cash/EFT/etc.)
     */
    public static Payment createPayment(
            Booking booking,
            double paymentAmount,
            Payment.PaymentMethod paymentMethod,
            Payment.PaymentStatus paymentStatus
    ) {
        return createPayment(booking, paymentAmount, paymentMethod, paymentStatus, null);
    }
}