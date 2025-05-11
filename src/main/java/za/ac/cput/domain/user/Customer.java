/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Domain class for Customer
 */

package za.ac.cput.domain.user;

import java.time.LocalDate;

public class Customer {
    private String customerID;
    private LocalDate customerDOB;

    // Default constructor
    public Customer() {
    }

    // Constructor with both fields
    public Customer(String customerID, LocalDate customerDOB) {
        this.customerID = customerID;
        this.customerDOB = customerDOB;
    }

    // Getters and setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public LocalDate getCustomerDOB() {
        return customerDOB;
    }

    public void setCustomerDOB(LocalDate customerDOB) {
        this.customerDOB = customerDOB;
    }

    // @Override
    // public String toString() {
    //     return "Customer{" +
    //             "customerID='" + customerID + '\'' +
    //             ", customerDOB=" + customerDOB +
    //             '}';
    // }
}
