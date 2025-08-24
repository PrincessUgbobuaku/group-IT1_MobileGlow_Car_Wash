//package za.ac.cput.factory.user;
//
//import za.ac.cput.domain.user.UserRole;
//import za.ac.cput.domain.user.UserRoleID;
//import za.ac.cput.util.Helper;
//
//public class UserRoleFactory {
//    public static UserRole createUserRole(boolean isActive) {
//        //Generate Composite Key values
//        String userId = Helper.generateID();
//        String roleId = Helper.generateID();
//
//        //Creating a composite key by combining the above attributes.
//        UserRoleID userRoleID = new UserRoleID(userId, roleId);
//
//        return new UserRole.Builder()
//                .setUserRoleID(userRoleID)
//                .setActive(isActive)
//                .build();
//    }
//}
