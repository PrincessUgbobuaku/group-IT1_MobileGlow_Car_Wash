package za.ac.cput.repository.user;

import za.ac.cput.domain.user.UserRole;
import za.ac.cput.repository.IRepository;

import java.util.Set;

public interface UserRoleRepository extends IRepository<UserRole, String> {
    Set<UserRole> getUserRoles();
}
