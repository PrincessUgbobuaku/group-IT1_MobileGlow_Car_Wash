package za.ac.cput.factory.payment;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.util.Helper;

public class PaymentFactory {


    public static Payment createPayment(Booking booking, double paymentAmount, Payment.PaymentMethod paymentMethod, Payment.PaymentStatus paymentStatus) {

        if (!Helper.isValidDouble(paymentAmount)
                || !Helper.isValidEnumValue(paymentMethod, Payment.PaymentMethod.class)
                || !Helper.isValidEnumValue(paymentStatus, Payment.PaymentStatus.class)) {
            return null;
        } else {
            return new Payment.Builder()
                    .setBooking(booking)
                    .setPaymentAmount(paymentAmount)
                    .setPaymentMethod(paymentMethod)
                    .setPaymentStatus(paymentStatus)
                    .build();
        }
    }
}
