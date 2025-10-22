package za.ac.cput.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.factory.payment.CardFactory;
import za.ac.cput.repository.payment.ICardRepository;
import za.ac.cput.repository.user.ICustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService {

    private final ICardRepository cardRepository;
    private final ICustomerRepository customerRepository;

    @Autowired
    public CardService(ICardRepository cardRepository, ICustomerRepository customerRepository) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Card create(Card card) {
        System.out.println("[DEBUG] create() called with cardNumber: " + card.getCardNumber());

        Optional<Card> existingCard = cardRepository.findByCardNumberAndCardHolderNameAndExpiryDate(
                card.getCardNumber(), card.getCardHolderName(), card.getExpiryDate());

        if (existingCard.isPresent()) {
            System.out.println("[DEBUG] Existing card found with ID: " + existingCard.get().getCardId());
            return existingCard.get();
        }

        System.out.println("[DEBUG] No existing card found, creating new card...");

        Card created = CardFactory.createCard(
                card.getCardNumber(),
                card.getCardHolderName(),
                card.getCvv(),
                card.getExpiryDate()
        );

        if (created == null) {
            System.out.println("[ERROR] Invalid card details provided");
            throw new IllegalArgumentException("Invalid card details");
        }

        if (card.getCustomer() != null && card.getCustomer().getUserId() != null) {
            Customer customer = customerRepository.findById(card.getCustomer().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            created.setCustomer(customer);
            System.out.println("[DEBUG] Linked card to customer ID: " + customer.getUserId());
        }

        Card savedCard = cardRepository.save(created);
        System.out.println("[DEBUG] Card saved with ID: " + savedCard.getCardId());
        return savedCard;
    }

    @Override
    public Card read(Long id) {
        System.out.println("[DEBUG] read() called for ID: " + id);
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card update(Card card) {
        System.out.println("[DEBUG] Updating card ID: " + card.getCardId());
        return cardRepository.save(card);
    }

    @Override
    public boolean delete(Long id) {
        System.out.println("[DEBUG] delete() called for ID: " + id);
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
            System.out.println("[DEBUG] Card deleted with ID: " + id);
            return true;
        }
        System.out.println("[WARN] Card not found for deletion with ID: " + id);
        return false;
    }

    @Override
    public List<Card> getAll() {
        System.out.println("[DEBUG] getAll() called");
        return cardRepository.findAll();
    }

    public List<Card> getCardsByCustomerId(Long customerId) {
        System.out.println("[DEBUG] getCardsByCustomerId() called for customer ID: " + customerId);
        return cardRepository.findByCustomer_UserId(customerId);
    }

    // ✅ ENHANCED DEBUGGING HERE
    public Card createCardForCustomer(Long customerId, Card card) {
        System.out.println("==================================================");
        System.out.println("[DEBUG] createCardForCustomer() called for customer ID: " + customerId);
        System.out.println("[DEBUG] Request body cardNumber: " + card.getCardNumber());
        System.out.println("[DEBUG] Request body holderName: " + card.getCardHolderName());
        System.out.println("[DEBUG] Checking if customer exists in DB...");

        boolean exists = customerRepository.existsById(customerId);
        System.out.println("[DEBUG] Customer exists? " + exists);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] Customer not found in DB with ID: " + customerId);
                    return new IllegalArgumentException("Customer not found with ID: " + customerId);
                });

        System.out.println("[DEBUG] Customer retrieved successfully -> ID: " + customer.getUserId());

        Card created = CardFactory.createCard(
                card.getCardNumber(),
                card.getCardHolderName(),
                card.getCvv(),
                card.getExpiryDate()
        );

        if (created == null) {
            System.out.println("[ERROR] CardFactory returned null (invalid data).");
            throw new IllegalArgumentException("Invalid card details");
        }

        created.setCustomer(customer);
        System.out.println("[DEBUG] Linked card to customer ID: " + customer.getUserId());

        Card saved = cardRepository.save(created);
        System.out.println("[DEBUG] Card saved successfully with ID: " + saved.getCardId());
        System.out.println("==================================================");
        return saved;
    }
}