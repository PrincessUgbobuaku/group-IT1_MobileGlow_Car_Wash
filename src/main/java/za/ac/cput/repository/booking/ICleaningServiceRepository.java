package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.CleaningService;

import java.util.List;
import java.util.Optional;

public interface ICleaningServiceRepository extends JpaRepository<CleaningService, Long> {

    // Check existence by service name
    boolean existsByServiceName(String serviceName);

    // Find one by service name
    Optional<CleaningService> findByServiceName(String serviceName);

    // Find all services by category
    List<CleaningService> findAllByCategory(String category);
}