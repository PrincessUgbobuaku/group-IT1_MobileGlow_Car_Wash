package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.util.Helper;

public class VehicleFactory {

        public static Vehicle createVehicle(String carPlateNumber, String carMake, String carColour, String carModel, String customerID) {
            String vehicleID = Helper.generateID(); // generateID() will be implemented in the Helper class
            return new Vehicle.Builder()
                    .vehicleID(vehicleID)
                    .carPlateNumber(carPlateNumber)
                    .carMake(carMake)
                    .carColour(carColour)
                    .carModel(carModel)
                    .customerID(customerID)
                    .build();
        }
    }


