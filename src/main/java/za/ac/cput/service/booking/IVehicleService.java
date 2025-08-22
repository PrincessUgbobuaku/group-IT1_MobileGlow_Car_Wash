package za.ac.cput.service.booking;

import org.springframework.stereotype.Repository;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.service.IService;

import java.util.List;

@Repository
public interface IVehicleService extends IService<CleaningService, String> {

    List<CleaningService> getAll();

}
