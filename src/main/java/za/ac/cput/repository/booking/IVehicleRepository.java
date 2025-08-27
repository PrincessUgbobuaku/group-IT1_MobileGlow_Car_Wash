//Thaakirah Watson, 230037550
package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.booking.Vehicle;

import java.util.Optional;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, Long> {
    // Find vehicle by plate number
    Optional<Vehicle> findByCarPlateNumber(String carPlateNumber);

    // Find all vehicles by customer ID
    java.util.List<Vehicle> findByCustomer_UserId(Long userId);
}