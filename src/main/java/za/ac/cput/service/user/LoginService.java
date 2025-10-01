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

    
}

