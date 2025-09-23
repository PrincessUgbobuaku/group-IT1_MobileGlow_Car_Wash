package za.ac.cput.factory.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.cput.domain.user.Login;
import za.ac.cput.util.Helper;

public class LoginFactory {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public static Login createLogin(String emailAddress, String password) {

        if (!Helper.isValidEmail(emailAddress) ||
                !Helper.isValidPassword(password)) {
            return null;
        }

        // Encode the password before building the Login object
        String encodedPassword = passwordEncoder.encode(password);

        return new Login.Builder()
                .setEmailAddress(emailAddress)
                .setPassword(encodedPassword)
                .build();
    }

}
