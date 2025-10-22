package za.ac.cput.controller.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.service.payment.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "false")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // ✅ 1. Create a new card (standalone or attached to customer)
    @PostMapping
    public Card create(@RequestBody Card card) {
        return cardService.create(card);
    }

    // ✅ 2. Create card linked directly to a specific customer
    @PostMapping("/customer/{customerId}")
    public Card createCardForCustomer(@PathVariable Long customerId, @RequestBody Card card) {
        System.out.println("[DEBUG] >>> Controller endpoint /customer/" + customerId + " hit <<<");

        return cardService.createCardForCustomer(customerId, card);
    }

    // ✅ 3. Read card by ID
    @GetMapping("/{id}")
    public Card read(@PathVariable Long id) {
        return cardService.read(id);
    }

    // ✅ 4. Get all cards
    @GetMapping
    public List<Card> getAll() {
        return cardService.getAll();
    }

    // ✅ 5. Get all cards belonging to a specific customer
    @GetMapping("/customer/{customerId}")
    public List<Card> getCardsByCustomerId(@PathVariable Long customerId) {
        return cardService.getCardsByCustomerId(customerId);
    }

    // ✅ 6. Update an existing card
    @PutMapping("/{id}")
    public Card update(@PathVariable Long id, @RequestBody Card updatedCard) {
        Card existingCard = cardService.read(id);
        if (existingCard == null) {
            throw new RuntimeException("Card not found with id: " + id);
        }

        Card cardToUpdate = new Card.Builder()
                .copy(existingCard)
                .setCardNumber(updatedCard.getCardNumber())
                .setCardHolderName(updatedCard.getCardHolderName())
                .setCvv(updatedCard.getCvv())
                .setExpiryDate(updatedCard.getExpiryDate())
                .build();

        return cardService.update(cardToUpdate);
    }

    // ✅ 7. Delete a card
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return cardService.delete(id);
    }

    @GetMapping("/debug")
    public String debug() {
        System.out.println("✅ [DEBUG] /api/cards/debug endpoint hit!");
        return "CardController active!";
    }
}