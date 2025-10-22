
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

    private boolean cancelled = false;

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
        this.cancelled = builder.cancelled;
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

    public boolean isCancelled() {
        return cancelled;
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
                "  isCancelled = " + cancelled + "\n" +
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
        private boolean cancelled;

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

        public Builder setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
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
            this.cancelled = booking.isCancelled();
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}



