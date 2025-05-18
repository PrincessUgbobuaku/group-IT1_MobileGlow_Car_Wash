package za.ac.cput.util;

import za.ac.cput.domain.booking.CleaningService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
