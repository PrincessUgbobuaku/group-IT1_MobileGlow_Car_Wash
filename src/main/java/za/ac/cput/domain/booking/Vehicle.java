/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Domain class for Vehicle
 */

package za.ac.cput.domain.booking;

public class Vehicle {
    private String vehicleID;
    private String carPlateNumber;
    private String carMake;
    private String carColour;
    private String carModel;
    private String customerID; // Foreign key reference to Customer

    // Private constructor for Builder
    private Vehicle(Builder builder) {
        this.vehicleID = builder.vehicleID;
        this.carPlateNumber = builder.carPlateNumber;
        this.carMake = builder.carMake;
        this.carColour = builder.carColour;
        this.carModel = builder.carModel;
        this.customerID = builder.customerID;
    }

    // Getters only
    public String getVehicleID() {
        return vehicleID;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarColour() {
        return carColour;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCustomerID() {
        return customerID;
    }

    // Builder inner class
    public static class Builder {
        private String vehicleID;
        private String carPlateNumber;
        private String carMake;
        private String carColour;
        private String carModel;
        private String customerID;

        public Builder setVehicleID(String vehicleID) {
            this.vehicleID = vehicleID;
            return this;
        }

        public Builder setCarPlateNumber(String carPlateNumber) {
            this.carPlateNumber = carPlateNumber;
            return this;
        }

        public Builder setCarMake(String carMake) {
            this.carMake = carMake;
            return this;
        }

        public Builder setCarColour(String carColour) {
            this.carColour = carColour;
            return this;
        }

        public Builder setCarModel(String carModel) {
            this.carModel = carModel;
            return this;
        }

        public Builder setCustomerID(String customerID) {
            this.customerID = customerID;
            return this;
        }

        public Vehicle build() {
            return new Vehicle(this);
        }
    }
}
