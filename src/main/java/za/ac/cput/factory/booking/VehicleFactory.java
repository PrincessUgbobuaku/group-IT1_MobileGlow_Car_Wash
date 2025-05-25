/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Factory class for Vehicle
 */

package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.util.Helper;

public class VehicleFactory {

    public static Vehicle build1(String carPlateNumber,
                                 String carMake,
                                 String carColour,
                                 String carModel,
                                 String customerID) {

        if (!Helper.validateStringDetails(carPlateNumber) ||
                !Helper.validateStringDetails(carMake) ||
                !Helper.validateStringDetails(carModel) ||
                !Helper.validateStringDetails(customerID)) {
            throw new IllegalArgumentException("Vehicle fields must not be null or empty");
        }

        String generatedID = Helper.generateID();

        return new Vehicle.Builder()
                .setVehicleID(generatedID)
                .setCarPlateNumber(carPlateNumber)
                .setCarMake(carMake)
                .setCarColour(carColour)
                .setCarModel(carModel)
                .setCustomerID(customerID)
                .build();
    }
}
