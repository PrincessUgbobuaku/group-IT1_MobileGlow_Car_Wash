package za.ac.cput.service.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.Login;
import za.ac.cput.repository.user.ILoginRepository;

import java.util.List;

@Service
public class LoginService implements ILoginService {

    private ILoginRepository loginRepository;

    @Autowired
    public LoginService(ILoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Login create(Login login) {
        return loginRepository.save(login);
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
    public boolean delete(Long Id) {
        loginRepository.deleteById(Id);
        return true;
    }

    
}

