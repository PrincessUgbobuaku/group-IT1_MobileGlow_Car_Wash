package za.ac.cput.repository.user.impl;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.UserRole;
import za.ac.cput.factory.user.UserRoleFactory;
import za.ac.cput.repository.user.UserRoleRepository;

import static org.junit.jupiter.api.Assertions.*;
class UserRoleRepositoryImplTest {

    private static UserRoleRepository repository = new UserRoleRepositoryImpl();
    private static UserRole userRole1 = new UserRole.Builder().setActive(true).build();

    @Test
    @Order(1)
    void create() {
        UserRole created = repository.create(userRole1);
        assertNotNull(created);
        System.out.println("userRole = " + created);
    }

    @Test
    @Order(2)
    void read() {
        UserRole readUserRole = repository.read(userRole1.getUserRoleID().getRoleId());
        assertNotNull(readUserRole);
        assertEquals(userRole1.getUserRoleID(), readUserRole.getUserRoleID());
        System.out.println("userRole = " + readUserRole);
    }

    @Test
    @Order(3)
    void update() {
        UserRole updatedUserRole = new UserRole.Builder().copy(userRole1).setActive(false).build();
        updatedUserRole = repository.update(updatedUserRole);
        assertNotNull(updatedUserRole);
        assertNotEquals(updatedUserRole.getIsActive(), userRole1.getIsActive());
        System.out.println("updatedUserRole = " + updatedUserRole);
    }

    @Test
    @Order(5)
    void delete() {
        repository.delete(userRole1.getUserRoleID().getRoleId());
    }

    @Test
    @Order(4)
    void getUserRoles() {
        System.out.println("userRole = " + repository.getUserRoles());
    }
}