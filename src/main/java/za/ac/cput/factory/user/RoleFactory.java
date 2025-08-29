//package za.ac.cput.factory.user;
//
//import za.ac.cput.domain.user.Role;
//import za.ac.cput.util.Helper;
//
//public class RoleFactory {
//
//    public static Role createRole(Role.RoleDescription roleDescription) {
//        String roleId = Helper.generateID();
//
//        return new Role.Builder()
//                .setRoleID(roleId)
//                .setRoleDescription(roleDescription)
//                .build();
//    }
//
//}
