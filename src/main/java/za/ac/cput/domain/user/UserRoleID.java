package za.ac.cput.domain.user;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


//This class is used for setting-up a composite key.
@Embeddable
public class UserRoleID implements Serializable {
    private String userId;
    private String roleId;

    //default constructor
    public UserRoleID() {}

    public UserRoleID(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    //Getters
    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    @Override
    public boolean equals (Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserRoleID userRoleID = (UserRoleID) object;
        return userId.equals(userRoleID.userId) && roleId.equals(userRoleID.roleId);
    }

    @Override
    public int hashCode () {
        return Objects.hash(userId, roleId);
    }
}

