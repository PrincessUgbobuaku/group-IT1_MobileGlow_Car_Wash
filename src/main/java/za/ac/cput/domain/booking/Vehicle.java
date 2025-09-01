//Thaakirah Watson, 230037550
package za.ac.cput.domain.booking;

import jakarta.persistence.*;
import za.ac.cput.domain.user.Customer;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleID;

    @Column(name = "car_plate_number", unique = true)
    private String carPlateNumber;

    private String carMake;   // ✅ corrected from carMaker

    private String carColour;

    private String carModel;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference("customer-vehicles")
    private Customer customer;

    protected Vehicle() {}

    private Vehicle(Builder builder) {
        this.vehicleID = builder.vehicleID;
        this.carPlateNumber = builder.carPlateNumber;
        this.carMake = builder.carMake;
        this.carColour = builder.carColour;
        this.carModel = builder.carModel;
        this.customer = builder.customer;
    }

    public Long getVehicleID() {
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

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID=" + vehicleID +
                ", carPlateNumber='" + carPlateNumber + '\'' +
                ", carMake='" + carMake + '\'' +
                ", carColour='" + carColour + '\'' +
                ", carModel='" + carModel + '\'' +
                ", customer=" + (customer != null ? customer.getUserId() : null) +
                '}';
    }

    public static class Builder {
        private Long vehicleID;
        private String carPlateNumber;
        private String carMake;
        private String carColour;
        private String carModel;
        private Customer customer;

        public Builder setVehicleID(Long vehicleID) {
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
        public Builder setCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder copy(Vehicle vehicle) {
            this.vehicleID = vehicle.vehicleID;
            this.carPlateNumber = vehicle.carPlateNumber;
            this.carMake = vehicle.carMake;
            this.carColour = vehicle.carColour;
            this.carModel = vehicle.carModel;
            this.customer = vehicle.customer;
            return this;
        }

        public Vehicle build() {
            return new Vehicle(this);
        }
    }
}
