package za.ac.cput.repository.user.employee;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.employee.Manager;

@Repository
public interface IManagerRepository  extends JpaRepository<Manager, Long> {
}
