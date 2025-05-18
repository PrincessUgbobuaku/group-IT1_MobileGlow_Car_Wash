package za.ac.cput.domain.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

public class Role {
    private String roleID;
    private RoleDescription roleDescription;

    public enum RoleDescription {
        CLIENT,
        EMPLOYEE;
    }

    //Default constructor
    public Role(){}

    private Role(Builder builder) {
        this.roleID = builder.roleID;
        this.roleDescription = builder.roleDescription;
    }

    //Getters
    public String getRoleID() {
        return roleID;
    }
    public RoleDescription getRoleDescription() {return roleDescription;}

    @Override
    public String toString() {
        return "Role{" +
                "roleID='" + roleID + '\'' +
                ", roleDescription=" + roleDescription +
                '}';
    }

    public static class Builder {
        private String roleID;
        private RoleDescription roleDescription;

        public Builder setRoleID(String roleID) {
            this.roleID = roleID;
            return this;
        }

        public Builder setRoleDescription(RoleDescription roleDescription) {
            this.roleDescription = roleDescription;
            return this;
        }

        public Builder copy(Role role) {
            this.roleID = role.getRoleID();
            this.roleDescription = role.getRoleDescription();
            return this;
        }

        public Role build() {return new Role(this);}
    }



}//End of File.
