package za.ac.cput.domain.user;

import java.util.Objects;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

public class UserRole {
    private String userId;
    private String roleId;
    private boolean isActive;

    //Default Constructor
    public UserRole() {}

    //Dynamic Constructor.
    public UserRole(String userId, String roleId, boolean is_Active) {
        this.userId = userId;
        this.roleId = roleId;
        this.isActive = is_Active;
    }

    // Constructor for composite key
    public UserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }
    public String getRoleId() {
        return roleId;
    }
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserRole userRole = (UserRole) object;
        return userId.equals(userRole.userId) &&
                roleId.equals(userRole.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}



