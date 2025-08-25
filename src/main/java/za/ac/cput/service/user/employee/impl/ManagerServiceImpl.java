package za.ac.cput.service.user.employee.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.repository.user.employee.impl.IManagerRepository;
import za.ac.cput.service.user.employee.IManagerService;

import java.util.List;

@Service
public class ManagerServiceImpl implements IManagerService {

    private IManagerRepository managerRepository;

    @Autowired
    public ManagerServiceImpl(IManagerRepository managerRepository) {
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
    public void delete(Long Id) {
        managerRepository.deleteById(Id);
    }
}
