package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookingId(Long bookingId);

    boolean existsByVehicleAndBookingDateTime(Vehicle vehicle, LocalDateTime bookingDateTime);

    boolean existsByVehicle_VehicleIDAndBookingDateTime(Long vehicleId, LocalDateTime bookingDateTime);

    boolean existsByWashAttendantAndBookingDateTime(WashAttendant attendant, LocalDateTime time);

    // ✅ Added methods to exclude cancelled bookings
    boolean existsByVehicleAndBookingDateTimeAndCancelledFalse(Vehicle vehicle, LocalDateTime bookingDateTime);

    boolean existsByWashAttendantAndBookingDateTimeAndCancelledFalse(WashAttendant washAttendant, LocalDateTime bookingDateTime);

}
