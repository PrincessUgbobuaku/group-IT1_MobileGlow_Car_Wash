package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

public class AccountantFactory {

    public static Accountant createAccountant(String userName, String userSurname,
                                              boolean isActive, User.RoleDescription roleDescription,
                                              boolean hasTaxFillingAuthority,
                                              Contact contact, Address address, Login login) {

        return new Accountant.Builder()
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setIsActive(isActive)
                .setRoleDescription(roleDescription)
                .setHasTaxFillingAuthority(hasTaxFillingAuthority)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }

    public static Accountant createAccountantWithId(Long userId, String userName, String userSurname,
                                                    boolean isActive, User.RoleDescription roleDescription,
                                                    boolean hasTaxFillingAuthority,
                                                    Contact contact, Address address, Login login) {

        return new Accountant.Builder()
                .setUserId(userId)
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setIsActive(isActive)
                .setRoleDescription(roleDescription)
                .setHasTaxFillingAuthority(hasTaxFillingAuthority)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }
}