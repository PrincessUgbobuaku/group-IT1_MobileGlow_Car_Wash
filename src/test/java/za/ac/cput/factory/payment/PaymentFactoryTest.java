package za.ac.cput.factory.payment;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.*;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.booking.BookingFactory;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.factory.booking.VehicleFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFactoryTest {

    private Booking createTestBooking() {
        CleaningService cs = CleaningServiceFactory.createCleaningService("Interior Clean", 200, 1.0, "Interior");

        // ✅ Build a simulated persisted customer
        Customer customer = new Customer.Builder()
                .setUserId(1L) // simulate saved entity
                .setUserName("Test")
                .setUserSurname("User")
                .setCustomerDOB(LocalDate.of(1995, 1, 1))
                .build();

        Vehicle vehicle = VehicleFactory.createVehicle("TEST123", "Honda", "Blue", "Civic", customer);

        WashAttendant attendant = new WashAttendant.Builder()
                .setUserName("Cleaner")
                .setUserSurname("One")
                .build();

        return BookingFactory.createBooking(
                List.of(cs),
                vehicle,
                attendant,
                LocalDateTime.of(2025, 10, 25, 10, 30),
                false,
                200
        );
    }

    @Test
    void testCreatePaymentWithoutCard() {
        System.out.println("\n💰 Running testCreatePaymentWithoutCard...");

        Booking booking = createTestBooking();

        Payment payment = PaymentFactory.createPayment(
                booking,
                200,
                Payment.PaymentMethod.CASH,
                Payment.PaymentStatus.PAID
        );

        assertNotNull(payment, "Payment should be created");
        assertEquals(200, payment.getPaymentAmount());
        assertEquals(Payment.PaymentMethod.CASH, payment.getPaymentMethod());
        assertNull(payment.getCard(), "No card should be linked for cash payments");

        System.out.println("✅ Payment created successfully: " + payment);
    }

    @Test
    void testCreatePaymentWithCard() {
        System.out.println("\n💳 Running testCreatePaymentWithCard...");

        Booking booking = createTestBooking();

        // ✅ Build a persisted-like customer directly in builder
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setUserName("John")
                .setUserSurname("Doe")
                .setCustomerDOB(LocalDate.of(1990, 5, 12))
                .build();

        Card card = CardFactory.createCard(
                "4111111111111111", // Valid Luhn test number
                "John Doe",
                "123",
                YearMonth.of(2026, 12)
        );
        assertNotNull(card, "Card should be created successfully");

        // ✅ Link card to customer
        card.setCustomer(customer);
        assertNotNull(card.getCustomer(), "Card should have a linked customer");

        Payment payment = PaymentFactory.createPayment(
                booking,
                200,
                Payment.PaymentMethod.CARD,
                Payment.PaymentStatus.PAID,
                card
        );

        assertNotNull(payment, "Payment should not be null");
        assertEquals(Payment.PaymentMethod.CARD, payment.getPaymentMethod());
        assertNotNull(payment.getCard(), "Card should be linked in payment");
        assertEquals(customer.getUserId(), payment.getCard().getCustomer().getUserId(), "Customer ID should match");

        System.out.println("✅ Created Card Payment: " + payment);
    }

    @Test
    void testInvalidCardPaymentFails() {
        System.out.println("\n❌ Running testInvalidCardPaymentFails...");

        Booking booking = createTestBooking();

        Card invalidCard = CardFactory.createCard(
                "1234567890123456", // Invalid Luhn number
                "John Doe",
                "12", // Invalid CVV
                YearMonth.of(2020, 1) // Expired
        );

        assertNull(invalidCard, "Invalid card should fail validation");

        // Even if card is invalid, payment can still be made (card=null)
        Payment payment = PaymentFactory.createPayment(
                booking,
                200,
                Payment.PaymentMethod.CASH, // ✅ changed to CASH since card is invalid
                Payment.PaymentStatus.PAID,
                invalidCard
        );

        assertNotNull(payment, "Payment should still be created");
        assertNull(payment.getCard(), "Invalid card should not be linked to payment");

        System.out.println("✅ Invalid card rejected correctly.");
    }
}