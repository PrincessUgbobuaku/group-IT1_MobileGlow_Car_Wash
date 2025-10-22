package za.ac.cput.factory.payment;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.user.Customer;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

public class CardFactoryTest {

    @Test
    public void testCreateValidCard() {
        Card card = CardFactory.createCard(
                "4111111111111111", // Valid Visa test number
                "John Doe",
                "123",
                YearMonth.now().plusYears(1)
        );

        assertNotNull(card);
        assertEquals("John Doe", card.getCardHolderName());
        assertEquals("4111111111111111", card.getCardNumber());
        assertEquals("123", card.getCvv());
        assertTrue(card.getExpiryDate().isAfter(YearMonth.now()));

        System.out.println("✅ Valid card created:\n" + card);
    }

    @Test
    public void testCreateCardWithInvalidCardNumber() {
        Card card = CardFactory.createCard(
                "1234567890123456", // Invalid (fails Luhn)
                "Alice Smith",
                "456",
                YearMonth.now().plusMonths(6)
        );

        assertNull(card);
        System.out.println("❌ Card creation failed: invalid card number (Luhn check).");
    }

    @Test
    public void testCreateCardWithInvalidCVV() {
        Card card = CardFactory.createCard(
                "4111111111111111",
                "Bob Johnson",
                "12", // Too short
                YearMonth.now().plusMonths(6)
        );

        assertNull(card);
        System.out.println("❌ Card creation failed: invalid CVV.");
    }

    @Test
    public void testCreateCardWithExpiredDate() {
        Card card = CardFactory.createCard(
                "4111111111111111",
                "Charlie King",
                "789",
                YearMonth.now().minusMonths(1) // Expired
        );

        assertNull(card);
        System.out.println("❌ Card creation failed: expired card date.");
    }

    @Test
    public void testCreateCardWithMissingDetails() {
        Card card = CardFactory.createCard(
                "", // Empty card number
                null, // Null name
                "", // Empty CVV
                null // Null expiry date
        );

        assertNull(card);
        System.out.println("❌ Card creation failed: missing details.");
    }

    @Test
    public void testCreateCardLinkedToCustomer() {
        // ✅ Build a customer using your builder
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setUserName("Jane")
                .setUserSurname("Smith")
                .setIsActive(true)
                .setCustomerDOB(LocalDate.of(1995, 5, 15))
                .build();

        // ✅ Create card with linked customer
        Card card = CardFactory.createCard(
                "4000056655665556",
                "Jane Smith",
                "321",
                YearMonth.now().plusYears(2)
        );

        // Manually link card to customer (if your factory doesn't handle it)
        card.setCustomer(customer);

        assertNotNull(card);
        assertNotNull(card.getCustomer());
        assertEquals("Jane", card.getCustomer().getUserName());
        assertEquals("Jane Smith", card.getCardHolderName());

        System.out.println("✅ Card linked to customer created:\n" + card);
    }
}