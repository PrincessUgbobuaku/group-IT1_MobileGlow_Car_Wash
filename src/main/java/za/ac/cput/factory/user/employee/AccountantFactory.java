package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class AccountantFactory {

    public static Accountant createAccountant(String userName, String userSurname,
                                              boolean isActive, User.RoleDescription roleDescription,
                                              String employeeType, boolean hasTaxFillingAuthority,
                                              Contact contact, Address address, Login login) {

        return new Accountant.Builder()
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setIsActive(isActive)
                .setRoleDescription(roleDescription)
                .setEmployeeType(employeeType)
                .setHasTaxFillingAuthority(hasTaxFillingAuthority)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }

    public static Accountant createAccountant(String userName, String userSurname,
                                              boolean isActive, User.RoleDescription roleDescription,
                                              String employeeType, boolean hasTaxFillingAuthority,
                                              LocalDate hireDate,
                                              Contact contact, Address address, Login login) {

        if (!Helper.validateStringDetails(userName) ||
                !Helper.validateStringDetails(userSurname) ||
                !Helper.isValidHireDate(hireDate) ||
                !Helper.validateStringDetails(employeeType)) {
            return null;
        }

        return new Accountant.Builder()
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setIsActive(isActive)
                .setRoleDescription(roleDescription)
                .setEmployeeType(employeeType)
                .setHasTaxFillingAuthority(hasTaxFillingAuthority)
                .setHireDate(hireDate)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }

    /*public static Accountant createAccountantWithId(Long userId, String userName, String userSurname,
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
    }*/
}