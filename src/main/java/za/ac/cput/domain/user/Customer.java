/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Domain class for Customer
 */

package za.ac.cput.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // or GenerationType.IDENTITY, depending on DB config
    private String customerID;
    private LocalDate customerDOB;

    // Private constructor used by Builder
    private Customer(Builder builder) {
        this.customerID = builder.customerID;
        this.customerDOB = builder.customerDOB;
    }

    // Getters only
    public String getCustomerID() {
        return customerID;
    }

    public LocalDate getCustomerDOB() {
        return customerDOB;
    }

    // Builder inner class
    public static class Builder {
        private String customerID;
        private LocalDate customerDOB;

        public Builder setCustomerID(String customerID) {
            this.customerID = customerID;
            return this;
        }

        public Builder setCustomerDOB(LocalDate customerDOB) {
            this.customerDOB = customerDOB;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
