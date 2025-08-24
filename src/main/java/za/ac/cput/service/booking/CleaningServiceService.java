package za.ac.cput.service.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.repository.booking.ICleaningServiceRepository;

import java.util.List;

import jakarta.annotation.PostConstruct;
import za.ac.cput.util.Helper;


@Service
public class CleaningServiceService implements ICleaningServiceService {

    private final ICleaningServiceRepository cleaningServiceRepository;

    @Autowired
    public CleaningServiceService(ICleaningServiceRepository cleaningServiceRepository) {
        this.cleaningServiceRepository = cleaningServiceRepository;
    }

    public CleaningService readByServiceName(CleaningService.ServiceName serviceName) {
        return cleaningServiceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + serviceName));
    }

    @Override
    public CleaningService create(CleaningService cleaningServiceInput) {
        // Check if service with the same serviceName exists to avoid duplicates
        if (cleaningServiceRepository.existsByServiceName(cleaningServiceInput.getServiceName())) {
            throw new IllegalArgumentException("CleaningServiceService: Service already exists");
        }

        // Use factory to create a new CleaningService object with generated ID and validated fields
        CleaningService created = CleaningServiceFactory.createCleaningService(
                cleaningServiceInput.getServiceName(),
                cleaningServiceInput.getPriceOfService(),
                cleaningServiceInput.getDuration()
        );

        if (created == null) {
            throw new IllegalArgumentException("Invalid CleaningService data");
        }

        // Save the created entity (with generated ID) to the repository
        return cleaningServiceRepository.save(created);
    }

    @Override
    public CleaningService read(String id) {
        return cleaningServiceRepository.findById(id).orElse(null);
    }

    @Override
    public CleaningService update(CleaningService cleaningService) {
        return cleaningServiceRepository.save(cleaningService);
    }

    @Override
    public boolean delete(String id) {
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

//    @PostConstruct
//    public void loadPredefinedCleaningServices() {
//        addIfNotExists(CleaningService.ServiceName.EXTERIOR_WASH, 150.00, 1.0);
//        addIfNotExists(CleaningService.ServiceName.INTERIOR_CLEANING, 200.00, 1.5);
//        addIfNotExists(CleaningService.ServiceName.WAXING_AND_POLISHING, 230.00, 2.0);
//        addIfNotExists(CleaningService.ServiceName.CERAMIC_COATING, 350.00, 2.5);
//    }

    private void addIfNotExists(CleaningService.ServiceName serviceName, double price, double duration) {
        // Check if a cleaning service with the given serviceName does NOT exist
        if (!cleaningServiceRepository.existsByServiceName(serviceName)) {
            String id = Helper.generateID();
            CleaningService cleaningService = new CleaningService.Builder()
                    .setCleaningServiceID(id)
                    .setServiceName(serviceName)
                    .setPriceOfService(price)
                    .setDuration(duration)
                    .build();
            cleaningServiceRepository.save(cleaningService);
        }
    }
}