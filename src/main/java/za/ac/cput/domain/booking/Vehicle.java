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

    // Default constructor
    public Vehicle() {
    }

    // Builder-based constructor
    private Vehicle(Builder builder) {
        this.vehicleID = builder.vehicleID;
        this.carPlateNumber = builder.carPlateNumber;
        this.carMake = builder.carMake;
        this.carColour = builder.carColour;
        this.carModel = builder.carModel;
        this.customerID = builder.customerID;
    }

    // Getters and Setters
    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    // Builder inner class
    public static class Builder {
        private String vehicleID;
        private String carPlateNumber;
        private String carMake;
        private String carColour;
        private String carModel;
        private String customerID;

        public Builder vehicleID(String vehicleID) {
            this.vehicleID = vehicleID;
            return this;
        }

        public Builder carPlateNumber(String carPlateNumber) {
            this.carPlateNumber = carPlateNumber;
            return this;
        }

        public Builder carMake(String carMake) {
            this.carMake = carMake;
            return this;
        }

        public Builder carColour(String carColour) {
            this.carColour = carColour;
            return this;
        }

        public Builder carModel(String carModel) {
            this.carModel = carModel;
            return this;
        }

        public Builder customerID(String customerID) {
            this.customerID = customerID;
            return this;
        }

        public Vehicle build() {
            //        if (vehicleID == null || carPlateNumber == null || carMake == null || carModel == null || customer == null) {
            //            throw new IllegalArgumentException("Vehicle fields must not be null");
            //        } will be used in helper class
            return new Vehicle(this);
        }
    }
}
