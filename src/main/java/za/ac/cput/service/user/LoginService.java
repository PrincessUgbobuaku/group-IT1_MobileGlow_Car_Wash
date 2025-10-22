package za.ac.cput.service.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;
import za.ac.cput.repository.user.ILoginRepository;
import za.ac.cput.repository.user.IUserRepository;

import java.util.List;

@Service
public class LoginService implements ILoginService {

    private ILoginRepository loginRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private IUserRepository userRepository;

    @Autowired
    public LoginService(ILoginRepository loginRepository,
                        IUserRepository userRepository) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.userRepository = userRepository;
    }

    @Override
    public Login create(Login login) {
        //Encrypt password before saving.
        login = new Login.Builder()
                .setEmailAddress(login.getEmailAddress())
                .setPassword(passwordEncoder.encode(login.getPassword()))
                .build();
        return loginRepository.save(login);
    }





    public Login findLoginByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getLogin() != null) {
            return user.getLogin(); // directly return the associated Login
        }
        return null;
    }







    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User findUserByLogin(Login login) {
        return userRepository.findByLogin(login);
    }


    @Override
    public Login read(Long Id) {
        return loginRepository.findById(Id).orElse(null);
    }

    @Override
    public Login update(Login login) {
        return loginRepository.save(login);
    }

    @Override
    public List<Login> getAllLogins() {
        return loginRepository.findAll();
    }

    @Override
    public Login findByEmailAddress(String email) {
        return loginRepository.findByEmailAddress(email);
    }

    @Override
    public boolean delete(Long Id) {
        loginRepository.deleteById(Id);
        return true;
    }

    @Override
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        Login login = findByEmailAddress(email);
        if (login == null) {
            return false;
        }
        if (!checkPassword(currentPassword, login.getPassword())) {
            return false;
        }
        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        Login updatedLogin = new Login.Builder()
                .setLoginID(login.getLoginID())
                .setEmailAddress(login.getEmailAddress())
                .setPassword(encodedNewPassword)
                .build();
        update(updatedLogin);
        return true;
    }
}

