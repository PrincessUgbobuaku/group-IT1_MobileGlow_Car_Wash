package za.ac.cput.service.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.repository.booking.ICleaningServiceRepository;

import java.util.List;

@Service
public class CleaningServiceService implements ICleaningServiceService {

    private final ICleaningServiceRepository cleaningServiceRepository;

    @Autowired
    public CleaningServiceService(ICleaningServiceRepository cleaningServiceRepository) {
        this.cleaningServiceRepository = cleaningServiceRepository;
    }

    public CleaningService readByServiceName(String serviceName) {
        return cleaningServiceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + serviceName));
    }

    @Override
    public CleaningService create(CleaningService cleaningServiceInput) {
        System.out.println("=== CLEANING SERVICE CREATE METHOD START ===");
        System.out.println("Received service input: " + cleaningServiceInput);

        try {
            // Step 1: Validate input
            if (cleaningServiceInput == null || cleaningServiceInput.getServiceName() == null) {
                throw new IllegalArgumentException("Service input or service name cannot be null");
            }

            System.out.println("Step 1: Checking if service exists with name: " + cleaningServiceInput.getServiceName());

            // Step 2: Check for duplicates using a simple approach
            try {
                List<CleaningService> allServices = cleaningServiceRepository.findAll();
                System.out.println("Step 2a: Total services in database: " + allServices.size());

                // Simple duplicate check
                for (CleaningService existingService : allServices) {
                    if (existingService.getServiceName() != null &&
                            existingService.getServiceName().equals(cleaningServiceInput.getServiceName())) {
                        System.err.println("Step 2b: DUPLICATE FOUND - Service already exists with name: " + cleaningServiceInput.getServiceName());
                        throw new IllegalArgumentException("CleaningService already exists with name: " + cleaningServiceInput.getServiceName());
                    }
                }

                System.out.println("Step 2c: No duplicate found, proceeding to create");

            } catch (IllegalArgumentException e) {
                // Re-throw IllegalArgumentException (duplicate found)
                System.err.println("Step 2d: Duplicate check failed: " + e.getMessage());
                throw e;
            } catch (Exception e) {
                // Log other repository errors but continue
                System.err.println("Step 2e: Repository error during duplicate check: " + e.getMessage());
                e.printStackTrace();
                // Don't throw here, continue with creation
            }

            System.out.println("Step 3: Creating service with factory...");
            CleaningService created = CleaningServiceFactory.createCleaningService(
                    cleaningServiceInput.getServiceName(),
                    cleaningServiceInput.getPriceOfService(),
                    cleaningServiceInput.getDuration(),
                    cleaningServiceInput.getCategory()
            );

            if (created == null) {
                System.err.println("Step 3a: Factory returned null - validation failed");
                throw new IllegalArgumentException("Invalid CleaningService data");
            }

            System.out.println("Step 3b: Factory created service: " + created);
            System.out.println("Step 4: Saving to repository...");

            try {
                CleaningService saved = cleaningServiceRepository.save(created);
                System.out.println("Step 4a: Successfully saved: " + saved);
                System.out.println("=== CLEANING SERVICE CREATE METHOD SUCCESS ===");
                return saved;
            } catch (Exception saveError) {
                System.err.println("Step 4b: Repository save error: " + saveError.getMessage());
                saveError.printStackTrace();
                throw new IllegalArgumentException("Error saving service: " + saveError.getMessage());
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Step X: IllegalArgumentException caught: " + e.getMessage());
            System.out.println("=== CLEANING SERVICE CREATE METHOD - VALIDATION ERROR ===");
            throw e;
        } catch (Exception e) {
            System.err.println("Step Y: Unexpected error in create method: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== CLEANING SERVICE CREATE METHOD - UNEXPECTED ERROR ===");
            throw e;
        }
    }

    @Override
    public CleaningService read(Long id) {
        return cleaningServiceRepository.findById(id)
                .orElse(null); // or throw an exception if preferred
    }

    @Override
    public CleaningService update(CleaningService cleaningService) {
        if (!cleaningServiceRepository.existsById(cleaningService.getCleaningServiceId())) {
            throw new IllegalArgumentException("Service with ID " + cleaningService.getCleaningServiceId() + " not found.");
        }
        return cleaningServiceRepository.save(cleaningService);
    }

    @Override
    public boolean delete(Long id) {
        if (cleaningServiceRepository.existsById(id)) {
            cleaningServiceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<CleaningService> getAll() {
        return cleaningServiceRepository.findAll();
    }

    public List<CleaningService> getByCategory(String category) {
        return cleaningServiceRepository.findAllByCategory(category);
    }

    // Optional: preload services
    // Useful for preloading default or initial cleaning services.
//    private void addIfNotExists(String serviceName, double price, double duration, String category) {
//        if (!cleaningServiceRepository.existsByServiceName(serviceName)) {
//            CleaningService cleaningService = CleaningServiceFactory.createCleaningService(
//                    serviceName, price, duration, category
//            );
//            cleaningServiceRepository.save(cleaningService);
//        }
//    }
}