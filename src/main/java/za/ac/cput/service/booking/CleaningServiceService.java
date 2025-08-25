package za.ac.cput.service.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.repository.booking.ICleaningServiceRepository;
import za.ac.cput.util.Helper;

import java.util.List;

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
        System.out.println("Received service input: " + cleaningServiceInput);

        if (cleaningServiceRepository.existsByServiceName(cleaningServiceInput.getServiceName())) {
            throw new IllegalArgumentException("CleaningServiceService: Service already exists");
        }

        CleaningService created = CleaningServiceFactory.createCleaningService(
                cleaningServiceInput.getServiceName(),
                cleaningServiceInput.getPriceOfService(),
                cleaningServiceInput.getDuration(),
                cleaningServiceInput.getCategory() // ✅ NEW
        );

        if (created == null) {
            throw new IllegalArgumentException("Invalid CleaningService data");
        }

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

    // ✅ Optional: Add support for querying by category
    public List<CleaningService> getByCategory(String category) {
        return cleaningServiceRepository.findAllByCategory(category);
    }

    // Optional: Preload services
    private void addIfNotExists(CleaningService.ServiceName serviceName, double price, double duration, String category) {
        if (!cleaningServiceRepository.existsByServiceName(serviceName)) {
            String id = Helper.generateID();
            CleaningService cleaningService = new CleaningService.Builder()
                    .setCleaningServiceID(id)
                    .setServiceName(serviceName)
                    .setPriceOfService(price)
                    .setDuration(duration)
                    .setCategory(category) // ✅ NEW
                    .build();
            cleaningServiceRepository.save(cleaningService);
        }
    }
}