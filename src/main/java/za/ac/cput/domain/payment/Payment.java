package za.ac.cput.domain.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import za.ac.cput.domain.booking.Booking;

import java.io.Serializable;

@Entity
public class Payment implements Serializable {

    @Id
    private String paymentID;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Booking booking;

    private double paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    protected Payment() {}

    private Payment(Builder builder) {
        this.paymentID = builder.paymentID;
        this.booking = builder.booking;
        this.paymentAmount = builder.paymentAmount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public Booking getBooking() {
        return booking;
    }

    // Add this method to expose bookingID in JSON response
    @JsonProperty("bookingID")
    public String getBookingID() {
        return booking != null ? booking.getBookingID() : null;
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

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID='" + paymentID + '\'' +
                ", booking='" + booking + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod=" + paymentMethod +
                ", paymentStatus=" + paymentStatus +
                '}';
    }

    public static class Builder {

        private String paymentID;
        private Booking booking;
        private double paymentAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;

        public Builder setPaymentID(String paymentID) {
            this.paymentID = paymentID;
            return this;
        }

        public Builder setBooking(Booking booking) {
            this.booking = booking;
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

        public Builder copy(Payment payment) {
            this.paymentID = payment.paymentID;
            this.booking = payment.booking;
            this.paymentAmount = payment.paymentAmount;
            this.paymentMethod = payment.paymentMethod;
            this.paymentStatus = payment.paymentStatus;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public enum PaymentMethod {
        DEBIT,
        CREDIT,
        APPLE_PAY,
        GOOGLE_PAY,
        PAYPAL
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        FAILED
    }
}