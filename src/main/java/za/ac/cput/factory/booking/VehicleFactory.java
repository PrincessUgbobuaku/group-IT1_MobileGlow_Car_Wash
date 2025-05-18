/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Factory class for Vehicle
 */

package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.util.Helper;

public class VehicleFactory {
    public static Vehicle build(String vehicleID,
                                String carPlateNumber,
                                String carMake,
                                String carColour,
                                String carModel,
                                String customerID) {

        Helper.validateVehicleFields(vehicleID, carPlateNumber, carMake, carModel, customerID);

        return new Vehicle.Builder()
                .setVehicleID(vehicleID)
                .setCarPlateNumber(carPlateNumber)
                .setCarMake(carMake)
                .setCarColour(carColour)
                .setCarModel(carModel)
                .setCustomerID(customerID)
                .build();
    }
}