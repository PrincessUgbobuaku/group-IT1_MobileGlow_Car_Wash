package za.ac.cput.repository.payment.impl;

import za.ac.cput.domain.payment.Payment;

import java.util.List;

public interface IPaymentRepository {

    List<Payment> getAll();

}
