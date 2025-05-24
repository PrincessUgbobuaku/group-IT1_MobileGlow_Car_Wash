package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.List;


public class BookingFactory {

    public static Booking createBookingFactory1 (List<CleaningService> cleaningServices, String washAttendantID, LocalDateTime bookingDateTime, String vehicleID, boolean tipAdd, double bookingCost) {

        String bookingID = Helper.generateID();
        //String washAttendantID = washAttendant.getWashAttendantID();


        if(!Helper.isListOfCorrectType(cleaningServices, CleaningService.class)
        || !Helper.validateStringDetails(washAttendantID)
        || !Helper.isFutureDate(bookingDateTime)
        || !Helper.validateStringDetails(vehicleID)
        || !Helper.validatePrice(bookingCost)){

            return null;

        } else {

            return new Booking.Builder()
                    .setBookingID(bookingID)
                    .setCleaningServices(cleaningServices)
                    .setWashAttendantID(washAttendantID)
                    .setBookingDateTime(bookingDateTime)
                    .setVehicleID(vehicleID)
                    .setTipAdd(tipAdd)
                    .setBookingCost(bookingCost)
                    .build();


        }


    }



}


