//Thaakirah Watson, 230037550
package za.ac.cput.service.user;

import org.springframework.web.multipart.MultipartFile;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.service.IService;

import java.io.IOException;
import java.util.List;

public interface ICustomerService extends IService<Customer, Long> {
    //Find customer by surname
    List<Customer> findBySurname(String surname);

    List<Customer> findAll(); //needed for customer and vehicle classes to find all customers and vehicles
    //find active customers
    List<Customer> findActiveCustomers();

    Customer create(Customer customer, MultipartFile imageFile) throws IOException;

    // Update existing customer by ID with optional image
    Customer updateCustomer(Long id, Customer customer, MultipartFile imageFile) throws IOException;

}
