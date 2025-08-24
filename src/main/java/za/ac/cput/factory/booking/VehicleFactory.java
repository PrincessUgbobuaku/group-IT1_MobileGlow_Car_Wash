//Thaakirah Watson, 230037550

package za.ac.cput.factory.booking;

import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.booking.Vehicle;

import java.util.UUID;

public final class VehicleFactory {

    private static final VehicleFactory INSTANCE = new VehicleFactory();
    private VehicleFactory() { }
    public static VehicleFactory getInstance() { return INSTANCE; }

    public Vehicle create(
            String vehicleID,
            String carPlateNumber,
            String carMaker,
            String carColour,
            String carModel,
            Customer customer) {

        if (customer == null) throw new IllegalArgumentException("customer is required");
        if (isBlank(carPlateNumber)) throw new IllegalArgumentException("carPlateNumber is required");

        String id = isBlank(vehicleID) ? generateId() : vehicleID.trim();

        return new Vehicle.Builder()
                .setVehicleID(id)
                .setCarPlateNumber(carPlateNumber.trim())
                .setCarMaker(carMaker != null ? carMaker.trim() : null)
                .setCarColour(carColour != null ? carColour.trim() : null)
                .setCarModel(carModel != null ? carModel.trim() : null)
                .setCustomer(customer)
                .build();
    }

    public Vehicle create(
            String carPlateNumber, String carMaker, String carColour, String carModel, Customer customer) {
        return create(null, carPlateNumber, carMaker, carColour, carModel, customer);
    }

    // --- Helpers ---
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private String generateId() { return "VEH-" + UUID.randomUUID(); }
}