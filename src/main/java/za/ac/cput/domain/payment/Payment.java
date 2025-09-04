package za.ac.cput.domain.payment;

import za.ac.cput.domain.booking.Booking;

import jakarta.persistence.*;

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

    public Payment() {}

    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.paymentAmount = builder.paymentAmount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
        this.booking = builder.booking;
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

    @Override
    public String toString() {
        return "Payment {\n" +
                "  paymentId = " + paymentId + ",\n" +
                "  paymentAmount = " + paymentAmount + ",\n" +
                "  paymentMethod = " + paymentMethod + ",\n" +
                "  paymentStatus = " + paymentStatus + ",\n" +
                "  bookingId = " + booking.getBookingId() + "\n" +
                '}';
    }

    // Builder
    public static class Builder {
        private Long paymentId;
        private double paymentAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private Booking booking;

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

        public Builder copy(Payment payment) {
            this.paymentId = payment.getPaymentId();
            this.paymentAmount = payment.getPaymentAmount();
            this.paymentMethod = payment.getPaymentMethod();
            this.paymentStatus = payment.getPaymentStatus();
            this.booking = payment.getBooking();
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



//package za.ac.cput.domain.payment;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
//import za.ac.cput.domain.booking.Booking;
//
//import java.io.Serializable;
//
//@Entity
//public class Payment implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long paymentID;  // Changed from String to Long
//
//    @ManyToOne
//    @JoinColumn(name = "booking_id", nullable = false)
//    @JsonBackReference
//    private Booking booking;
//
//    private double paymentAmount;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentMethod paymentMethod;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentStatus paymentStatus;
//
//    protected Payment() {}
//
//    private Payment(Builder builder) {
//        this.paymentID = builder.paymentID;
//        this.booking = builder.booking;
//        this.paymentAmount = builder.paymentAmount;
//        this.paymentMethod = builder.paymentMethod;
//        this.paymentStatus = builder.paymentStatus;
//    }
//
//    public Long getPaymentID() {  // Changed return type
//        return paymentID;
//    }
//
//    public Booking getBooking() {
//        return booking;
//    }
//
//    @JsonProperty("bookingID")
//    public Long getBookingID() {
//        return booking != null ? booking.getBookingID() : null;
//    }
//
//    public double getPaymentAmount() {
//        return paymentAmount;
//    }
//
//    public PaymentMethod getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public PaymentStatus getPaymentStatus() {
//        return paymentStatus;
//    }
//
//    @Override
//    public String toString() {
//        return "Payment{" +
//                "paymentID=" + paymentID +  // no quotes for Long
//                ", bookingID=" + (booking != null ? booking.getBookingID() : "null") +  // 👈 Changed
//                ", paymentAmount=" + paymentAmount +
//                ", paymentMethod=" + paymentMethod +
//                ", paymentStatus=" + paymentStatus +
//                '}';
//    }
//
//    public static class Builder {
//
//        private Long paymentID;  // Changed from String to Long
//        private Booking booking;
//        private double paymentAmount;
//        private PaymentMethod paymentMethod;
//        private PaymentStatus paymentStatus;
//
//        public Builder setPaymentID(Long paymentID) {  // Changed parameter type
//            this.paymentID = paymentID;
//            return this;
//        }
//
//        public Builder setBooking(Booking booking) {
//            this.booking = booking;
//            return this;
//        }
//
//        public Builder setPaymentAmount(double paymentAmount) {
//            this.paymentAmount = paymentAmount;
//            return this;
//        }
//
//        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
//            this.paymentMethod = paymentMethod;
//            return this;
//        }
//
//        public Builder setPaymentStatus(PaymentStatus paymentStatus) {
//            this.paymentStatus = paymentStatus;
//            return this;
//        }
//
//        public Builder copy(Payment payment) {
//            this.paymentID = payment.paymentID;
//            this.booking = payment.booking;
//            this.paymentAmount = payment.paymentAmount;
//            this.paymentMethod = payment.paymentMethod;
//            this.paymentStatus = payment.paymentStatus;
//            return this;
//        }
//
//        public Payment build() {
//            return new Payment(this);
//        }
//    }
//
//    public enum PaymentMethod {
//        DEBIT,
//        CREDIT,
//        APPLE_PAY,
//        GOOGLE_PAY,
//        PAYPAL
//    }
//
//    public enum PaymentStatus {
//        PENDING,
//        PAID,
//        FAILED
//    }
//}