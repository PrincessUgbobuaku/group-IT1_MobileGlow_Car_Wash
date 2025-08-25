package za.ac.cput.service.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import za.ac.cput.domain.user.Login;
import za.ac.cput.service.IService;

import java.util.List;

public interface ILoginService extends IService<Login, Long> {
    List<Login> getAllLogins();
}
