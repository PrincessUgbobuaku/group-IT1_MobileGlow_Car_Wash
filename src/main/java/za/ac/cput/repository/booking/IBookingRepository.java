package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    // List<Booking> getAll(); //going into the database, getting all the objects
    // and returning them as a list
    List<Booking> findByBookingId(Long bookingId);

    boolean existsByVehicleAndBookingDateTime(Vehicle vehicle, LocalDateTime bookingDateTime);

    boolean existsByVehicle_VehicleIDAndBookingDateTime(Long vehicleId, LocalDateTime bookingDateTime);

    boolean existsByWashAttendantAndBookingDateTime(WashAttendant attendant, LocalDateTime time);
    // boolean existsByVehicleAndBookingDateTime(Vehicle vehicle, LocalDateTime
    // bookingDateTime);

}
