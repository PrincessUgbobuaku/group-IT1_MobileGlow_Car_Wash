/*Payment.java
 * Payment model class
 * Author: Adaeze Princess Ugbobuaku
 * Date: 11 May 2025*/

package za.ac.cput.domain.payment;

import za.ac.cput.domain.booking.Booking;

public class Payment {

    private String paymentID;
    private String bookingID;
    private double paymentAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private Payment (Builder builder) {
        this.paymentID = builder.paymentID;
        this.bookingID = builder.bookingID;
        this.paymentAmount = builder.paymentAmount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getBookingID() {
        return bookingID;
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
                ", bookingID='" + bookingID + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod=" + paymentMethod +
                ", paymentStatus=" + paymentStatus +
                '}';
    }

    public static class Builder {

        private String paymentID;
        private String bookingID;
        private double paymentAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;

        public Builder setPaymentID(String paymentID) {
            this.paymentID = paymentID;
            return this;
        }

        public Builder setBookingID(String bookingID) {
            this.bookingID = bookingID;
            return this;
        }

        public Builder setPaymentAmount(double paymentAmount) {

            this.paymentAmount = paymentAmount;
            return this;
        }

        public double calculatePaymentAmount(Booking booking) { // calculates total payment; adds tip if set to true
            double bookingCost = booking.getBookingCost();
            boolean tipAdd = booking.isTipAdd();


            if (tipAdd) {
                paymentAmount = bookingCost + (bookingCost * 0.10);
            } else {
                paymentAmount = bookingCost;
            }
                return paymentAmount;

            }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Payment build () {
            return new Payment (this);
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
