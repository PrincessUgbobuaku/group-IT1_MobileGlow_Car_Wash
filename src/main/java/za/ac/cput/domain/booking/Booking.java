package za.ac.cput.domain.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private String bookingID;
    private List<CleaningService> cleaningServices = new ArrayList<>();
    private String washAttendantID;
    private LocalDateTime bookingDateTime;
    private String vehicleID;
    private boolean tipAdd;
    private double bookingCost;

    private Booking (Builder builder) {
        this.bookingID = builder.bookingID;
        this.cleaningServices = builder.cleaningServices;
        this.washAttendantID = builder.washAttendantID;
        this.bookingDateTime = builder.bookingDateTime;
        this.vehicleID = builder.vehicleID;
        this.tipAdd = builder.tipAdd;
        this.bookingCost = builder.bookingCost;
    }

    public String getBookingID() {
        return bookingID;
    }

    public List<CleaningService> getCleaningServices() {
        return cleaningServices;
    }

    public String getWashAttendantID() {
        return washAttendantID;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public String getVehicleID() {
        return vehicleID;
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
                ", washAttendantID='" + washAttendantID + '\'' +
                ", bookingDateTime=" + bookingDateTime +
                ", vehicleID='" + vehicleID + '\'' +
                ", tipAdd=" + tipAdd +
                ", bookingCost=" + bookingCost +
                '}';
    }

    public static class Builder {
        private String bookingID;
        private List<CleaningService> cleaningServices = new ArrayList<>();
        private String washAttendantID;
        private LocalDateTime bookingDateTime;
        private String vehicleID;
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
        public Builder addCleaningService(CleaningService service) {
            this.cleaningServices.add(service);
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

        public Builder setVehicleID(String vehicleID) {
            this.vehicleID = vehicleID;
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

        public double calculateBookingCost(List<CleaningService> booking_list) { //loops through booking_list and calculates booking cost

            double totalCost = 0;
            int index = 0;

            while (index < booking_list.size()) {
                totalCost += booking_list.get(index).getPriceOfService();
                index++;
            }

            return totalCost;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
