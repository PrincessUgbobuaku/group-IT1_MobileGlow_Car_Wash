package za.ac.cput.factory.user.employee;

import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.util.Helper;

public class WashAttendantFactory {

    public static WashAttendant createWashAttendant(String userName,
                                                    String userSurname,
                                                    User.RoleDescription roleDescription,
                                                    boolean isActive,
                                                    String employeeType,
                                                    boolean isFullTime,
                                                    int shiftHours,
                                                    Contact contact,
                                                    Address address,
                                                    Login login) {

        if (!Helper.validateStringDetails(userName) ||
                !Helper.validateStringDetails(userSurname) ||
                !Helper.validateStringDetails(employeeType) ||
                !Helper.validateShiftHours(shiftHours) ||
                contact == null ||
                address == null ||
                login == null) {
            return null;
        }

        return new WashAttendant.Builder()
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setRoleDescription(roleDescription)
                .setIsActive(isActive)
                .setEmployeeType(employeeType)
                .setIsFullTime(isFullTime)
                .setShiftHours(shiftHours)
                .setContact(contact)
                .setAddress(address)
                .setLogin(login)
                .build();
    }
}