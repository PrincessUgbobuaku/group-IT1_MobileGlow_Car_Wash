//Thaakirah Watson, 230037550
package za.ac.cput.service.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.service.IService;

import java.util.List;
import java.util.Optional;

public interface IVehicleService extends IService<Vehicle, Long> {
    //find vehicle by plate number
    Optional<Vehicle> findByPlateNumber(String plateNumber);

    //find vehicle by customerId
    List<Vehicle> findByCustomerId(Long customerId);
}
