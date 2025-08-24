package za.ac.cput.service.user.employee;

import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.service.IService;

import java.util.List;

public interface IManagerService extends IService<Manager, Long> {
    List<Manager> getAllManagers();
}
