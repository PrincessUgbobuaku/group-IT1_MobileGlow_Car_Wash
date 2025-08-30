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
        System.out.println("Received service input: " + cleaningServiceInput);

        if (cleaningServiceRepository.existsByServiceName(cleaningServiceInput.getServiceName())) {
            throw new IllegalArgumentException("CleaningService already exists with name: " + cleaningServiceInput.getServiceName());
        }

        CleaningService created = CleaningServiceFactory.createCleaningService(
                cleaningServiceInput.getServiceName(),
                cleaningServiceInput.getPriceOfService(),
                cleaningServiceInput.getDuration(),
                cleaningServiceInput.getCategory()
        );

        if (created == null) {
            throw new IllegalArgumentException("Invalid CleaningService data");
        }

        return cleaningServiceRepository.save(created);
    }

    @Override
    public CleaningService read(Long id) {
        return cleaningServiceRepository.findById(id).orElse(null);
    }

    @Override
    public CleaningService update(CleaningService cleaningService) {
        if (!cleaningServiceRepository.existsById(cleaningService.getCleaningServiceID())) {
            throw new IllegalArgumentException("Service with ID " + cleaningService.getCleaningServiceID() + " not found.");
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
    private void addIfNotExists(String serviceName, double price, double duration, String category) {
        if (!cleaningServiceRepository.existsByServiceName(serviceName)) {
            CleaningService cleaningService = CleaningServiceFactory.createCleaningService(
                    serviceName, price, duration, category
            );
            cleaningServiceRepository.save(cleaningService);
        }
    }
}