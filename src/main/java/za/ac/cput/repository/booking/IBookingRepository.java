package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.repository.IRepository;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, String> {

//        List<Booking> getAll(); //going into the database, getting all the objects and returning them as a list
}
