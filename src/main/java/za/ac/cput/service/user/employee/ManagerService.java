package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.repository.user.employee.IManagerRepository;
import za.ac.cput.service.user.UserService;

import java.util.List;

@Service
public class ManagerService implements IManagerService {

    private IManagerRepository managerRepository;

    @Autowired
    private UserService userService;


    @Autowired
    public ManagerService(IManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Manager create(Manager manager) {
        Manager encryptManager = userService.encryptUserPassword(manager);
        return managerRepository.save(encryptManager);
    }

    @Override
    public Manager read(Long Id) {
        return managerRepository.findById(Id).orElse(null);
    }

    @Override
    public Manager update(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public boolean delete(Long Id) {
        managerRepository.deleteById(Id);
        return true;
    }

   
}
