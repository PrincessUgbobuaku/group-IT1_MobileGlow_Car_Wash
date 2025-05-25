package za.ac.cput.repository.user.impl;

import za.ac.cput.domain.user.UserRole;
import za.ac.cput.repository.user.UserRoleRepository;

import java.util.HashSet;
import java.util.Set;

public class UserRoleRepositoryImpl implements UserRoleRepository {

    private Set<UserRole> userRolesDB;

    private static UserRoleRepository userRoleRepository;

    UserRoleRepositoryImpl() {userRolesDB = new HashSet<>();}

    public static UserRoleRepository getUserRoleRepository() {
        if (userRoleRepository == null) {
            userRoleRepository = new UserRoleRepositoryImpl();
        }
        return userRoleRepository;
    }

    @Override
    public UserRole create(UserRole userRole) {
        this.userRolesDB.add(userRole);
        return userRole;
    }

    @Override
    public UserRole read(String userRoleId) {
        for (UserRole userRole : userRolesDB) {
            if (userRole.getUserRoleID().equals(userRoleId)) {
                return userRole;
            }
        }
        return null;
    }

    @Override
    public UserRole update(UserRole userRole) {
        UserRole oldUserRole = read(userRole.getUserRoleID().getRoleId());//Since this is a composite key is it correct to call one of its attributes??
        if (oldUserRole != null) {
            userRolesDB.remove(oldUserRole);
            userRolesDB.add(userRole);
            return userRole;
        }
        return userRole;

    }

    @Override
    public void delete(String userRoleId) {
        UserRole userRole = read(userRoleId);
        if (userRole != null) {
            userRolesDB.remove(userRole);
        }
    }

    @Override
    public Set<UserRole> getUserRoles() {
        return userRolesDB;
    }
}
