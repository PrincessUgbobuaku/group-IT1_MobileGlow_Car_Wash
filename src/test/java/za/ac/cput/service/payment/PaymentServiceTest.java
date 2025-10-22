package za.ac.cput.service.payment;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.factory.payment.CardFactory;
import za.ac.cput.service.booking.BookingService;

import java.time.YearMonth;
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
    private CardService cardService;

    private static Payment savedPayment;
    private static Booking booking;
    private static Card testCard;

    @BeforeAll
    static void init() {
        System.out.println("🔧 PaymentServiceTest starting...");
    }

    @Test
    @Order(1)
    @Rollback(false)
    void testSetupBookingAndCard() {
        System.out.println("⚙️ Setting up Booking and Card...");

        booking = bookingService.read(6L); // ✅ Ensure booking with ID 6 exists
        assertNotNull(booking, "Booking should exist in the DB for test setup.");

        // Ensure booking has a customer
        Customer customer = booking.getVehicle().getCustomer();
        assertNotNull(customer, "Booking must have an associated customer.");

        // Force-load related collections
        assertNotNull(booking.getCleaningServices());
        booking.getCleaningServices().size();

        // ✅ Create and link valid card
        testCard = CardFactory.createCard(
                "4242424242424242",
                customer.getUserName() + " " + customer.getUserSurname(),
                "123",
                YearMonth.of(2026, 12));
        assertNotNull(testCard, "Card should be created via factory.");

        // ✅ Link card to booking’s customer
        testCard.setCustomer(customer);

        // ✅ Persist the card
        testCard = cardService.create(testCard);
        assertNotNull(testCard.getCardId(), "Card should be saved and have an ID.");

        System.out.println("📇 Card created and linked to Customer: " + testCard);
    }

    @Test
    @Order(2)
    @Rollback(false)
    void testCreatePaymentWithTipAndCard() {
        System.out.println("💳 Creating Payment with Card...");

        assertNotNull(booking, "Booking must be initialized.");
        assertNotNull(testCard, "Card must be initialized.");

        double tipAmount = booking.isTipAdd()
                ? booking.getBookingCost() * 1.10
                : booking.getBookingCost();

        Payment payment = new Payment.Builder()
                .setBooking(booking)
                .setPaymentMethod(Payment.PaymentMethod.CARD)
                .setPaymentStatus(Payment.PaymentStatus.PAID)
                .setPaymentAmount(tipAmount)
                .setCard(testCard)
                .build();

        Payment result = paymentService.create(payment);

        assertNotNull(result, "Payment should be created.");
        assertEquals(tipAmount, result.getPaymentAmount(), 0.01);
        assertEquals(booking.getBookingId(), result.getBooking().getBookingId());
        assertEquals(testCard.getCardId(), result.getCard().getCardId(), "Card ID must match.");

        savedPayment = result;
        System.out.println("💰 Payment with card created successfully: " + result);
    }

    @Test
    @Order(7)
    @Rollback(false)
    void testDuplicatePaymentCreationAllowed() {
        assertNotNull(savedPayment, "Saved payment should exist");

        // ✅ Use the existing Card from DB (ID = 2)
        Card existingCard = cardService.read(2L);
        assertNotNull(existingCard, "Existing card must be found in the database");

        Payment duplicate = new Payment.Builder()
                .setBooking(savedPayment.getBooking())
                .setPaymentMethod(Payment.PaymentMethod.CARD)
                .setPaymentStatus(Payment.PaymentStatus.PAID)
                .setPaymentAmount(savedPayment.getPaymentAmount())
                .setCard(existingCard)
                .build();

        Payment result = paymentService.create(duplicate);

        assertNotNull(result, "Duplicate payment should still be created");
        assertNotEquals(savedPayment.getPaymentId(), result.getPaymentId(),
                "Each payment should have a unique ID");

        System.out.println("✅ Duplicate payment created successfully: " + result);
    }

    @Test
    @Order(4)
    void testReadPayment() {
        System.out.println("📖 Reading Payment...");

        assertNotNull(savedPayment, "Saved payment must exist.");
        Payment readPayment = paymentService.read(savedPayment.getPaymentId());

        assertNotNull(readPayment, "Read Payment should not be null.");
        assertEquals(savedPayment.getPaymentId(), readPayment.getPaymentId(), "Payment IDs must match.");
        assertNotNull(readPayment.getBooking(), "Read Payment must contain booking info.");

        System.out.println("📘 Read Payment successfully: " + readPayment);
    }

    @Test
    @Order(5)
    @Rollback(false)
    void testUpdatePayment() {
        System.out.println("🛠️ Updating Payment...");

        assertNotNull(savedPayment, "Saved payment must exist.");

        Payment updatedPayment = new Payment.Builder()
                .copy(savedPayment)
                .setPaymentAmount(savedPayment.getPaymentAmount() + 50.0)
                .setPaymentStatus(Payment.PaymentStatus.PAID)
                .setPaymentMethod(Payment.PaymentMethod.CARD)
                .build();

        Payment result = paymentService.update(updatedPayment);

        assertNotNull(result);
        assertEquals(savedPayment.getPaymentId(), result.getPaymentId(), "Payment ID should not change.");
        assertEquals(savedPayment.getPaymentAmount() + 50.0, result.getPaymentAmount(), 0.01);
        assertEquals(Payment.PaymentStatus.PAID, result.getPaymentStatus());

        System.out.println("✅ Payment updated successfully: " + result);
    }

    @Test
    @Order(6)
    void testGetAllPayments() {
        System.out.println("📦 Fetching all Payments...");

        List<Payment> payments = paymentService.getAll();
        assertNotNull(payments, "List of payments should not be null.");
        assertFalse(payments.isEmpty(), "Payments list should not be empty.");

        System.out.println("✅ Total Payments found: " + payments.size());
        payments.forEach(p -> System.out.println("➡️ " + p));
    }

    @Test
    @Order(7)
    @Rollback(false)
    void testDeletePayment() {
        System.out.println("🗑️ Deleting Payment...");

        assertNotNull(savedPayment, "Saved payment must exist.");

        boolean deleted = paymentService.delete(savedPayment.getPaymentId());
        assertTrue(deleted, "Payment should be deleted.");

        Payment afterDelete = paymentService.read(savedPayment.getPaymentId());
        assertNull(afterDelete, "Deleted payment should not be found.");

        System.out.println("✅ Payment deleted successfully.");
    }
}