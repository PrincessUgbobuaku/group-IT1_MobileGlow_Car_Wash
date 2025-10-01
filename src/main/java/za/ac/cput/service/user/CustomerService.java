//Thaakirah Watson, 230037550
package za.ac.cput.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.repository.user.ICustomerRepository;

import java.util.List;

@Service
@Transactional
public class CustomerService implements ICustomerService {

    private final ICustomerRepository repository;

    @Autowired
    private UserService userService;

    public CustomerService(ICustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(Customer customer) {
        Customer encryptedCustomer = userService.encryptUserPassword(customer);
        return repository.save(encryptedCustomer);
    }

    @Override
    public Customer read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        return repository.save(customer); // ✅ simpler + avoids null returns
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Customer> findBySurname(String surname) {
        return repository.findByUserSurname(surname);
    }

    @Override
    public List<Customer> findActiveCustomers() {
        return repository.findByIsActive(true);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }
}
