package za.ac.cput.domain.payment;

import jakarta.persistence.*;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.util.YearMonthConverter;

import java.time.YearMonth;
import java.util.List;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(unique = true)
    private String cardNumber;

    private String cardHolderName;

    private String cvv;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth expiryDate;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // ✅ Relationship to Customer

    public Card() {}

    private Card(Builder builder) {
        this.cardId = builder.cardId;
        this.cardNumber = builder.cardNumber;
        this.cardHolderName = builder.cardHolderName;
        this.cvv = builder.cvv;
        this.expiryDate = builder.expiryDate;
        this.customer = builder.customer;
    }

    // ✅ Getters
    public Long getCardId() {
        return cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCvv() {
        return cvv;
    }

    public YearMonth getExpiryDate() {
        return expiryDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    // ✅ Add this setter
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // ✅ Updated Builder
    public static class Builder {
        private Long cardId;
        private String cardNumber;
        private String cardHolderName;
        private String cvv;
        private YearMonth expiryDate;
        private Customer customer; // added

        public Builder setCardId(Long cardId) {
            this.cardId = cardId;
            return this;
        }

        public Builder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder setCardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
            return this;
        }

        public Builder setCvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder setExpiryDate(YearMonth expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        // ✅ Add builder support for linking customer
        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder copy(Card card) {
            this.cardId = card.cardId;
            this.cardNumber = card.cardNumber;
            this.cardHolderName = card.cardHolderName;
            this.cvv = card.cvv;
            this.expiryDate = card.expiryDate;
            this.customer = card.customer;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", expiryDate=" + expiryDate +
                ", customer=" + (customer != null ? customer.getUserName() : "null") +
                '}';
    }
}