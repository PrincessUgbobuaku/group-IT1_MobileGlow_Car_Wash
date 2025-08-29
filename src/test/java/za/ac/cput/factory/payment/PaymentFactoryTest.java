//package za.ac.cput.factory.payment;
//
//import org.junit.jupiter.api.Test;  // JUnit 5 Test annotation
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.domain.payment.Payment;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.factory.booking.CleaningServiceFactory;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class PaymentFactoryTest {
//
//    @Test
//    public void testCreatePaymentForExistingBooking() {
//        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 300, 1.5);
//        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.CERAMIC_COATING, 700, 2.0);
//
//        List<CleaningService> booking1ListOfServices = new ArrayList<>();
//
//        booking1ListOfServices.add(cs_1);
//        booking1ListOfServices.add(cs_2);
//
//        Booking booking = BookingFactory.createBooking(
//                booking1ListOfServices,
//                null,
//                "WA101",
//                LocalDateTime.now().plusHours(4),
//                "VEHICLE102",
//                false,
//                250
//        );
//
//        assertNotNull(booking);
//
//        Payment payment = PaymentFactory.createPayment(
//                booking,
//                250,
//                Payment.PaymentMethod.DEBIT,
//                Payment.PaymentStatus.PAID
//        );
//
//        assertNotNull(payment);
//        assertEquals(booking.getBookingID(), payment.getBooking().getBookingID());
//        assertEquals(250, payment.getPaymentAmount());
//
//        System.out.println("Payment linked to booking:\n" + payment);
//    }
//
//    @Test
//    public void testCreateMultiplePaymentsForOneBooking() {
//        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 600, 2.0);
//
//        List<CleaningService> booking2ListOfServices = new ArrayList<>();
//
//        booking2ListOfServices.add(cs_1);
//
//        Booking booking = BookingFactory.createBooking(
//                booking2ListOfServices,
//                null,
//                "WA002",
//                LocalDateTime.now().plusDays(1),
//                "VEHICLE002",
//                true,
//                600
//        );
//
//        assertNotNull(booking);
//
//        Payment payment1 = PaymentFactory.createPayment(
//                booking,
//                300,
//                Payment.PaymentMethod.DEBIT,
//                Payment.PaymentStatus.PENDING
//        );
//
//        Payment payment2 = PaymentFactory.createPayment(
//                booking,
//                300,
//                Payment.PaymentMethod.PAYPAL,
//                Payment.PaymentStatus.PENDING
//        );
//
//        assertNotNull(payment1);
//        assertNotNull(payment2);
//
//        assertEquals(booking.getBookingID(), payment1.getBooking().getBookingID());
//        assertEquals(booking.getBookingID(), payment2.getBooking().getBookingID());
//
//        System.out.println("Multiple payments for a single booking:\n" + payment1 + "\n" + payment2);
//    }
//
//}
//
//
//
////    @Test
////    public void testCreatePayment(){
////        CleaningService cs_1 = new CleaningService.Builder() //cleaning service 1 of booking 1
////                .setCleaningServiceID("cs_1")
////                .setServiceName(CleaningService.ServiceName.WAXING_AND_POLISHING)
////                .setPriceOfService(230.00)
////                .setDuration(2.5)
////                .build();
////
////        CleaningService cs_2 = new CleaningService.Builder() //cleaning service 2 of booking 1
////                .setCleaningServiceID("cs_2")
////                .setServiceName(CleaningService.ServiceName.INTERIOR_CLEANING)
////                .setPriceOfService(500.00)
////                .setDuration(1)
////                .build();
////
////        List<CleaningService> booking1ListOfServices = new ArrayList<>();
////
////        booking1ListOfServices.add(cs_1);
////        booking1ListOfServices.add(cs_2);
////
////        Booking booking1 = BookingFactory.createBooking(booking1ListOfServices, "wa_1", (LocalDateTime.of(2025, 10, 30, 12, 0)), "vehicle1", true, 500);
////
////        Payment payment1 = PaymentFactory.createPayment(booking1, 500.00, Payment.PaymentMethod.CREDIT, Payment.PaymentStatus.PENDING);
////
////        assertNotNull(payment1, "Payment object should not be null");
////
////        System.out.println("Payment: " + payment1);
//
//        // --- Assertions ---
//        // Check that the payment object was created successfully
////        // Check that the booking ID in the payment matches the booking ID of the booking object
////        assertEquals("Booking ID in payment should match booking1's ID", booking1.getBookingID(), payment1.getBookingID());
////        // Check payment amount
////        assertEquals("Payment amount should be 500.00", 500.00, payment1.getPaymentAmount(), 0.001); // Use delta for double comparison
////
////        // Check that a payment ID was generated
////        assertNotNull("Payment ID should not be null", payment1.getPaymentID());
////
////        System.out.println("Payment created successfully: " + payment1.toString());
//    //}
//
//
