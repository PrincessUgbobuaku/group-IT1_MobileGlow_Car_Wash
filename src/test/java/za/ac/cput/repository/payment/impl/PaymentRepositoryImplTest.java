package za.ac.cput.repository.payment.impl;
//
//
//import org.junit.jupiter.api.*;
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.domain.payment.Payment;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.factory.booking.CleaningServiceFactory;
//import za.ac.cput.factory.payment.PaymentFactory;
//import za.ac.cput.repository.payment.IPaymentRepository;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//
//public class PaymentRepositoryImplTest {
//
//    private static IPaymentRepository repository = PaymentRepositoryImpl.getRepository();
//
//    private static CleaningService cs1;
//    private static CleaningService cs2;
//    private static List<CleaningService> services;
//    private static Booking booking1;
//    private static Payment payment1;
//
//    @BeforeAll
//    static void setup() {
//        cs1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 230.00, 2.5);
//
//        cs2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 500.00, 1);
//
//        services = new ArrayList<>();
//        services.add(cs1);
//        services.add(cs2);
//
//        booking1 = BookingFactory.createBooking(
//                services, "wa_1", LocalDateTime.of(2025, 10, 30, 12, 0), "vehicle1", true, 500);
//
//        payment1 = PaymentFactory.createPayment(
//                booking1, 500.00, Payment.PaymentMethod.CREDIT, Payment.PaymentStatus.PENDING);
//    }
//
//    @Test
//    @Order(1)
//    void create() {
//        Payment created = repository.create(payment1);
//        assertNotNull(created);
//        System.out.println("Created Payment: " + created);
//    }
//
//    @Test
//    @Order(2)
//    void read() {
//        Payment read = repository.read(payment1.getPaymentID());
//        assertNotNull(read);
//        System.out.println("Read Payment: " + read);
//    }
//
//    @Test
//    @Order(3)
//    void update() {
//        Payment updatedPayment = new Payment.Builder().copy(payment1)
//                .setPaymentAmount(600.00)
//                .build();
//
//        Payment updated = repository.update(updatedPayment);
//        assertNotNull(updated);
//        assertEquals(600.00, updated.getPaymentAmount());
//        System.out.println("Updated Payment: " + updated);
//    }
//
//    @Test
//    @Order(4)
//    void delete() {
//        boolean success = repository.delete(payment1.getPaymentID());
//        assertTrue(success);
//        System.out.println("Deleted Payment with ID: " + payment1.getPaymentID());
//    }
//
//    @Test
//    @Order(5)
//    void getAll() {
//        System.out.println("All Payments: " + repository.getAll());
//    }
//
//}
