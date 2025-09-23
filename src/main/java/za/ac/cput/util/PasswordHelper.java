package za.ac.cput.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.user.Login;

@Component
public class PasswordHelper {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordHelper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder; // strength 12
    }

    // Encrypt the password in a Login object and return a new Login object with encrypted password
    public Login encodeLoginPassword(Login login) {
        if (login == null || login.getPassword() == null) return login;

        String encodedPassword = passwordEncoder.encode(login.getPassword());
        return new Login.Builder()
                .setLoginID(login.getLoginID())
                .setEmailAddress(login.getEmailAddress())
                .setPassword(encodedPassword)
                .build();
    }

    // Optional: check if raw password matches encoded password
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
