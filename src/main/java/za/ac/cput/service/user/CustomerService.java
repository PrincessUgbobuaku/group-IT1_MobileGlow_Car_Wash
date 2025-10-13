//Thaakirah Watson, 230037550
package za.ac.cput.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;
import za.ac.cput.repository.user.ICustomerRepository;

import java.io.IOException;
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
    public Customer create(Customer customer, MultipartFile imageFile) throws IOException {
        Customer newCustomer = new Customer.Builder()
                .copy(customer)
                .setImageName(imageFile.getOriginalFilename())
                .setImageType(imageFile.getContentType())
                .setImageData(imageFile.getBytes())
                .build();
        Customer encryptedCustomer = userService.encryptUserPassword(newCustomer);
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
    public Customer updateCustomer(Long id, Customer updatedCustomer, MultipartFile imageFile) {
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));

        byte[] imageData = existing.getImageData();
        String imageName = existing.getImageName();
        String imageType = existing.getImageType();

        // Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageData = imageFile.getBytes();
                imageName = imageFile.getOriginalFilename();
                imageType = imageFile.getContentType();
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image file", e);
            }
        }

        // Handle login update - merge with existing login to preserve password and other fields
        Login updatedLogin = existing.getLogin();
        if (updatedCustomer.getLogin() != null && updatedCustomer.getLogin().getEmailAddress() != null) {
            updatedLogin = new Login.Builder()
                    .copy(existing.getLogin())
                    .setEmailAddress(updatedCustomer.getLogin().getEmailAddress())
                    .build();
        }

        // Build a new updated instance
        Customer updated = new Customer.Builder()
                .copy(existing)
                .setUserName(updatedCustomer.getUserName())
                .setUserSurname(updatedCustomer.getUserSurname())
                .setIsActive(updatedCustomer.getIsActive() != null ? updatedCustomer.getIsActive() : existing.getIsActive())
                .setRoleDescription(updatedCustomer.getRoleDescription() != null ? updatedCustomer.getRoleDescription() : existing.getRoleDescription())
                .setAddress(updatedCustomer.getAddress() != null ? updatedCustomer.getAddress() : existing.getAddress())
                .setContact(updatedCustomer.getContact() != null ? updatedCustomer.getContact() : existing.getContact())
                .setLogin(updatedLogin)
                .setCustomerDOB(updatedCustomer.getCustomerDOB() != null ? updatedCustomer.getCustomerDOB() : existing.getCustomerDOB())
                .setImageName(imageName)
                .setImageType(imageType)
                .setImageData(imageData)
                .build();

        return repository.save(updated);
    }


   /* @Override
    public Customer update(Customer customer, MultipartFile imageFile) throws IOException {
        Customer updatedCustomer = customer;
        if (imageFile != null && !imageFile.isEmpty()) {
            updatedCustomer = new Customer.Builder()
                    .copy(customer)
                    .setImageName(imageFile.getOriginalFilename())
                    .setImageType(imageFile.getContentType())
                    .setImageData(imageFile.getBytes())
                    .build();
        }
        return repository.save(updatedCustomer);
    }*/

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
