package za.ac.cput.factory.user;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.Role;

import static org.junit.jupiter.api.Assertions.*;

public class RoleFactoryTest {

    Role role1 = RoleFactory.createRole(Role.RoleDescription.EMPLOYEE);
    Role role2 = RoleFactory.createRole(Role.RoleDescription.CLIENT);

    @Test
    @Order(1)
    public void testCreateRoleWithValidDescription() {
        //Check that a role object is created successfully with a valid role description.
        assertNotNull(role1);
        assertNotNull(role1.getRoleID(), "roleId should not be null");
        assertEquals(Role.RoleDescription.EMPLOYEE, role1.getRoleDescription(),
                "roleDescription should be EMPLOYEE");
        System.out.println("Role object created " + role1.toString());
        System.out.println(Role.RoleDescription.EMPLOYEE + " \n " + role1.getRoleDescription());
    }

    @Test
    @Order(2)
    public void testCreateRoleGenerateUniqueID() {
        //Test to ensure two different Role Objects created from fractory generate unique id.
        assertNotEquals(role1.getRoleID(), role2.getRoleID(), "RoleId's should be unique");
        System.out.println("Role Object 1 created: " + role1.getRoleID() +
                "\nRole Object 2 created: " + role2.getRoleID());
    }
}
