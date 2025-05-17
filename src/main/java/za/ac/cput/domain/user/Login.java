package za.ac.cput.domain.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

public class Login {
    private String loginID;
    private String emailAddress;
    private String password;

    private Login() {}

    private Login(Builder builder) {
        this.loginID = builder.loginID;
        this.emailAddress = builder.emailAddress;
        this.password = builder.password;
    }

    public String getLoginID() {
        return loginID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String loginID;
        private String emailAddress;
        private String password;

        public Builder setLoginID(String loginID) {
            this.loginID = loginID;
            return this;
        }
        public Builder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder copy(Login login) {
            this.loginID = login.getLoginID();
            this.emailAddress = login.getEmailAddress();
            this.password = login.getPassword();
            return this;
        }

        public Login build() {return new Login(this);}
    }
}//End Of File.
