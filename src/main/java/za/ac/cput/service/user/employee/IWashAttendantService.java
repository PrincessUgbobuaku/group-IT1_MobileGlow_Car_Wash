package za.ac.cput.service.user.employee;

import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.service.IService;

import java.util.List;

public interface IWashAttendantService extends IService<WashAttendant, Long> {
    List<WashAttendant> getAllWashAttendants();

//    List<WashAttendant> findAll();
}
