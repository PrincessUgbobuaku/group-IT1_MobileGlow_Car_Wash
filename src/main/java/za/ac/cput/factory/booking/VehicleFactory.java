//Thaakirah Watson, 230037550
package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.util.Helper;

public class VehicleFactory {

    public static Vehicle createVehicle(
            String carPlateNumber,
            String carMake,
            String carColour,
            String carModel,
            Customer customer
    ) {
        if (!Helper.validateStringDetails(carPlateNumber)) {
            throw new IllegalArgumentException("Vehicle must have a plate number");
        }
        if (!Helper.validateStringDetails(carMake)) {
            throw new IllegalArgumentException("Vehicle must have a make");
        }
        if (!Helper.validateStringDetails(carModel)) {
            throw new IllegalArgumentException("Vehicle must have a model");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Vehicle must belong to a customer");
        }

        //vehicle builder
        return new Vehicle.Builder()
                .setCarPlateNumber(carPlateNumber.toUpperCase()) // optional: normalize
                .setCarMake(carMake)
                .setCarColour(carColour)
                .setCarModel(carModel)
                .setCustomer(customer)
                .build();
    }
}