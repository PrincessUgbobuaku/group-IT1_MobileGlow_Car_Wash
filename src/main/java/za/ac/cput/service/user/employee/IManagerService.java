package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.service.IService;

import java.io.IOException;
import java.util.List;

public interface IManagerService extends IService<Manager, Long> {
    List<Manager> getAllManagers();
    Manager create(Manager manager, MultipartFile imageFile) throws IOException;
    Manager update(Manager manager, MultipartFile imageFile) throws IOException;
    Manager updateManager(Long id, Manager updatedManager, MultipartFile imageFile);
}
