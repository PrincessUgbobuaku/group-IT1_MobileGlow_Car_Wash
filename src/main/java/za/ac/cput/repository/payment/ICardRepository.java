package za.ac.cput.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.payment.Card;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);

    boolean existsByCardNumber(String cardNumber);

    Optional<Card> findByCardNumberAndCardHolderNameAndExpiryDate(
            String cardNumber, String cardHolderName, YearMonth expiryDate
    );

    // ✅ Add this new query to support your service
    List<Card> findByCustomer_UserId(Long customerId);
}