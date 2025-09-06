package za.ac.cput.service.user.employee.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.employee.Accountant;
//import za.ac.cput.repository.user.employee.impl.IAccountantRepository;
import za.ac.cput.repository.user.employee.IAccountantRepository;
import za.ac.cput.service.user.employee.IAccountantService;

import java.util.List;

@Service
public class AccountantServiceImpl implements IAccountantService {

    private final IAccountantRepository accountantRepository;

    @Autowired
    public AccountantServiceImpl(IAccountantRepository accountantRepository) {
        this.accountantRepository = accountantRepository;
    }

    @Override
    public Accountant create(Accountant accountant) {
        return accountantRepository.save(accountant);
    }

    @Override
    public Accountant read(Long Id) {
        return accountantRepository.findById(Id).orElse(null);
    }

    @Override
    public Accountant update(Accountant accountant) {
        return accountantRepository.save(accountant);
    }

    @Override
    public List<Accountant> getAllAccountants() {
        return accountantRepository.findAll();
    }

    @Override
    public boolean delete(Long Id) {
        accountantRepository.deleteById(Id);
        return false;
    }

    @Override
    public List<Accountant> findAll() {
        return List.of();
    }
}
