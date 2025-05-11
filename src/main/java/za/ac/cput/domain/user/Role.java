package za.ac.cput.domain.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

public class Role {
    private String roleID;
    private enum roleDescription {
        CLIENT,
        EMPLOYEE;
    }

    public Role(){}

    public String getRoleID() {
        return roleID;
    }
}//End of File.
