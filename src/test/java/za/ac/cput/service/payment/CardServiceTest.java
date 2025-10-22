package za.ac.cput.service.payment;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.repository.user.ICustomerRepository;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ✅ CardService Integration Test
 * Tests all CRUD operations + linking cards to customers.
 * Author: Thaakirah Watson
 */
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private ICustomerRepository customerRepository;

    private static Card savedCard;
    private static Customer testCustomer;

    @BeforeAll
    static void init() {
        System.out.println("🔧 Starting CardServiceTest...");
    }

    @BeforeEach
    void setupCustomer() {
        if (testCustomer == null) {
            testCustomer = new Customer.Builder()
                    .setUserName("Jane")
                    .setUserSurname("Tester")
                    .setIsActive(true)
                    .setCustomerDOB(java.time.LocalDate.of(1998, 5, 21))
                    .build();
            testCustomer = customerRepository.save(testCustomer);
            System.out.println("👤 Test Customer created with ID: " + testCustomer.getUserId());
        }
    }

    @Test
    @Order(1)
    @Rollback(false)
    void testCreateValidCard() {
        Card card = new Card.Builder()
                .setCardNumber("6011111111111117")
                .setCardHolderName("Jane Doe")
                .setCvv("000")
                .setExpiryDate(YearMonth.now().plusMonths(6))
                .build();

        Card created = cardService.create(card);
        assertNotNull(created, "Created card should not be null");
        assertEquals(card.getCardHolderName(), created.getCardHolderName());

        savedCard = created;

        System.out.println("✅ Created Card: " + created);
    }

    @Test
    @Order(2)
    @Rollback(false)
    void testCreateDuplicateCardShouldReturnExisting() {
        Card duplicateCard = new Card.Builder()
                .setCardNumber(savedCard.getCardNumber())
                .setCardHolderName(savedCard.getCardHolderName())
                .setCvv("124")
                .setExpiryDate(savedCard.getExpiryDate())
                .build();

        Card result = cardService.create(duplicateCard);
        assertNotNull(result);
        assertEquals(savedCard.getCardId(), result.getCardId(), "Duplicate card creation should return existing card");

        System.out.println("♻️ Duplicate card creation returned existing card: " + result);
    }

    @Test
    @Order(3)
    void testReadCard() {
        assertNotNull(savedCard, "Saved card should exist");

        Card readCard = cardService.read(savedCard.getCardId());
        assertNotNull(readCard);
        assertEquals(savedCard.getCardId(), readCard.getCardId());

        System.out.println("📖 Read Card: " + readCard);
    }

    @Test
    @Order(4)
    @Rollback(false)
    void testUpdateCard() {
        assertNotNull(savedCard, "Saved card should exist");

        Card updatedCard = new Card.Builder()
                .copy(savedCard)
                .setCardHolderName("John Lekker")
                .build();

        Card result = cardService.update(updatedCard);
        assertNotNull(result);
        assertEquals("John Lekker", result.getCardHolderName(), "Card holder name should be updated");

        System.out.println("✏️ Updated Card: " + result);
    }

    @Test
    @Order(5)
    @Rollback(false)
    void testCreateCardForCustomer() {
        assertNotNull(testCustomer, "Test customer should exist");

        Card card = new Card.Builder()
                .setCardNumber("5555555555554444")
                .setCardHolderName("Customer Linked Card")
                .setCvv("555")
                .setExpiryDate(YearMonth.now().plusYears(2))
                .build();

        Card created = cardService.createCardForCustomer(testCustomer.getUserId(), card);

        assertNotNull(created, "Created card should not be null");
        assertNotNull(created.getCustomer(), "Card should be linked to a customer");
        assertEquals(testCustomer.getUserId(), created.getCustomer().getUserId(), "Customer ID should match");

        System.out.println("🔗 Created Card Linked to Customer: " + created);
    }

    @Test
    @Order(6)
    void testGetAllCards() {
        List<Card> cards = cardService.getAll();
        assertNotNull(cards);
        assertFalse(cards.isEmpty(), "Card list should not be empty");

        System.out.println("📋 All Cards: " + cards);
    }

    @Test
    @Order(7)
    void testGetCardsByCustomerId() {
        List<Card> customerCards = cardService.getCardsByCustomerId(testCustomer.getUserId());
        assertNotNull(customerCards);
        assertFalse(customerCards.isEmpty(), "Customer should have at least one card linked");

        System.out.println("👛 Cards linked to Customer ID " + testCustomer.getUserId() + ": " + customerCards);
    }

    @Test
    @Order(8)
    @Rollback(false)
    void testDeleteCard() {
        assertNotNull(savedCard, "Saved card should exist");

        boolean deleted = cardService.delete(savedCard.getCardId());
        assertTrue(deleted, "Card should be successfully deleted");

        Card afterDelete = cardService.read(savedCard.getCardId());
        assertNull(afterDelete, "Card should no longer exist after deletion");

        System.out.println("🗑️ Card deleted successfully");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("✅ CardServiceTest completed successfully.");
    }
}