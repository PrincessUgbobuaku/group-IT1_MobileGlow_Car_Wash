package za.ac.cput;

import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.employee.WashAttendant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

/*UNCOMMENT TO SEE HOW PROGRAM RUNS*/

    public static void main(String[] args) {

        Vehicle vehicle_1 = new Vehicle.Builder()
                .vehicleID("vehicle_1")
                .carPlateNumber("CAA123456")
                .carMake("Toyota")
                .carColour("Black")
                .carModel("Corolla")
                .customerID("get_customer_1")
                .build();

        Vehicle vehicle_2 = new Vehicle.Builder()
                .vehicleID("vehicle_2")
                .carPlateNumber("CAA789101")
                .carMake("Mercedes-Benz")
                .carColour("Blue")
                .carModel("C-Class")
                .customerID("get_customer_2")
                .build();

        List<CleaningService> booking1ListOfServices = new ArrayList<>(); //booking 1 list where we store all services requested

        CleaningService cs_1 = new CleaningService.Builder() //cleaning service 1 of booking 1
                .setCleaningServiceID("cs_1")
                .setServiceName(CleaningService.ServiceName.WAXING_AND_POLISHING)
                .setPriceOfService(230.00)
                .setDuration(2.5)
                .build();

        CleaningService cs_2 = new CleaningService.Builder() //cleaning service 2 of booking 1
                .setCleaningServiceID("cs_2")
                .setServiceName(CleaningService.ServiceName.INTERIOR_CLEANING)
                .setPriceOfService(500.00)
                .setDuration(1)
                .build();

        //adding cleaning services to list
        booking1ListOfServices.add(cs_1);
        booking1ListOfServices.add(cs_2);

        Booking.Builder booking1 = new Booking.Builder(); // new builder booking object - created so that we can use the method that is in Builder which is calculateBookingCost
        WashAttendant washAttendant_1 = new WashAttendant("wa_1", true, 5); //wash attendant 1

        Booking booking_1 = new Booking.Builder() //defining attributes of booking 2
                .setBookingID("bkg_1")
                .setCleaningServices(booking1ListOfServices)
                .setWashAttendantID(washAttendant_1.getWashAttendantID())
                .setBookingDateTime(LocalDateTime.of(2025, 5, 8, 12, 0))
                .setVehicleID(vehicle_1.getVehicleID())
                .setTipAdd(true)
                .setBookingCost(booking1.calculateBookingCost(booking1ListOfServices))
                .build();

        //next booking

        List<CleaningService> booking2ListOfServices = new ArrayList<>(); //booking list where we store all services requested

        CleaningService cs_3 = new CleaningService.Builder() //service 1
                .setCleaningServiceID("cs_3")
                .setServiceName(CleaningService.ServiceName.EXTERIOR_WASH)
                .setPriceOfService(200.00)
                .setDuration(2.5)
                .build();

        CleaningService cs_4 = new CleaningService.Builder() //service 2
                .setCleaningServiceID("cs_4")
                .setServiceName(CleaningService.ServiceName.ENGINE_CLEANING)
                .setPriceOfService(200.00)
                .setDuration(1)
                .build();

        //adding cleaning services to list
        booking2ListOfServices.add(cs_3);
        booking2ListOfServices.add(cs_4);

        Booking.Builder booking2 = new Booking.Builder(); // new builder booking object - created so that we can use the method that is in Builder which is calculateBookingCost

        WashAttendant washAttendant_2 = new WashAttendant("wa_1", true, 5); //wash attendant 1

        Booking booking_2 = new Booking.Builder() //defining attributes of booking 2
                .setBookingID("bkg_2")
                .setCleaningServices(booking2ListOfServices)
                .setWashAttendantID(washAttendant_2.getWashAttendantID())
                .setBookingDateTime(LocalDateTime.of(2025, 5, 8, 12, 0))
                .setVehicleID(vehicle_2.getVehicleID())
                .setBookingCost(booking2.calculateBookingCost(booking2ListOfServices))
                .build();

        System.out.println(booking_1.toString());
        System.out.println(booking_2.toString());


        Payment.Builder paymentBuilder = new Payment.Builder();

        double calculatedAmount1 = paymentBuilder.calculatePaymentAmount(booking_1);

        Payment payment_1 = new Payment.Builder()
                .setPaymentID("payment_1")
                .setBookingID(booking_1.getBookingID())
                .setPaymentAmount(calculatedAmount1)
                .setPaymentMethod(Payment.PaymentMethod.CREDIT)
                .setPaymentStatus(Payment.PaymentStatus.PENDING)
                .build();

         double calculatedAmount2 = paymentBuilder.calculatePaymentAmount(booking_2);

        Payment payment_2 = new Payment.Builder()
                .setPaymentID("payment_2")
                .setBookingID(booking_2.getBookingID())
                .setPaymentAmount(calculatedAmount2)
                .setPaymentMethod(Payment.PaymentMethod.CREDIT)
                .setPaymentStatus(Payment.PaymentStatus.PENDING)
                .build();

        System.out.println("Payment for the first booking: " + payment_1.toString());
        System.out.println("Payment for the second booking: " + payment_2.toString());



    }
}
