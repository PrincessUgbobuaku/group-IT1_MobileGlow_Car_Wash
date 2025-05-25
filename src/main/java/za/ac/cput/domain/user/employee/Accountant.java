/*
    AccountantDomain
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.domain.user.employee;

import jakarta.persistence.Entity;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

@Entity
public class Accountant extends User {
    private boolean hasTaxFillingAuthority;
    private String employeeType;

    protected Accountant() {
        super();
    }

    private Accountant(Builder builder){
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userSurname = builder.userSurname;
        this.isActive = builder.isActive;
        this.roleDescription = builder.roleDescription;
        this.employeeType = builder.employeeType;
        this.hasTaxFillingAuthority = builder.hasTaxFillingAuthority;
        this.contact = builder.contact;
        this.address = builder.address;
        this.login= builder.login;
    }

    public boolean getHasTaxFillingAuthority() {
        return hasTaxFillingAuthority;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    @Override
    public String toString() {
        return "Accountant{" +
                "hasTaxFillingAuthority=" + getHasTaxFillingAuthority() +
                ", employeeType='" + getEmployeeType() + '\'' +
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
        private String employeeType;
        private boolean hasTaxFillingAuthority;
        private Contact contact;
        private Address address;
        private Login login;

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

        public Builder setEmployeeType(String employeeType) {
            this.employeeType = employeeType;
            return this;
        }

        public Builder setHasTaxFillingAuthority(boolean hasTaxFillingAuthority) {
            this.hasTaxFillingAuthority = hasTaxFillingAuthority;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setLogin(Login login) {
            this.login = login;
            return this;
        }

        public Builder copy(Accountant accountant) {
            this.userId = accountant.userId;
            this.userName = accountant.userName;
            this.userSurname = accountant.userSurname;
            this.isActive = accountant.isActive;
            this.roleDescription = accountant.roleDescription;
            this.employeeType = accountant.employeeType;
            this.hasTaxFillingAuthority = accountant.hasTaxFillingAuthority;
            this.contact = accountant.contact;
            this.address = accountant.address;
            this.login= accountant.login;
            return this;
        }

        public Accountant build() {
            return new Accountant(this);
        }
    }
}