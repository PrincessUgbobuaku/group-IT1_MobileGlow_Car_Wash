package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class ManagerFactory {

    public static Manager createManager(String userName,
                                        String userSurname,
                                        User.RoleDescription roleDescription,
                                        boolean isActive,
                                        LocalDate hireDate,
                                        String employeeType,
                                        Contact contact,
                                        Address address,
                                        Login login) {

        if (!Helper.validateStringDetails(userName) ||
                !Helper.validateStringDetails(userSurname) ||
                !Helper.isValidHireDate(hireDate) ||
                !Helper.validateStringDetails(employeeType)) {
            return null;
        }

        return new Manager.Builder()
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setRoleDescription(roleDescription)
                .setIsActive(isActive)
                .setHireDate(hireDate)
                .setEmployeeType(employeeType)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }

}
