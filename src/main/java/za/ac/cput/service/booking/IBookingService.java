package za.ac.cput.service.booking;

import org.springframework.stereotype.Repository;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.service.IService;

import java.util.List;

@Repository
public interface IBookingService extends IService<Booking, String> {

    List<Booking> getAll();

}
