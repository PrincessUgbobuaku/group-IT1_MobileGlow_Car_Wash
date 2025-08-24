//package za.ac.cput.factory.user;
//
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import za.ac.cput.domain.user.UserRole;
//import za.ac.cput.domain.user.UserRoleID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserRoleFactoryTest {
//
//    UserRole userRole1 = UserRoleFactory.createUserRole(true);
//    UserRole userRole2 = UserRoleFactory.createUserRole(false);
//
//    @Test
//    @Order(1)
//    public void testCreateUserRoleWithActiveStatus() {
//        //Checks if the attribute isActive is set correctly to true.
//
//        assertNotNull(userRole1, "UserRole should not be null");
//        assertTrue(userRole1.getIsActive(), "UserRole should be active");
//        System.out.println(userRole1.toString());
//    }
//
//    @Test
//    @Order(2)
//    public void testCreateUserRoleWithInactiveStatus() {
//        //Checks if the attribute isActive is set correctly to false.
//
//        assertNotNull(userRole2, "UserRole should not be null");
//        assertFalse(userRole2.getIsActive(), "UserRole should be inactive");
//        System.out.println(userRole2.toString());
//    }
//
//
//    @Test
//    @Order(3)
//    void testUserRoleIDIsNotNull() {
//        //Checks if a valid embadded key is generated.
//        UserRoleID userRoleID = userRole1.getUserRoleID();
//
//        assertNotNull(userRoleID, "UserRoleID should not be null");
//        assertNotNull(userRoleID.getUserId(), "UserId inside UserRoleID should not be null");
//        assertNotNull(userRoleID.getRoleId(), "RoleId inside UserRoleID should not be null");
//        System.out.println(userRoleID.toString());
//    }
//
//    @Test
//    @Order(4)
//    void testGeneratedIDsAreUnique() {
//        //Checks if the id's of composite key are unique.
//
//        assertNotEquals(userRole1.getUserRoleID().getUserId(), userRole2.getUserRoleID().getUserId(),
//                "User IDs should be unique");
//
//        assertNotEquals(userRole1.getUserRoleID().getRoleId(), userRole2.getUserRoleID().getRoleId(),
//                "Role IDs should be unique");
//        System.out.println(userRole1.toString() + "\n" + userRole2.toString());
//    }
//
//
//    @Test
//    @Order(5)
//    void testUserRoleIDEqualityAndHashCode() {
//        //Ensures methods (equals() and hashCode() work as expected for composite keys.
//        String userId = "user001";
//        String roleId = "role001";
//
//        UserRoleID id1 = new UserRoleID(userId, roleId);
//        UserRoleID id2 = new UserRoleID(userId, roleId);
//
//        assertEquals(id1, id2, "UserRoleIDs with same userId and roleId should be equal");
//        assertEquals(id1.hashCode(), id2.hashCode(), "HashCodes for equal UserRoleIDs should match");
//        System.out.println(id1.toString() + "\n" + id2.toString());
//    }
//
//
//}
