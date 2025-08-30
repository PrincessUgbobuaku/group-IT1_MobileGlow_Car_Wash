package za.ac.cput.util;


import org.apache.commons.validator.routines.EmailValidator;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Helper {

    public static String generateID(){
        return UUID.randomUUID().toString();
    }

    public static boolean validateStringDetails(String str) {
        // Ensure that none of the fields are empty or null

        if (str != null && !str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    //validated the employee hire date.
    public static boolean isValidHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        // Hire date should not be in the future
        if (hireDate.isAfter(today)) {
            return false;
        }

        // Optional: Restrict hire date to not be older than, say, 50 years
        LocalDate earliestAllowed = today.minusYears(50);

        return !hireDate.isBefore(earliestAllowed);
    }

    public static boolean isListOfCorrectType(List<?> list, Class<?> expectedType) {
        for (Object obj : list) {
            if (!expectedType.isInstance(obj)) {
                return false;
            }
        }
        return true;
    }

    public static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String value) {
        if (value == null || enumClass == null)
            return false;

        try {
            Enum.valueOf(enumClass, value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidEnumValue(Object obj, Class<? extends Enum<?>> enumType) {
        return obj != null && enumType.isInstance(obj);
    }

    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean validatePrice(double price) {
        if (price > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*public static boolean validateDuration(int durationMinutes) {
        return durationMinutes > 0;
    }*/

   /* public static boolean isValidEmail(String email){
        EmailValidator validator = EmailValidator.getInstance();
        if(validator.isValid(email))
            return false;

        return true;
    }*/

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        //Checks if Password has at least one digit, one letter and is more than 8 characters long and No Special Characters.
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidObject(Object obj) {
        return obj != null;
    }

    public static boolean isValidInt(int number) {
        return number > 0;
    }

    public static boolean isValidDouble(double number) {
        return number > 0;
    }
    public static boolean validateDuration(int durationMinutes) {
        return durationMinutes > 0;
    }
  
    //the following is related to the customer and vehicle factory classes
    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static void validateCustomerFields(String customerID, LocalDate dob) {
        if (!validateStringDetails(customerID)) {
            throw new IllegalArgumentException("CustomerID must not be null or empty");
        }
        if (!isValidDate(dob)) {
            throw new IllegalArgumentException("CustomerDOB must not be null or a future date");
        }
    }


    public static void validateVehicleFields(String vehicleID, String carPlateNumber,
                                             String carMake, String carModel, String customerID) {

        if (!validateStringDetails(vehicleID) ||
                !validateStringDetails(carPlateNumber) ||
                !validateStringDetails(carMake) ||
                !validateStringDetails(carModel) ||
                !validateStringDetails(customerID)) {

            throw new IllegalArgumentException("Vehicle fields must not be null or empty");
        }

    }
  
    //Employee Factory Classes
    public static boolean validateDate(Date hireDate) {
        if (hireDate != null) {
            return true;
        }
        return false;

    }

    public static boolean validateShiftHours(int shiftHours) {
        if(shiftHours <0){
            return false;
        }
        return true;
    }

    public static boolean validateIsFullTime(boolean isFullTime) {
        if(isFullTime == true){
            return true;
        }
        return false;
    }

    public static boolean validateHasTaxFillingAuthority(boolean hasTaxFillingAuthority) {
        if(hasTaxFillingAuthority == true){
            return true;
        }
        return false;

    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isNullorEmpty(phoneNumber)) {
            return false;
        }

        //Checks for e.g 0725637252 or +27 72 563...
        String regex = "^(\\+27|0)[6-8][0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

}

