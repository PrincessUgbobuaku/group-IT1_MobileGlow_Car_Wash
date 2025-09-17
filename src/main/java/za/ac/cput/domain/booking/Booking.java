
package za.ac.cput.domain.booking;

import jakarta.persistence.*;
import za.ac.cput.domain.user.employee.WashAttendant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToMany
    @JoinTable(
            name = "booking_cleaning_service",
            joinColumns = @JoinColumn(name = "booking_Id"),
            inverseJoinColumns = @JoinColumn(name = "cleaning_service_id")
    )
    private List<CleaningService> cleaningServices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "wash_attendant_id")
    private WashAttendant washAttendant;

    private LocalDateTime bookingDateTime;
    private boolean tipAdd;
    private double bookingCost;

    // Constructors
    public Booking() {}

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.cleaningServices = builder.cleaningServices;
        this.washAttendant = builder.washAttendant;
        this.vehicle = builder.vehicle;
        this.bookingDateTime = builder.bookingDateTime;
        this.tipAdd = builder.tipAdd;
        this.bookingCost = builder.bookingCost;
    }

    // Getters

    public Long getBookingId() {
        return bookingId;
    }

    public List<CleaningService> getCleaningServices() {
        return cleaningServices;
    }

    public WashAttendant getWashAttendant() {
        return washAttendant;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public boolean isTipAdd() {
        return tipAdd;
    }

    public double getBookingCost() {
        return bookingCost;
    }

    @Override
    public String toString() {
        return "Booking {\n" +
                "  bookingId = " + bookingId + ",\n" +
                "  cleaningServices = " + (cleaningServices != null ? cleaningServices : "[]") + ",\n" +
                "  washAttendant = " + (washAttendant != null ? washAttendant.getUserName() + " " + washAttendant.getUserSurname() : "null") + ",\n" +
                "  vehicle = " + (vehicle != null ? vehicle.getCarPlateNumber() + " - " + vehicle.getCarMake() : "null") + ",\n" +
                "  bookingDateTime = " + bookingDateTime + ",\n" +
                "  tipAdd = " + tipAdd + ",\n" +
                "  bookingCost = " + bookingCost + "\n" +
                '}';
    }

    // Builder
    public static class Builder {
        private Long bookingId;
        private List<CleaningService> cleaningServices = new ArrayList<>();
        private WashAttendant washAttendant;
        private Vehicle vehicle;
        private LocalDateTime bookingDateTime;
        private boolean tipAdd;
        private double bookingCost;

        public Builder setBookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder setCleaningServices(List<CleaningService> cleaningServices) {
            this.cleaningServices = cleaningServices;
            return this;
        }

        public Builder setWashAttendant(WashAttendant washAttendant) {
            this.washAttendant = washAttendant;
            return this;
        }

        public Builder setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public Builder setBookingDateTime(LocalDateTime bookingDateTime) {
            this.bookingDateTime = bookingDateTime;
            return this;
        }

        public Builder setTipAdd(boolean tipAdd) {
            this.tipAdd = tipAdd;
            return this;
        }

        public Builder setBookingCost(double bookingCost) {
            this.bookingCost = bookingCost;
            return this;
        }

        public Builder copy(Booking booking) {
            this.bookingId = booking.getBookingId();
            this.cleaningServices = new ArrayList<>(booking.getCleaningServices());
            this.washAttendant = booking.getWashAttendant();
            this.vehicle = booking.getVehicle();
            this.bookingDateTime = booking.getBookingDateTime();
            this.tipAdd = booking.isTipAdd();
            this.bookingCost = booking.getBookingCost();
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}





//package za.ac.cput.domain.booking;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import za.ac.cput.domain.payment.Payment;

//import za.ac.cput.domain.user.employee.WashAttendant;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Booking {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long bookingID;
//
//    @ManyToMany
//    @JoinTable(
//            name = "booking_cleaning_service",
//            joinColumns = @JoinColumn(name = "booking_id"),
//            inverseJoinColumns = @JoinColumn(name = "cleaning_service_id")
//    )
//    private List<CleaningService> cleaningServices = new ArrayList<>();
//
//    @OneToMany(mappedBy = "booking", orphanRemoval = true, cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<Payment> payments = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "wash_attendant_id")
//    private WashAttendant washAttendant;
//
//    private LocalDateTime bookingDateTime;
//
//    @ManyToOne
//    @JoinColumn(name = "vehicle_id")
//    private Vehicle vehicle;
//
//    private boolean tipAdd;
//    private double bookingCost;
//
//    @JsonCreator
//    protected Booking() {}
//
//    private Booking(Builder builder) {
//        this.bookingID = builder.bookingID;
//        this.cleaningServices = builder.cleaningServices;
//        this.payments = builder.payments;
//        this.washAttendant = builder.washAttendant;
//        this.bookingDateTime = builder.bookingDateTime;
//        this.vehicle = builder.vehicle;
//        this.tipAdd = builder.tipAdd;
//        this.bookingCost = builder.bookingCost;
//    }
//
//    // --- Getters ---
//
//    public Long getBookingID() {
//        return bookingID;
//    }
//
//    public List<CleaningService> getCleaningServices() {
//        return cleaningServices;
//    }
//
//    public List<Payment> getPayments() {
//        return payments;
//    }
//
//    public WashAttendant getWashAttendant() {
//        return washAttendant;
//    }
//
//    public LocalDateTime getBookingDateTime() {
//        return bookingDateTime;
//    }
//
//    public Vehicle getVehicle() {
//        return vehicle;
//    }
//
//    public boolean isTipAdd() {
//        return tipAdd;
//    }
//
//    public double getBookingCost() {
//        return bookingCost;
//    }
//
//    @Override
//    public String toString() {
//        return "Booking{" +
//                "bookingID=" + bookingID +
//                ", cleaningServices=" + cleaningServices +
//                ", payments=" + (payments != null ? payments.size() + " payment(s)" : "null") +  // 👈 Changed
//                ", washAttendant=" + washAttendant  +
//                ", bookingDateTime=" + bookingDateTime +
//                ", vehicleID=" + (vehicle != null ? vehicle.getVehicleID() : "null") +
//                ", tipAdd=" + tipAdd +
//                ", bookingCost=" + bookingCost +
//                '}';
//    }
//
//    public static class Builder {
//        private Long bookingID;
//        private List<CleaningService> cleaningServices = new ArrayList<>();
//        private List<Payment> payments = new ArrayList<>();
//        private WashAttendant washAttendant;
//        private LocalDateTime bookingDateTime;
//        private Vehicle vehicle;
//        private boolean tipAdd;
//        private double bookingCost;
//
//        public Builder setBookingID(Long bookingID) {
//            this.bookingID = bookingID;
//            return this;
//        }
//
//        public Builder setCleaningServices(List<CleaningService> cleaningServices) {
//            this.cleaningServices = cleaningServices;
//            return this;
//        }
//
//        public Builder setPayments(List<Payment> payments) {
//            this.payments = payments;
//            return this;
//        }
//
//        public Builder setWashAttendant(WashAttendant washAttendant) {
//            this.washAttendant = washAttendant;
//            return this;
//        }
//
//        public Builder setBookingDateTime(LocalDateTime bookingDateTime) {
//            this.bookingDateTime = bookingDateTime;
//            return this;
//        }
//
//        public Builder setVehicle(Vehicle vehicle) {
//            this.vehicle = vehicle;
//            return this;
//        }
//
//        public Builder setTipAdd(boolean tipAdd) {
//            this.tipAdd = tipAdd;
//            return this;
//        }
//
//        public Builder setBookingCost(double bookingCost) {
//            this.bookingCost = bookingCost;
//            return this;
//        }
//
//        public Builder copy(Booking booking) {
//            this.bookingID = booking.bookingID;
//            this.cleaningServices = booking.cleaningServices;
//            this.payments = booking.payments;
//            this.washAttendant = booking.washAttendant;
//            this.bookingDateTime = booking.bookingDateTime;
//            this.vehicle = booking.vehicle;
//            this.tipAdd = booking.tipAdd;
//            this.bookingCost = booking.bookingCost;
//            return this;
//        }
//
//        public Booking build() {
//            return new Booking(this);
//        }
//    }
//}