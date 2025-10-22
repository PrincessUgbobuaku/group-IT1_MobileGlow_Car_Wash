package za.ac.cput.controller.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.service.payment.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * CREATE PAYMENT
     * Example: POST /api/payments
     * Body: {
     * "booking": { "bookingId": 1 },
     * "paymentAmount": 200.00,
     * "paymentMethod": "CARD",
     * "paymentStatus": "PAID",
     * "card": { "cardId": 3 } // optional unless method == CARD
     * }
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Payment payment) {
        logger.info("🟢 Creating payment: {}", payment);

        try {
            Payment created = paymentService.create(payment);
            logger.info("✅ Payment created successfully (ID: {})", created.getPaymentId());
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Invalid payment request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Error creating payment", e);
            return ResponseEntity.internalServerError().body("Error creating payment: " + e.getMessage());
        }
    }

    /** READ PAYMENT BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> read(@PathVariable Long id) {
        Payment found = paymentService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    /** UPDATE PAYMENT */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment payment) {
        if (!id.equals(payment.getPaymentId())) {
            logger.warn("⚠️ Path ID ({}) does not match body ID ({})", id, payment.getPaymentId());
            return ResponseEntity.badRequest().body("Payment ID mismatch");
        }
        try {
            Payment updated = paymentService.update(payment);
            logger.info("✅ Payment updated (ID: {})", updated.getPaymentId());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("❌ Error updating payment", e);
            return ResponseEntity.internalServerError().body("Error updating payment: " + e.getMessage());
        }
    }

    /** DELETE PAYMENT */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = paymentService.delete(id);
        if (deleted) {
            logger.info("🗑️ Payment deleted (ID: {})", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("⚠️ Payment not found for delete (ID: {})", id);
            return ResponseEntity.notFound().build();
        }
    }

    /** GET ALL PAYMENTS */
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    /** GET PAYMENTS BY BOOKING ID */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBookingID(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }

    /**
     * CREATE PAYMENT FOR BOOKING WITH TIP
     * Example: POST /api/payments/booking/{bookingId}/tip?method=CARD
     */
    @PostMapping("/booking/{bookingId}/tip")
    public ResponseEntity<?> createPaymentWithTip(
            @PathVariable Long bookingId,
            @RequestParam Payment.PaymentMethod method) {
        try {
            Payment created = paymentService.createPaymentForBookingWithTip(bookingId, method);
            logger.info("💳 Payment with tip created for booking ID {}", bookingId);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("❌ Error creating payment with tip", e);
            return ResponseEntity.internalServerError().body("Error creating payment with tip: " + e.getMessage());
        }
    }
}