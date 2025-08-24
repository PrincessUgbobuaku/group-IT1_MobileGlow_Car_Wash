package za.ac.cput.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.factory.payment.PaymentFactory;
import za.ac.cput.repository.payment.IPaymentRepository;
import za.ac.cput.service.booking.BookingService;


import java.util.List;
import java.util.UUID;

@Service
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final BookingService bookingService;

    @Autowired
    public PaymentService(IPaymentRepository paymentRepository, BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
    }

    @Override
    public Payment create(Payment payment) {
        Booking booking = bookingService.read(payment.getBooking().getBookingID());

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found for ID: " + payment.getBooking().getBookingID());
        }

        Payment created = PaymentFactory.createPayment(
                booking,
                payment.getPaymentAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus()
        );

        if (created == null) {
            throw new IllegalArgumentException("Invalid Payment data");
        }

        return paymentRepository.save(created);
    }

    @Override
    public Payment read(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public boolean delete(String id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByBookingID(String bookingID) {
        return paymentRepository.findByBooking_BookingID(bookingID);
    }

//    // Create a payment specifically linked to an existing booking by booking ID
//    public Payment createPaymentForBooking(String bookingId, double amount, Payment.PaymentMethod method, Payment.PaymentStatus status) {
//        Booking booking = bookingService.read(bookingId);
//        if (booking == null) {
//            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
//        }
//        Payment payment = new Payment.Builder()
//                .setBooking(booking)
//                .setPaymentAmount(amount)
//                .setPaymentMethod(method)
//                .setPaymentStatus(status)
//                .build();
//        return paymentRepository.save(payment);
//    }

    public Payment createPaymentForBookingWithTip(String bookingId) {
        Booking booking = bookingService.read(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }

        double baseAmount = booking.getBookingCost();
        double paymentAmount = booking.isTipAdd() ? baseAmount * 1.10 : baseAmount;

        Payment payment = new Payment.Builder()
                .setPaymentID(UUID.randomUUID().toString())
                .setBooking(booking)
                .setPaymentMethod(Payment.PaymentMethod.DEBIT)
                .setPaymentStatus(Payment.PaymentStatus.PAID)
                .setPaymentAmount(paymentAmount)
                .build();

        return paymentRepository.save(payment);
    }
}
