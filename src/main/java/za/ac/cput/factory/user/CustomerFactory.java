// Thaakirah Watson, 230037550
package za.ac.cput.factory.user;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.generic.Address;
import za.ac.cput.domain.generic.Contact;
import za.ac.cput.util.Helper;

import java.time.LocalDate;

public class CustomerFactory {

    public static Customer createCustomer(
            Long userId,
            String userName,
            String userSurname,
            Boolean isActive,
            Customer.RoleDescription roleDescription,
            Login login,
            Address address,
            Contact contact,
            LocalDate customerDOB
    ) {
        // Basic validations
        if (!Helper.validateStringDetails(userName)) {
            throw new IllegalArgumentException("Customer requires a name");
        }
        if (!Helper.validateStringDetails(userSurname)) {
            throw new IllegalArgumentException("Customer requires a surname");
        }
        if (customerDOB == null) {
            throw new IllegalArgumentException("Customer date of birth cannot be null");
        }

        return new Customer.Builder()
                .setUserId(userId)
                .setUserName(userName)
                .setUserSurname(userSurname)
                .setIsActive(isActive != null ? isActive : true)
                .setRoleDescription(roleDescription != null ? roleDescription : Customer.RoleDescription.CLIENT)
                .setLogin(login)
                .setAddress(address)
                .setContact(contact)
                .setCustomerDOB(customerDOB)
                .build();
    }
}
