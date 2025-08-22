package za.ac.cput.repository.user;

import za.ac.cput.domain.user.Login;
import za.ac.cput.repository.IRepository;

import java.util.Set;

public interface LoginRepository extends IRepository<Login, String> {
    Set<Login> getLogins();
}
