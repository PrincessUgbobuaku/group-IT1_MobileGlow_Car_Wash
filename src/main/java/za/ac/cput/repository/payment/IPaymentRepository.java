package za.ac.cput.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {

//    List<Payment> getAll(); //going into the database, getting all the objects and returning them as a list

    List<Payment> findByBooking_BookingId(Long bookingID); // needed for service

}
