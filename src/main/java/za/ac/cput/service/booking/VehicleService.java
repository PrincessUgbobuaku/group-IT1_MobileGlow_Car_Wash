//Thaakirah Watson, 230037550
package za.ac.cput.service.booking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.repository.booking.IVehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class VehicleService implements IVehicleService {

    private static final Logger logger = Logger.getLogger(VehicleService.class.getName());
    private final IVehicleRepository repository;

    public VehicleService(IVehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @Override
    public Vehicle read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Vehicle> findByPlateNumber(String plateNumber) {
        try {
            return repository.findByCarPlateNumber(plateNumber);
        } catch (org.hibernate.NonUniqueResultException e) {
            logger.warning("Multiple vehicles found with plate number: " + plateNumber +
                    ". This indicates duplicate data in the database.");

            // Get the first vehicle as a workaround
            List<Vehicle> allVehicles = repository.findAll();
            Optional<Vehicle> firstMatch = allVehicles.stream()
                    .filter(v -> plateNumber.equals(v.getCarPlateNumber()))
                    .findFirst();

            if (firstMatch.isPresent()) {
                logger.warning("Returning first matching vehicle for plate: " + plateNumber);
            }
            return firstMatch;
        }
    }

    @Override
    public List<Vehicle> findByCustomerId(Long customerId) {
        return repository.findByCustomer_UserId(customerId);
    }

    @Override
    public List<Vehicle> findAll() {
        return repository.findAll();
    }

    // Helper method to clean up duplicate plates (for testing/maintenance)
    public int removeDuplicatePlates(String plateNumber) {
        List<Vehicle> allVehicles = repository.findAll();
        List<Vehicle> duplicates = allVehicles.stream()
                .filter(v -> plateNumber.equals(v.getCarPlateNumber()))
                .toList();

        if (duplicates.size() > 1) {
            logger.info("Found " + duplicates.size() + " vehicles with plate " + plateNumber);
            // Keep the first one, delete the rest
            for (int i = 1; i < duplicates.size(); i++) {
                repository.deleteById(duplicates.get(i).getVehicleID());
                logger.info("Deleted duplicate vehicle with ID: " + duplicates.get(i).getVehicleID());
            }
            return duplicates.size() - 1; // Return number of duplicates removed
        }
        return 0;
    }
}