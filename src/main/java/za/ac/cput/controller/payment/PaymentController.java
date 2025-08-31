package za.ac.cput.controller.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.service.payment.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments") // Matches BookingController pattern
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ CREATE - POST /api/payments
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment payment) {
        Payment created = paymentService.create(payment);
        return ResponseEntity.ok(created);
    }

    // ✅ READ - GET /api/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Payment> read(@PathVariable Long id) {
        Payment found = paymentService.read(id);
        return found != null ? ResponseEntity.ok(found) : ResponseEntity.notFound().build();
    }

    // ✅ UPDATE - PUT /api/payments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payment) {
        if (!id.equals(payment.getPaymentID())) {
            return ResponseEntity.badRequest().build();
        }
        Payment updated = paymentService.update(payment);
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE - DELETE /api/payments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = paymentService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ GET ALL - GET /api/payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    // ✅ GET payments by booking ID - GET /api/payments/booking/{bookingId}
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBookingID(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBookingID(bookingId);
        return ResponseEntity.ok(payments);
    }
}