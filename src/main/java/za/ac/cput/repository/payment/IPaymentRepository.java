package za.ac.cput.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.payment.Payment;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    // Find all payments associated with a booking ID
    List<Payment> findByBooking_BookingId(Long bookingId);

    // Check if a payment exists for a given booking
    boolean existsByBooking_BookingId(Long bookingId);

    // Optional: Get all payments where method was CARD
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
}