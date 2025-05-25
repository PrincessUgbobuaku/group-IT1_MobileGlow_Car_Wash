package za.ac.cput.domain.user;

import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.domain.user.employee.WashAttendant;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

public class User {

    private String userId;
    private String userName;
    private String userSurname;
    /*private Accountant accountant;          //Composition relationship
    private Manager manager;                //Composition relationship
    private Customer customer;              //Composition relationship
    private WashAttendant washAttendant;    //Composition relationship
    private Contact contact;                //Composition relationship
    private Address address;           */     //Composition relationship

    public User(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userSurname = builder.userSurname;
      /*  this.accountant = builder.accountant;
        this.manager = builder.manager;                 //Composition relationship
        this.customer = builder.customer;               //Composition relationship
        this.washAttendant = builder.washAttendant;     //Composition relationship
        this.contact = builder.contact;                 //Composition relationship
        this.address = builder.address;       */          //Composition relationship
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    /*public Accountant getAccountant() {
        return accountant;
    }

    public Manager getManager() {
        return manager;
    }

    public Customer getCustomer() {return customer; }

    public WashAttendant getWashAttendant() {return washAttendant; }

    public Contact getContact() {return contact; }

    public Address getAddress() {return address; }*/

    @Override
    public String toString() {
        return "User{" +
                "\nuserId ='" + userId +
                "\nuserName ='" + userName +
                "\nuserSurname = '" + userSurname +
                /*"\naccountant = " + accountant.isHasTaxFillingAuthority() +
                "\nmanager = " + manager.getHireDate() +
                *//*"\ncustomer = " + customer.getFormattedDateOfBirth() +*//*
                "\nIs washAttendant full time = " + washAttendant.isFullTime() +
                "\nwashAttendant shift hours = " + washAttendant.getShiftHours() +
                "\nContact = " + contact.getPhoneNumber() +
                "\nStreet Number: " + address.getStreetNumber() +
                "\nStreet Name = " + address.getStreetName() +
                "\nCity: " + address.getCity() +
                "\nzip code = " + address.getPostalCode() +*/
                '}';
    }

    public static class Builder {
        private String userId;
        private String userName;
        private String userSurname;
       /* private Accountant accountant;
        private Manager manager;
        private Customer customer;
        private WashAttendant washAttendant;
        private Contact contact;
        private Address address;*/

        public Builder setUserId(String userId) {
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

       /* public Builder setAccountant(Accountant accountant) {
            this.accountant = accountant;
            return this;
        }

        public Builder setManager(Manager manager) {
            this.manager = manager;
            return this;
        }

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder setWashAttendant(WashAttendant washAttendant) {
            this.washAttendant = washAttendant;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }*/

        public Builder copy(User user) {
            this.userId = user.userId;
            this.userName = user.userName;
            this.userSurname = user.userSurname;
           /* this.accountant = user.accountant;
            this.manager = user.manager;
            this.customer = user.customer;
            this.washAttendant = user.washAttendant;
            this.contact = user.contact;
            this.address = user.address;*/
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}






