package za.ac.cput.service.payment;

import za.ac.cput.domain.payment.Payment;
import za.ac.cput.service.IService;

import java.util.List;

public interface IPaymentService extends IService<Payment, Long> {
    List<Payment> getAll();

    List<Payment> getPaymentsByBookingId(Long bookingId);

}
