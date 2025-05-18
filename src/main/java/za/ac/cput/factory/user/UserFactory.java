package za.ac.cput.factory.user;
//Firstname:        Kwanda
//LastName:         Twalo
//Student Number:   218120192.

import za.ac.cput.domain.user.User;
import za.ac.cput.util.Helper;

import java.util.UUID;

public class UserFactory {
    public static User createUser(String userName, String userSurname) {
        String userId = Helper.generateID();

        if (!Helper.validateStringDetails(userName) ||
        !Helper.validateStringDetails(userSurname)) {
            return null;
        }
        return new User.Builder()
                .setUserId(userId)
                .setUserName(userName)
                .setUserSurname(userSurname)
                .build();
    }

}
