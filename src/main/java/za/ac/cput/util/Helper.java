package za.ac.cput.util;

import java.time.LocalDate;
import java.util.UUID;

public class Helper {
    public static String generateID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isEmptyOrNullString(String str) {
        return str == null || str.trim().isEmpty();
    }

//    public static boolean isNullOrInvalid(String... values) {
//        for (String value : values) {
//            if (isNullOrEmpty(value)) return true;
//        }
//        return false;
//    }

    public static boolean isValidDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    public static void validateVehicleFields(String vehicleID, String carPlateNumber, String carMake,
                                             String carModel, String customerID) {
        if (Helper.isEmptyOrNullString(vehicleID) ||
                Helper.isEmptyOrNullString(carPlateNumber) ||
                Helper.isEmptyOrNullString(carMake) ||
                Helper.isEmptyOrNullString(carModel) ||
                Helper.isEmptyOrNullString(customerID)) {
            throw new IllegalArgumentException("Vehicle fields must not be null or empty");
        }
    }

//    public static void validateVehicleFields(String vehicleID, String carPlateNumber, String carMake,
//                                             String carModel, String customerID) {
//        if (isNullOrInvalid(vehicleID, carPlateNumber, carMake, carModel, customerID)) {
//            throw new IllegalArgumentException("Vehicle fields must not be null or empty");
//        }
//    }

    public static void validateCustomerFields(String customerID, LocalDate dob) {
        if (isNullOrEmpty(customerID)) {
            throw new IllegalArgumentException("CustomerID must not be null or empty");
        }
        if (!isValidDate(dob)) {
            throw new IllegalArgumentException("CustomerDOB must not be null or a future date");
        }
    }
}
