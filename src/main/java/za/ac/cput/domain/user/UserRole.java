//package za.ac.cput.domain.user;
//
//import jakarta.persistence.EmbeddedId;
//
//import java.util.Objects;
//
////Firstname:        Kwanda
////LastName:         Twalo
////Student Number:   218120192.
//
//public class UserRole {
//    @EmbeddedId
//    private UserRoleID userRoleID;
//    private boolean isActive;
//
//    //Default Constructor
//    public UserRole() {}
//
//
//    public UserRole(Builder builder) {
//        this.userRoleID = builder.userRoleID;
//        this.isActive = builder.isActive;
//    }
//
//
//    //Getters
//    public UserRoleID getUserRoleID() {
//        return userRoleID;
//    }
//    public boolean getIsActive() {
//        return isActive;
//    }
//
//    @Override
//    public String toString() {
//        return "UserRole{" +
//                "userRoleID= " + userRoleID +
//                ", isActive= " + isActive +
//                '}';
//    }
//
//    public static class Builder {
//        private UserRoleID userRoleID;
//        private boolean isActive;
//
//        public Builder setUserRoleID(UserRoleID userRoleID) {
//            this.userRoleID = userRoleID;
//            return this;
//        }
//
//        public Builder setActive(boolean isActive) {
//            this.isActive = isActive;
//            return this;
//        }
//
//        public Builder copy(UserRole userRole) {
//            this.userRoleID = userRole.getUserRoleID();
//            this.isActive = userRole.getIsActive();
//            return this;
//        }
//
//        public UserRole build() {
//            return new UserRole(this);
//        }
//    }
//
//
//}
//
//
//
