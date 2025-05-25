/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository class for Vehicle
 */

package za.ac.cput.repository.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.repository.IRepository;
import java.util.Set;

public interface VehicleRepository extends IRepository<Vehicle, String> {
    Set<Vehicle> getAll();
}
