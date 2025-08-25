//Thaakirah Watson, 230037550
package za.ac.cput.domain.user;

import jakarta.persistence.*;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends User {
    private LocalDate customerDOB;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles = new HashSet<>();

    protected Customer() {
        super();
    }

    private Customer(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userSurname = builder.userSurname;
        this.isActive = builder.isActive;
        this.roleDescription = builder.roleDescription;
        this.login = builder.login;
        this.address = builder.address;
        this.contact = builder.contact;
        this.customerDOB = builder.customerDOB;
        this.vehicles = builder.vehicles;
    }

    public LocalDate getCustomerDOB() {
        return customerDOB;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerDOB=" + customerDOB +
                ", vehicles=" + vehicles.size() +
                ", userId=" + getUserId() +
                ", userName='" + getUserName() + '\'' +
                ", userSurname='" + getUserSurname() + '\'' +
                ", isActive=" + getIsActive() +
                ", roleDescription=" + getRoleDescription() +
                ", contact=" + getContact() +
                ", address=" + getAddress() +
                ", login=" + getLogin() +
                '}';
    }

    public static class Builder {
        private Long userId;
        private String userName;
        private String userSurname;
        private Boolean isActive;
        private User.RoleDescription roleDescription;
        private Login login;
        private Address address;
        private Contact contact;
        private LocalDate customerDOB;
        private Set<Vehicle> vehicles = new HashSet<>();

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }
        public Builder setUserSurname(String userSurname) {
            this.userSurname = userSurname;
            return this;
        }
        public Builder setIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public Builder setRoleDescription(User.RoleDescription roleDescription) {
            this.roleDescription = roleDescription;
            return this;
        }
        public Builder setLogin(Login login) {
            this.login = login;
            return this;
        }
        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }
        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }
        public Builder setCustomerDOB(LocalDate customerDOB) {
            this.customerDOB = customerDOB;
            return this;
        }
        public Builder setVehicles(Set<Vehicle> vehicles) {
            this.vehicles = vehicles;
            return this;
        }

        public Builder copy(Customer customer) {
            this.userId = customer.userId;
            this.userName = customer.userName;
            this.userSurname = customer.userSurname;
            this.isActive = customer.isActive;
            this.roleDescription = customer.roleDescription;
            this.login = customer.login;
            this.address = customer.address;
            this.contact = customer.contact;
            this.customerDOB = customer.customerDOB;
            this.vehicles = customer.vehicles;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}