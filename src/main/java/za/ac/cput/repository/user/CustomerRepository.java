/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository class for Customer
 */

package za.ac.cput.repository.user;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.repository.IRepository;

import java.util.Set;

public interface CustomerRepository extends IRepository<Customer, String> {
        Set<Customer> getAll();
    }
