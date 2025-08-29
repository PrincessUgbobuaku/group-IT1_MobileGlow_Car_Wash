//Thaakirah Watson, 230037550
package za.ac.cput.service.user;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.service.IService;

import java.util.List;

public interface ICustomerService extends IService<Customer, Long> {
    //Find customer by surname
    List<Customer> findBySurname(String surname);

    List<Customer> findAll(); //needed for customer and vehicle classes to find all customers and vehicles
    //find active customers
    List<Customer> findActiveCustomers();
}
