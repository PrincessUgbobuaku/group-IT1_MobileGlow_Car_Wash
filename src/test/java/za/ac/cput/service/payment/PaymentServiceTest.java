package za.ac.cput.service.payment;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.payment.PaymentFactory;
import za.ac.cput.service.booking.BookingService;
import za.ac.cput.service.booking.CleaningServiceService;
import za.ac.cput.service.booking.VehicleService;
import za.ac.cput.service.user.employee.WashAttendantService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CleaningServiceService cleaningServiceService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private WashAttendantService washAttendantService;

    private static Payment savedPayment;
    private static Booking booking;
    private static Vehicle vehicle;
    private static WashAttendant washAttendant;

    @BeforeAll
    static void init() {
        System.out.println("🔧 PaymentServiceTest starting...");
    }

    @Test
    @Rollback(value = false)
    @Order(0)
    void setupEntities() {
//        // Fetch an existing vehicle from DB (ensure this ID exists in your database)
//        vehicle = vehicleService.read(64L);  // Assuming ID 62 exists in the DB
//        assertNotNull(vehicle, "Vehicle should exist in the database");
//
//        // Fetch an existing wash attendant (ensure this ID exists in DB)
//        washAttendant = washAttendantService.read(59L);  // Assuming ID 59 exists in the DB
//        assertNotNull(washAttendant, "WashAttendant should exist in the database");
    }

    @Test
    @Rollback(value = false)
    @Order(1)
    void testCreatePaymentWithExistingBooking() {
        // Fetch an existing booking from DB
        booking = bookingService.read(36L); // Use real booking ID
        assertNotNull(booking, "Booking should exist");

        // 💡 Force loading of cleaning services (avoids lazy init errors)
        assertNotNull(booking.getCleaningServices());
        booking.getCleaningServices().size(); // Initializes the collection

        // Now create the payment
        Payment payment = paymentService.createPaymentForBookingWithTip(booking.getBookingID());
        assertNotNull(payment);
        assertEquals(booking.getBookingID(), payment.getBooking().getBookingID());

        double expectedAmount = booking.isTipAdd()
                ? booking.getBookingCost() * 1.10
                : booking.getBookingCost();

        assertEquals(expectedAmount, payment.getPaymentAmount(), 0.01);
        savedPayment = payment;

        System.out.println("✅ Created Payment: " + payment);  // Safe to log now
    }

    @Test
    @Rollback(value = false)
    @Order(2)
    void testCreate() {
        assertNotNull(savedPayment, "Payment should have been created in the previous test");

        // Save the payment again to confirm successful creation
        Payment result = paymentService.create(savedPayment);
        assertNotNull(result);
        assertEquals(savedPayment.getBooking().getBookingID(), result.getBooking().getBookingID());

        System.out.println("✅ Created Payment (again): " + result);
    }

    @Test
    @Rollback(value = false)
    @Order(3)
    void testRead() {
        assertNotNull(savedPayment, "Payment should have been created in the previous test");

        // Read the payment and check if it's the same
        Payment readPayment = paymentService.read(savedPayment.getPaymentID());
        assertNotNull(readPayment);
        assertEquals(savedPayment.getPaymentID(), readPayment.getPaymentID());

        System.out.println("✅ Read Payment: " + readPayment);
    }

//    @Test
//    @Rollback(value = false)
//    @Order(4)
//    void testUpdate() {
//        assertNotNull(savedPayment, "Payment should have been created in the previous test");
//
//        // Update the payment (e.g., changing the amount or status)
//        Payment updatedPayment = new Payment.Builder()
//                .copy(savedPayment)
//                .setPaymentAmount(150.00)  // Adjusted amount for the update
//                .setPaymentStatus(Payment.PaymentStatus.PAID)
//                .setPaymentMethod(Payment.PaymentMethod.CREDIT)// Changed status
//                .build();
//
//        Payment result = paymentService.update(updatedPayment);
//        assertNotNull(result);
//        assertEquals(150.00, result.getPaymentAmount());
//        assertEquals(Payment.PaymentStatus.PAID, result.getPaymentStatus()); // ✅
//
//        System.out.println("✅ Updated Payment: " + result);
//    }

    @Test
    @Rollback(value = false)
    @Order(5)
    void testGetAll() {
        // Fetch all payments
        List<Payment> payments = paymentService.getAll();
        assertNotNull(payments, "Payment list should not be null");
        assertFalse(payments.isEmpty(), "There should be at least one payment");

        System.out.println("✅ All Payments: " + payments);
    }

//    @Test
//    @Order(6)
//    void testDelete() {
//        assertNotNull(savedPayment, "Payment should have been created in the previous test");
//
//        // Delete the payment
//        boolean deleted = paymentService.delete(savedPayment.getPaymentID());
//        assertTrue(deleted);
//
//        // Verify it has been deleted
//        Payment afterDelete = paymentService.read(savedPayment.getPaymentID());
//        assertNull(afterDelete);
//        System.out.println("✅ Payment deleted successfully");
//    }
}