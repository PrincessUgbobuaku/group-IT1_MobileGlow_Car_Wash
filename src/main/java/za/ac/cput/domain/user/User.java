package za.ac.cput.domain.user;

import jakarta.persistence.*;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;
    protected String userName;
    protected String userSurname;
    protected Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_description", length = 50)
    protected User.RoleDescription roleDescription;

    public enum RoleDescription {
        CLIENT,
        EMPLOYEE,
    }

   @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_Id", referencedColumnName = "contact_Id")
    protected Contact contact;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "addressid", referencedColumnName = "addressid")
    protected Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "loginid", referencedColumnName = "loginid")
    protected Login login;

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public RoleDescription getRoleDescription() {
        return roleDescription;
    }

    public Contact getContact() {
        return contact;
    }

    public Address getAddress() {
        return address;
    }

    public Login getLogin() {
        return login;
    }

    @Override
    public abstract String toString();
}






