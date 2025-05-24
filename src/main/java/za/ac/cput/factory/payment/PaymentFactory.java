package za.ac.cput.factory.payment;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.util.Helper;

public class PaymentFactory {


    public static Payment createPaymentFactory1(Booking booking, double paymentAmount, Payment.PaymentMethod paymentMethod, Payment.PaymentStatus paymentStatus) {

        String paymentID = Helper.generateID();
        String bookingID = booking.getBookingID();

        if( !Helper.validatePrice(paymentAmount)
         || !Helper.isValidEnumValue(paymentMethod, Payment.PaymentMethod.class)
        || !Helper.isValidEnumValue(paymentStatus, Payment.PaymentStatus.class)) {
            return null;
        } else {
            return new Payment.Builder()
                    .setPaymentID(paymentID)
                    .setBookingID(bookingID)
                    .setPaymentAmount(paymentAmount)
                    .setPaymentMethod(paymentMethod)
                    .setPaymentStatus(paymentStatus)
                    .build();
        }


    }
}
