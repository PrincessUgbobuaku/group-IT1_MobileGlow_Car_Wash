package za.ac.cput.repository.user.impl;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.Role;
import za.ac.cput.repository.user.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryImplTest {

    private static RoleRepository rolerepository = new RoleRepositoryImpl();
    private static Role role1 = new Role.Builder().setRoleDescription(Role.RoleDescription.EMPLOYEE).build();


    @Test
    @Order(1)
    void create() {
        Role createdRole = rolerepository.create(role1);
        assertNotNull(createdRole);
        System.out.println("Role created: " + createdRole);
    }

    @Test
    @Order(2)
    void read() {
        Role readRole = rolerepository.read(role1.getRoleID());
        assertNotNull(readRole);
        assertEquals(role1.getRoleID(), readRole.getRoleID());
        System.out.println("Role read: " + readRole);
    }

    @Test
    @Order(3)
    void update() {
        Role updatedRole = new Role.Builder().copy(role1).setRoleDescription(Role.RoleDescription.CLIENT).build();
        rolerepository.update(updatedRole);
        assertNotNull(updatedRole);
        assertNotEquals(role1.getRoleDescription(), updatedRole.getRoleDescription());
        System.out.println("Role updated: " + updatedRole);
    }

    @Test
    @Order(5)
    void delete() {
        rolerepository.delete(role1.getRoleID());
    }

    @Test
    @Order(4)
    void getRoles() {
        System.out.println("List of roles: " + rolerepository.getRoles());

    }
}