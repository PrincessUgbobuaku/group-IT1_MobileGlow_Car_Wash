package za.ac.cput.repository.user.impl;

import za.ac.cput.domain.user.Role;
import za.ac.cput.repository.user.RoleRepository;

import java.util.HashSet;
import java.util.Set;

public class RoleRepositoryImpl implements RoleRepository {

    private Set<Role> rolesDB;
    private static RoleRepository RoleRepository;

    RoleRepositoryImpl() {rolesDB = new HashSet<Role>();}

    public static RoleRepository getRoleRepository(){
        if(RoleRepository==null){
            RoleRepository = new RoleRepositoryImpl();
        }
        return RoleRepository;
    }


    @Override
    public Role create(Role role) {
        this.rolesDB.add(role);
        return role;
    }

    @Override
    public Role read(String roleId) {
        for (Role role : rolesDB) {
            role.getRoleID().equals(roleId);
            return role;
        }
        return null;
    }

    @Override
    public Role update(Role role) {
        Role oldRole = this.read(role.getRoleID());
        if (oldRole != null) {
            this.rolesDB.remove(oldRole);
            this.rolesDB.add(role);
        }
        return role;
    }

    @Override
    public void delete(String roleId) {
        Role role = this.read(roleId);
        if (role != null) {
            this.rolesDB.remove(role);
        }

    }

    @Override
    public Set<Role> getRoles() {
        return rolesDB;
    }
}
