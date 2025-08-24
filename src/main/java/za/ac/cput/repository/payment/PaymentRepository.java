package za.ac.cput.repository.payment;

import za.ac.cput.repository.payment.impl.IPaymentRepository;

public class PaymentRepository /*implements IPaymentRepository*/ {

    /*
    * private static PaymentRepository repository = null;
    private Map<String, Payment> paymentDB;

    private PaymentRepository() {
        this.paymentDB = new HashMap<>();
    }

    public static PaymentRepository getRepository() {
        if (repository == null) {
            repository = new PaymentRepository();
        }
        return repository;
    }

    @Override
    public Payment create(Payment payment) {
        paymentDB.put(payment.getPaymentID(), payment);
        return payment;
    }

    @Override
    public Payment read(String paymentID) {
        return paymentDB.get(paymentID);
    }

    @Override
    public Payment update(Payment payment) {
        if (paymentDB.containsKey(payment.getPaymentID())) {
            paymentDB.put(payment.getPaymentID(), payment);
            return payment;
        }
        return null;
    }

    @Override
    public boolean delete(String paymentID) {
        return paymentDB.remove(paymentID) != null;
    }

    @Override
    public List<Payment> getAll() {
        return new ArrayList<>(paymentDB.values());
    }
    *
    *
    * */



}
