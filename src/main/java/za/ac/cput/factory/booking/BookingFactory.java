package za.ac.cput.factory.booking;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.util.Helper;

import java.time.LocalDateTime;
import java.util.List;


public class BookingFactory {

    public static Booking createBooking(List<CleaningService> cleaningServices, List<Payment> payments, String washAttendantID, LocalDateTime bookingDateTime, Vehicle vehicle, boolean tipAdd, double bookingCost) {

        String bookingID = Helper.generateID();
        //String washAttendantID = washAttendant.getWashAttendantID();


        if(!Helper.isListOfCorrectType(cleaningServices, CleaningService.class)
//                ||!Helper.isListOfCorrectType(payments, Payment.class)
//                ||(!Helper.areAllPaymentAmountsValid(payments))
                || !Helper.validateStringDetails(washAttendantID)
                || !Helper.isFutureDate(bookingDateTime)
                || !Helper.isInstanceOf(vehicle, Vehicle.class)
                || !Helper.isValidDouble(bookingCost)) {

            return null;

        } else {


            Booking booking = new Booking.Builder()
                    .setBookingID(bookingID)
                    .setCleaningServices(cleaningServices)
                    .setWashAttendantID(washAttendantID)
                    .setBookingDateTime(bookingDateTime)
                    .setVehicle(vehicle)
                    .setTipAdd(tipAdd)
                    .setBookingCost(bookingCost)
                    .build();

            //Checks if a list of Payment objects was passed in.
            if (payments != null) {
                for (int i = 0; i < payments.size(); i++) {
                    Payment oldPayment = payments.get(i);

                    Payment newPayment = new Payment.Builder()
                            .copy(oldPayment)
                            .setBooking(booking)
                            .build();

                    payments.set(i, newPayment); // update the list with the rebuilt, linked payment
                }

                // Now inject the payments list into the booking using the builder again
                booking = new Booking.Builder()
                        .copy(booking)
                        .setPayments(payments)
                        .build();
            }

            return booking;


        }
    }
}


