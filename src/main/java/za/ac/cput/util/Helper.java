package za.ac.cput.util;


import org.apache.commons.validator.routines.EmailValidator;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;


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

    public static boolean validateDuration(int durationMinutes) {
        return durationMinutes > 0;
    }

    public static boolean isValidEmail(String email){
        EmailValidator validator = EmailValidator.getInstance();
        if(validator.isValid(email))
            return false;
        return true;
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

}

