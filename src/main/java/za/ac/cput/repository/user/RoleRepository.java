package za.ac.cput.repository.user;

import za.ac.cput.domain.user.Role;
import za.ac.cput.repository.IRepository;

import java.util.Set;

public interface RoleRepository extends IRepository<Role, String> {
    Set<Role> getRoles();
}
