//package za.ac.cput.factory.user;
//
////Firstname:        Kwanda
////LastName:         Twalo
////Student Number:   218120192.
//
//import za.ac.cput.domain.user.Login;
//import za.ac.cput.util.Helper;
//
//public class LoginFactory {
//
//    public static Login createLogin(String emailAddress, String password) {
//
//        String loginID = Helper.generateID();
//
//        if (Helper.isValidEmail(emailAddress) ||
//                !Helper.validateStringDetails(password)
//        )
//         {return null;}
//        return new Login.Builder()
//                .setLoginID(loginID)
//                .setEmailAddress(emailAddress)
//                .setPassword(password)
//                .build();
//    }
//
//}
