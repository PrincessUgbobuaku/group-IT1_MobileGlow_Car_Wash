package za.ac.cput.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

@Entity
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginID;
    private String emailAddress;
    private String password;

    protected Login() {}

    private Login(Builder builder) {
        this.loginID = builder.loginID;
        this.emailAddress = builder.emailAddress;
        this.password = builder.password;
    }

    public Long getLoginID() {
        return loginID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "\nLogin{" +
                "loginID='" + loginID + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class Builder {
        private Long loginID;
        private String emailAddress;
        private String password;

        public Builder setLoginID(Long loginID) {
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
