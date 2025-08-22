//package za.ac.cput.service.payment;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.payment.Payment;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.factory.payment.PaymentFactory;
//import za.ac.cput.service.booking.BookingService;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import za.ac.cput.domain.booking.CleaningService;
//
//import za.ac.cput.service.booking.CleaningServiceService;
//
//
//@SpringBootTest
//public class PaymentServiceTest {
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    private BookingService bookingService;
//
//    private static Booking createdBooking;
//
//    private static Payment createdPayment;
//
//    @Autowired
//    private CleaningServiceService cleaningServiceService;
//
//    // Use an existing booking ID from your DB here:
//    private static final String EXISTING_BOOKING_ID = "5fc0a12d-564c-48de-8f17-ee514e5663d5";
//
//    @Test
//    @Order(1)
//    void testCreatePaymentWithExistingBooking() {
//        Booking existingBooking = bookingService.read(EXISTING_BOOKING_ID);
//        assertNotNull(existingBooking);
//
//        Payment payment = paymentService.createPaymentForBookingWithTip(
//                existingBooking.getBookingID());
//
//        assertNotNull(payment);
//        assertEquals(existingBooking.getBookingID(), payment.getBooking().getBookingID());
//
//        double expectedAmount = existingBooking.isTipAdd()
//                ? existingBooking.getBookingCost() * 1.10
//                : existingBooking.getBookingCost();
//        assertEquals(expectedAmount, payment.getPaymentAmount());
//
//        createdPayment = payment; // save for CRUD tests
//    }
//
//    @Test
//    @Order(2)
//    void testCreatePaymentWithNewBooking() {
//
//        // Step 1: Prepare cleaning services (fetch from DB or create new)
//        List<CleaningService> services = new ArrayList<>();
//
//        // Example: fetch existing cleaning services from BookingService or CleaningServiceService (assuming you have one)
//        // Replace with your actual service fetching logic
//        CleaningService service1 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.CERAMIC_COATING);
//        CleaningService service2 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.INTERIOR_CLEANING);
//
//        // Add fetched services to list (ensure they are not null)
//        if (service1 != null) services.add(service1);
//        if (service2 != null) services.add(service2);
//
//        double calculatedBookingCost = services.stream()
//                .mapToDouble(CleaningService::getPriceOfService)
//                .sum();
//
//
//        createdBooking = BookingFactory.createBooking(
//                services,
//                null,
//                "WA_TEST_010",
//                LocalDateTime.now().plusDays(1),
//                "VEHICLE_TEST_001",
//                true,
//                calculatedBookingCost
//        );
//
//        createdBooking = bookingService.create(createdBooking);
//        assertNotNull(createdBooking);
//
//        Payment payment = paymentService.createPaymentForBookingWithTip(
//                createdBooking.getBookingID());
//
//        assertNotNull(payment);
//        assertEquals(createdBooking.getBookingID(), payment.getBooking().getBookingID());
//
//        double expectedAmount = createdBooking.getBookingCost() * 1.10;
//        assertEquals(expectedAmount, payment.getPaymentAmount());
//    }
//
//    private Payment createTestPayment() {
//        // Use existing booking for simplicity here:
//        Booking booking = bookingService.read(EXISTING_BOOKING_ID);
//        if (booking == null) {
//            // Or create a new booking here if needed
//            List<CleaningService> services = new ArrayList<>();
//            CleaningService service1 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.CERAMIC_COATING);
//            CleaningService service2 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.INTERIOR_CLEANING);
//            if (service1 != null) services.add(service1);
//            if (service2 != null) services.add(service2);
//            double bookingCost = services.stream().mapToDouble(CleaningService::getPriceOfService).sum();
//
//            booking = BookingFactory.createBooking(
//                    services,
//                    null,
//                    "WA_TEST_011",
//                    LocalDateTime.now().plusDays(1),
//                    "VEHICLE_TEST_011",
//                    true,
//                    bookingCost
//            );
//            booking = bookingService.create(booking);
//        }
//        return paymentService.createPaymentForBookingWithTip(booking.getBookingID());
//    }
//
//    @Test
//    @Order(3)
//    void testCreate() {
//
//        System.out.println("Running testCreate...");
//        Payment testPayment = createTestPayment(); // get fresh test payment
//        Payment saved = paymentService.create(testPayment);
//        assertNotNull(saved);
//        assertEquals(testPayment.getBooking().getBookingID(), saved.getBooking().getBookingID());
//    }
//
//    @Test
//    @Order(4)
//    void testRead() {
//        System.out.println("Running testRead...");
//        Payment testPayment = createTestPayment();
//        Payment saved = paymentService.create(testPayment);
//        Payment read = paymentService.read(saved.getPaymentID());
//        assertNotNull(read);
//        assertEquals(saved.getPaymentID(), read.getPaymentID());
//    }
//
//    @Test
//    @Order(5)
//
//    void testUpdate() {
//
//        System.out.println("Running testUpdate...");
//        Payment testPayment = createTestPayment();
//        Payment saved = paymentService.create(testPayment);
//
//        Payment updated = new Payment.Builder()
//                .copy(saved)
//                .setPaymentAmount(999.99)
//                .setPaymentStatus(Payment.PaymentStatus.PAID)
//                .build();
//
//        Payment result = paymentService.update(updated);
//        assertEquals(999.99, result.getPaymentAmount());
//        assertEquals(Payment.PaymentStatus.PAID, result.getPaymentStatus());
//    }
//
////    @Test
////    @Order(6)
////
////    void testDelete() {
////        System.out.println("Running testDelete...");
////        Payment testPayment = createTestPayment();
////        Payment saved = paymentService.create(testPayment);
////
////        boolean deleted = paymentService.delete(saved.getPaymentID());
////        assertTrue(deleted);
////        assertNull(paymentService.read(saved.getPaymentID()));
////    }
//
//    @Test
//    @Order(7)
//
//    void testGetAll() {
//        System.out.println("Running testGetAll...");
//        Payment testPayment = createTestPayment();
//        paymentService.create(testPayment);
//
//        List<Payment> allPayments = paymentService.getAll();
//        assertFalse(allPayments.isEmpty());
//    }
//
//}
