
//Thaakirah Watson, 230037550

package za.ac.cput.domain.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import za.ac.cput.domain.user.Customer;

@Entity
public class Vehicle {

    @Id
    private String vehicleID;
    private String carPlateNumber;
    private String carMaker;
    private String carColour;
    private String carModel;

    @ManyToOne
    private Customer customer;

    private Vehicle(Builder builder) {
        this.vehicleID = builder.vehicleID;
        this.carPlateNumber = builder.carPlateNumber;
        this.carMaker = builder.carMaker;
        this.carColour = builder.carColour;
        this.carModel = builder.carModel;
        this.customer = builder.customer;
    }

    public Vehicle() {

    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public String getCarMaker() {
        return carMaker;
    }

    public String getCarColour() {
        return carColour;
    }

    public String getCarModel() {
        return carModel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static class Builder {
        private String vehicleID;
        private String carPlateNumber;
        private String carMaker;
        private String carColour;
        private String carModel;
        private Customer customer;

        public Builder setVehicleID(String vehicleID) {
            this.vehicleID = vehicleID;
            return this;
        }

        public Builder setCarPlateNumber(String carPlateNumber) {
            this.carPlateNumber = carPlateNumber;
            return this;
        }

        public Builder setCarMaker(String carMaker) {
            this.carMaker = carMaker;
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

        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Vehicle copy() {
            return new Builder()
                    .setVehicleID(this.vehicleID)
                    .setCarPlateNumber(this.carPlateNumber)
                    .setCarMaker(this.carMaker)
                    .setCarColour(this.carColour)
                    .setCarModel(this.carModel)
                    .setCustomer(this.customer)
                    .build();
        }

        public Vehicle build() {
            return new Vehicle(this);
        }
    }
}
