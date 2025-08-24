package za.ac.cput.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}
