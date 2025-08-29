package za.ac.cput.repository.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Login;

@Repository
public interface ILoginRepository extends JpaRepository<Login, Long> {
}
