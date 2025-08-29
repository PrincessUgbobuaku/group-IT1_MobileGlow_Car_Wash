package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.service.IService;

import java.util.List;

public interface IManagerService extends IService<Manager, Long> {
    List<Manager> getAllManagers();
}
