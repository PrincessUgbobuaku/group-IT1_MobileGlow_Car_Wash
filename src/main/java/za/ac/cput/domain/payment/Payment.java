package za.ac.cput.domain.payment;

import jakarta.persistence.*;
import za.ac.cput.domain.booking.Booking;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private double paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", nullable = true)
    private Card card;

    // Default constructor
    public Payment() {
    }

    // Builder constructor
    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.paymentAmount = builder.paymentAmount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
        this.booking = builder.booking;
        this.card = builder.card;
    }

    // Getters
    public Long getPaymentId() {
        return paymentId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Booking getBooking() {
        return booking;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public String toString() {
        return "Payment {\n" +
                "  paymentId = " + paymentId + ",\n" +
                "  paymentAmount = " + paymentAmount + ",\n" +
                "  paymentMethod = " + paymentMethod + ",\n" +
                "  paymentStatus = " + paymentStatus + ",\n" +
                "  bookingId = " + (booking != null ? booking.getBookingId() : "null") + ",\n" +
                "  cardId = " + (card != null ? card.getCardId() : "null") + "\n" +
                '}';
    }

    // Builder class
    public static class Builder {
        private Long paymentId;
        private double paymentAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private Booking booking;
        private Card card;

        public Builder setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setPaymentAmount(double paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setBooking(Booking booking) {
            this.booking = booking;
            return this;
        }

        public Builder setCard(Card card) {
            this.card = card;
            return this;
        }

        public Builder copy(Payment payment) {
            this.paymentId = payment.getPaymentId();
            this.paymentAmount = payment.getPaymentAmount();
            this.paymentMethod = payment.getPaymentMethod();
            this.paymentStatus = payment.getPaymentStatus();
            this.booking = payment.getBooking();
            this.card = payment.getCard();
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    // --- ENUMS ---
    public enum PaymentMethod {
        CASH,
        CARD
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        FAILED
    }
}