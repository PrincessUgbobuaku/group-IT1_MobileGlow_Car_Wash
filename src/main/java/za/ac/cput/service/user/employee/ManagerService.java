package za.ac.cput.service.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.repository.user.employee.IManagerRepository;

import java.util.List;

@Service
public class ManagerService implements IManagerService {

    private IManagerRepository managerRepository;

    @Autowired
    public ManagerService(IManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Manager create(Manager manager) {
        return managerRepository.save(manager);
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

    //this method is not supposed to be here but because it is in IService that is why I have implemented.
    @Override
    public List<Manager> findAll() {
        return null;
    }
}
