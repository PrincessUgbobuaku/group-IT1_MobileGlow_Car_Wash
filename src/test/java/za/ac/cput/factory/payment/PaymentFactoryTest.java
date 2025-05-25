package za.ac.cput.factory.payment;

import org.junit.Test;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.factory.booking.BookingFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentFactoryTest {

    @Test
    public void testPaymentFactory1(){
        CleaningService cs_1 = new CleaningService.Builder() //cleaning service 1 of booking 1
                .setCleaningServiceID("cs_1")
                .setServiceName(CleaningService.ServiceName.WAXING_AND_POLISHING)
                .setPriceOfService(230.00)
                .setDuration(2.5)
                .build();

        CleaningService cs_2 = new CleaningService.Builder() //cleaning service 2 of booking 1
                .setCleaningServiceID("cs_2")
                .setServiceName(CleaningService.ServiceName.INTERIOR_CLEANING)
                .setPriceOfService(500.00)
                .setDuration(1)
                .build();

        List<CleaningService> booking1ListOfServices = new ArrayList<>();

        booking1ListOfServices.add(cs_1);
        booking1ListOfServices.add(cs_2);

        Booking booking1 = BookingFactory.createBookingFactory1(booking1ListOfServices, "wa_1", (LocalDateTime.of(2025, 5, 30, 12, 0)), "vehicle1", true, 500);

        Payment payment1 = PaymentFactory.createPaymentFactory1(booking1, 500.00, Payment.PaymentMethod.CREDIT, Payment.PaymentStatus.PENDING);

        // --- Assertions ---
        // Check that the payment object was created successfully
        assertNotNull("Payment object should not be null", payment1);
//        // Check that the booking ID in the payment matches the booking ID of the booking object
//        assertEquals("Booking ID in payment should match booking1's ID", booking1.getBookingID(), payment1.getBookingID());
//        // Check payment amount
//        assertEquals("Payment amount should be 500.00", 500.00, payment1.getPaymentAmount(), 0.001); // Use delta for double comparison
//
//        // Check that a payment ID was generated
//        assertNotNull("Payment ID should not be null", payment1.getPaymentID());
//
//        System.out.println("Payment created successfully: " + payment1.toString());
    }

}
