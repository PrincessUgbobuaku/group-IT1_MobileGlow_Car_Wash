package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.Vehicle;

//Thaakirah Watson, 230037550

import za.ac.cput.domain.booking.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, String> {

    // already has findById
    Optional<Vehicle> findByVehicleID(String vehicleID);

    // find vehicles belonging to a customer
    List<Vehicle> findByCustomer_CustomerID(String customerID);

    // find by plate number
    Optional<Vehicle> findByCarPlateNumber(String carPlateNumber);


}
