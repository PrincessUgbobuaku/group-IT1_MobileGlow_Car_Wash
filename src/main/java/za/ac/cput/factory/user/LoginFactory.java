package za.ac.cput.factory.user;

//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import za.ac.cput.domain.user.Login;
import za.ac.cput.util.Helper;

public class LoginFactory {

    public static Login createLogin(String emailAddress, String password) {

        if (!Helper.isValidEmail(emailAddress) ||
                !Helper.isValidPassword(password)
        )
         {return null;}
        return new Login.Builder()
                .setEmailAddress(emailAddress)
                .setPassword(password)
                .build();
    }

}
