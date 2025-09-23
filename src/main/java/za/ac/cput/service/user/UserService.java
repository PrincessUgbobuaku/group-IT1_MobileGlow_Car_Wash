package za.ac.cput.service.user;

//This class is used for to encrypt when all the users create passwords. Security reasons.
//Also, it has to be centralized so that it is easy to update if we decide to add other users.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.domain.user.employee.Accountant;
import za.ac.cput.domain.user.employee.Manager;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.util.PasswordHelper;
@Service
public class UserService {

    private final PasswordHelper passwordHelper;

    @Autowired
    public UserService(PasswordHelper passwordHelper) {
        this.passwordHelper = passwordHelper;
    }

    //Encrypt the user's password and return a new User with encrypted Login
    public <T extends User> T encryptUserPassword(T user) {
        if (user.getLogin() != null) {
            Login encryptedLogin = passwordHelper.encodeLoginPassword(user.getLogin());

            // Create a new Builder for the specific user type
            if (user instanceof Manager manager) {
                return (T) new Manager.Builder()
                        .copy(manager)
                        .setLogin(encryptedLogin)
                        .build();
            } else if (user instanceof WashAttendant washAttendant) {
                return (T) new WashAttendant.Builder()
                        .copy(washAttendant)
                        .setLogin(encryptedLogin)
                        .build();
            } else if (user instanceof Accountant accountant) {
                return (T) new Accountant.Builder()
                        .copy(accountant)
                        .setLogin(encryptedLogin)
                        .build();
            } else if (user instanceof Customer customer) {
                return (T) new Customer.Builder()
                        .copy(customer)
                        .setLogin(encryptedLogin)
                        .build();
            }
        }
        return user;
    }
}
