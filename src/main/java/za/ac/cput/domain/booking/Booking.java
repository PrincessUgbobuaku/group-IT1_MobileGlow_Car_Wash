/*Booking.java
 * Booking model class
 * Author: Adaeze Princess Ugbobuaku
 * Date: 11 May 2025*/

package za.ac.cput.domain.booking;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.booking.Vehicle;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {

    @Id
    private String bookingID;

    @ManyToMany
    @JoinTable(
            name = "booking_cleaning_service", // 👈 join table name
            joinColumns = @JoinColumn(name = "booking_id"), // 👈 foreign key to Booking
            inverseJoinColumns = @JoinColumn(name = "cleaning_service_id") // 👈 foreign key to CleaningService
    )
    private List<CleaningService> cleaningServices = new ArrayList<>();

    @OneToMany(mappedBy = "booking", orphanRemoval = true)
    @JsonManagedReference
    private List<Payment> payments = new ArrayList<>();
    //One booking can have many payments.
    //mappedBy = "booking" means the booking field in Payment.java is the owning side.

    private String washAttendantID;
    private LocalDateTime bookingDateTime;

    @ManyToOne
    @JoinColumn(name = "vehicleID")
    private Vehicle vehicle;

    private boolean tipAdd;
    private double bookingCost;

    // Required by JPA
    protected Booking() {}


    private Booking (Builder builder) {
        this.bookingID = builder.bookingID;
        this.cleaningServices = builder.cleaningServices;
        this.payments = builder.payments;
        this.washAttendantID = builder.washAttendantID;
        this.bookingDateTime = builder.bookingDateTime;
        this.vehicle = builder.vehicle;
        this.tipAdd = builder.tipAdd;
        this.bookingCost = builder.bookingCost;
    }

    public String getBookingID() {
        return bookingID;
    }

    public List<CleaningService> getCleaningServices() {
        return cleaningServices;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public String getWashAttendantID() {
        return washAttendantID;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isTipAdd() {
        return tipAdd;
    }

    public double getBookingCost() {
        return bookingCost;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID='" + bookingID + '\'' +
                ", cleaningServices=" + cleaningServices +
                ", payments=" + payments +
                ", washAttendantID='" + washAttendantID + '\'' +
                ", bookingDateTime=" + bookingDateTime +
                ", vehicleID='" + vehicle.getVehicleID() + '\'' +
                ", vehicleModel='" + vehicle.getCarModel() + '\'' +
                ", tipAdd=" + tipAdd +
                ", bookingCost=" + bookingCost +
                '}';
    }

    public static class Builder {
        private String bookingID;
        private List<CleaningService> cleaningServices = new ArrayList<>();
        private String washAttendantID;
        private List<Payment> payments = new ArrayList<>();
        private LocalDateTime bookingDateTime;
        private Vehicle vehicle;
        private boolean tipAdd;
        private double bookingCost;

        public Builder setBookingID(String bookingID) {
            this.bookingID = bookingID;
            return this;
        }

        public Builder setCleaningServices(List<CleaningService> cleaningServices) {
            this.cleaningServices = cleaningServices;
            return this;
        }

        //allows us to automatically add services to the list

//        public Builder addCleaningService(List<CleaningService> list, CleaningService service) {
//            this.cleaningServices.add(service);
//            return this;
//        }

        public Builder setPayments(List<Payment> payments) {
            this.payments = payments;
            return this;
        }

        public Builder setWashAttendantID(String washAttendantID) {
            this.washAttendantID = washAttendantID;
            return this;
        }

        public Builder setBookingDateTime(LocalDateTime bookingDateTime) {
            this.bookingDateTime = bookingDateTime;
            return this;
        }

        public Builder setVehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
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
            this.bookingID = booking.bookingID;
            this.cleaningServices = booking.cleaningServices;
            this.payments = booking.payments;
            this.washAttendantID = booking.washAttendantID;
            this.bookingDateTime = booking.bookingDateTime;
            this.vehicle = booking.vehicle;
            this.tipAdd = booking.tipAdd;
            this.bookingCost = booking.bookingCost;
            return this;
        }

        //copy method can be used when injecting payments after booking has been created.

        /*ADD TO SERVICE*/
//        public double calculateBookingCost(List<CleaningService> booking_list) { //loops through booking_list and calculates booking cost
//
//            double totalCost = 0;
//            int index = 0;
//
//            while (index < booking_list.size()) {
//                totalCost += booking_list.get(index).getPriceOfService();
//                index++;
//            }
//
//            return totalCost;
//        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
