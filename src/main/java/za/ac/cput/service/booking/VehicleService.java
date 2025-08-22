//Thaakirah Watson, 230037550
        package za.ac.cput.service.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.repository.booking.IVehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final IVehicleRepository repository;

    public VehicleService(IVehicleRepository repository) {
        this.repository = repository;
    }

    public Vehicle create(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public Optional<Vehicle> read(String id) {
        return repository.findById(id);
    }

    public Vehicle update(Vehicle vehicle) {
        if (repository.existsById(vehicle.getVehicleID())) {
            return repository.save(vehicle);
        }
        throw new IllegalArgumentException("Vehicle with ID " + vehicle.getVehicleID() + " does not exist");
    }

    public void delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Vehicle with ID " + id + " does not exist");
        }
    }

    public List<Vehicle> getAll() {
        return repository.findAll();
    }

    // --- extra queries ---
    public List<Vehicle> findByCustomerID(String customerID) {
        return repository.findByCustomer_CustomerID(customerID);
    }

    public Optional<Vehicle> findByPlateNumber(String plateNumber) {
        return repository.findByCarPlateNumber(plateNumber);
    }
}
