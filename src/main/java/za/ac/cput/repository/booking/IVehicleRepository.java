//Thaakirah Watson, 230037550
package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.booking.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, Long> {
    // Find vehicle by plate number
    Optional<Vehicle> findByCarPlateNumber(String carPlateNumber);

    @Query("SELECT v FROM Vehicle v WHERE v.carPlateNumber = :plateNumber")
    List<Vehicle> findAllByCarPlateNumber(@Param("plateNumber") String plateNumber);

    // Find all vehicles by customer ID
    java.util.List<Vehicle> findByCustomer_UserId(Long userId);
}