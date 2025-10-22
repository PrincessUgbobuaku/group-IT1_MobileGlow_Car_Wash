package za.ac.cput.service.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.factory.payment.PaymentFactory;
import za.ac.cput.repository.payment.IPaymentRepository;
import za.ac.cput.service.booking.BookingService;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final CardService cardService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(IPaymentRepository paymentRepository,
            BookingService bookingService,
            CardService cardService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
        this.cardService = cardService;
    }

    @Override
    public Payment create(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment object cannot be null.");
        }

        if (payment.getBooking() == null || payment.getBooking().getBookingId() == null) {
            throw new IllegalArgumentException("Payment must be linked to a valid booking.");
        }

        Long bookingId = payment.getBooking().getBookingId();
        Booking booking = bookingService.read(bookingId);

        if (booking == null) {
            logger.error("❌ Booking not found for ID: {}", bookingId);
            throw new IllegalArgumentException("Booking not found for ID: " + bookingId);
        }

        Card card = null;
        if (payment.getPaymentMethod() == Payment.PaymentMethod.CARD) {
            if (payment.getCard() == null || payment.getCard().getCardId() == null) {
                logger.error("❌ Card details missing for CARD payment.");
                throw new IllegalArgumentException("Card details are required for CARD payments.");
            }

            // Fetch managed Card entity
            card = cardService.read(payment.getCard().getCardId());
            if (card == null) {
                logger.error("❌ Card not found with ID: {}", payment.getCard().getCardId());
                throw new IllegalArgumentException("Card not found with ID: " + payment.getCard().getCardId());
            }

            // Validate card-customer link
            if (card.getCustomer() == null) {
                logger.error("❌ Card {} is not linked to any Customer.", card.getCardId());
                throw new IllegalArgumentException("Card must be linked to a valid Customer before payment.");
            }
        }

        logger.info("🧾 Creating Payment for Booking ID: {} using method: {}", bookingId, payment.getPaymentMethod());

        Payment createdPayment = PaymentFactory.createPayment(
                booking,
                payment.getPaymentAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                card);

        if (createdPayment == null) {
            logger.error("❌ PaymentFactory returned null — check validation logic.");
            throw new IllegalStateException("Failed to create Payment object. Validation failed.");
        }

        Payment saved = paymentRepository.save(createdPayment);
        logger.info("✅ Payment created successfully with ID: {} for Booking: {}", saved.getPaymentId(), bookingId);
        return saved;
    }

    @Override
    public Payment read(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment update(Payment payment) {
        if (payment == null || payment.getPaymentId() == null) {
            throw new IllegalArgumentException("Payment or Payment ID cannot be null for update.");
        }
        logger.info("🛠️ Updating Payment with ID: {}", payment.getPaymentId());
        return paymentRepository.save(payment);
    }

    @Override
    public boolean delete(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            logger.info("🗑️ Payment deleted with ID: {}", id);
            return true;
        }
        logger.warn("⚠️ Payment not found with ID: {}", id);
        return false;
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBooking_BookingId(bookingId);
    }

    public Payment createPaymentForBookingWithTip(Long bookingId, Payment.PaymentMethod paymentMethod) {
        Booking booking = bookingService.read(bookingId);
        if (booking == null) {
            logger.error("❌ Booking not found for ID: {}", bookingId);
            throw new IllegalArgumentException("Booking not found with ID: " + bookingId);
        }

        double baseAmount = booking.getBookingCost();
        double paymentAmount = booking.isTipAdd() ? baseAmount * 1.10 : baseAmount;

        Payment payment = new Payment.Builder()
                .setBooking(booking)
                .setPaymentMethod(paymentMethod)
                .setPaymentStatus(Payment.PaymentStatus.PAID)
                .setPaymentAmount(paymentAmount)
                .build();

        Payment saved = paymentRepository.save(payment);
        logger.info("💵 Payment with tip created for booking ID: {} (Amount: {})", bookingId, paymentAmount);
        return saved;
    }

    public boolean paymentExistsForBooking(Long bookingId) {
        boolean exists = paymentRepository.existsByBooking_BookingId(bookingId);
        logger.info("🔍 Checking if payment exists for Booking ID {}: {}", bookingId, exists);
        return exists;
    }
}