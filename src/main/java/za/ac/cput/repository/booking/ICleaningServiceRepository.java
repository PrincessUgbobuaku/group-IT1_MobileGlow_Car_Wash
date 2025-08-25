package za.ac.cput.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.repository.IRepository;

import java.util.List;
import java.util.Optional;

public interface ICleaningServiceRepository extends JpaRepository<CleaningService, String> {

//        List<CleaningService> getAll(); //going into the database, getting all the objects and returning them as a list
    boolean existsByServiceName(CleaningService.ServiceName serviceName);

    Optional<CleaningService> findByServiceName(CleaningService.ServiceName serviceName); // ✅ method declaration only

    // ✅ New method: fetch all services by category
    List<CleaningService> findAllByCategory(String category);
}


