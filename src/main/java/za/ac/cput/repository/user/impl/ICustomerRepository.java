/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository implementation class for Customer
 */

package za.ac.cput.repository.user.impl;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.repository.user.CustomerRepository;

import java.util.HashSet;
import java.util.Set;

public class ICustomerRepository implements CustomerRepository {
    private static ICustomerRepository repository = null;
    private Set<Customer> customers = new HashSet<>();

    private ICustomerRepository() {}

    public static ICustomerRepository getRepository() {
        if (repository == null) repository = new ICustomerRepository();
        return repository;
    }

    @Override
    public Customer create(Customer customer) {
        customers.add(customer);
        return customer;
    }

    @Override
    public Customer read(String id) {
        return customers.stream()
                .filter(c -> c.getCustomerID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Customer update(Customer customer) {
        Customer old = read(customer.getCustomerID());
        if (old != null) {
            customers.remove(old);
            customers.add(customer);
            return customer;
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        Customer customer = read(id);
        if (customer != null) {
            customers.remove(customer);
            return true;
        }
        return false;
    }

    @Override
    public Set<Customer> getAll() {
        return customers;
    }
}