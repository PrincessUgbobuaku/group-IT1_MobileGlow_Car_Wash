package za.ac.cput.repository.user;

//This interface is needed because it is used to fetch full user domain information by just using email
//Since login and User have a OneToOne relationship.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Login;
import za.ac.cput.domain.user.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByLogin_EmailAddress(String emailAddress);
    User findByLogin(Login login);
}
