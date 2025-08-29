//package za.ac.cput.factory.booking;
//
//import org.junit.jupiter.api.Test;  // JUnit 5 Test annotation
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.domain.booking.Vehicle;
//import za.ac.cput.domain.payment.Payment;
//import za.ac.cput.domain.user.Customer;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.factory.payment.PaymentFactory;
//import za.ac.cput.factory.booking.VehicleFactory;
//import za.ac.cput.factory.user.CustomerFactory;
//
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class BookingFactoryTest {
//
//    @Test
//    public void testCreateBookingWithoutPayments() {
//
//        //creating my services
//        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 230.00, 2.5);
//
//        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 500.00, 1);
//
//        //creating list to store array of cleaning services
//        List<CleaningService> booking1ListOfServices = new ArrayList<>();
//
//        //storing cleaning services in array
//        booking1ListOfServices.add(cs_1);
//        booking1ListOfServices.add(cs_2);
//
//        //temp customer
//        Customer cust_001 = new Customer.Builder().setCustomerID("cust001").setCustomerDOB(LocalDate.of(1990, 1, 1)).build();
//
//        Vehicle vehicle1 = VehicleFactory.getInstance().create("222222", "Toyota", "Black", "Camry", cust_001);
//
//        Booking booking1 = BookingFactory.createBooking(booking1ListOfServices, null,"WA001", (LocalDateTime.of(2025, 10, 30, 12, 0)), vehicle1, true, 500);
//
//        assertNotNull(booking1);
//        assertEquals("WA001", booking1.getWashAttendantID());
//        assertEquals(vehicle1.getVehicleID(), booking1.getVehicle().getVehicleID());
//        assertEquals(2, booking1.getCleaningServices().size());
//        assertEquals(0, booking1.getPayments().size());
//
//        System.out.println("Booking without payments created:\n" + booking1.toString());
//
//    }
//
//    @Test
//    public void testCreateBookingWithMultiplePayments() {
//
//        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 230.00, 2.5);
//
//        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 500.00, 1);
//
//        List<CleaningService> booking2ListOfServices = new ArrayList<>();
//
//        booking2ListOfServices.add(cs_1);
//        booking2ListOfServices.add(cs_2);
//
//        //temp customer
//        Customer cust_001 = new Customer.Builder().setCustomerID("cust001").setCustomerDOB(LocalDate.of(1990, 1, 1)).build();
//
//        Vehicle vehicle1 = VehicleFactory.getInstance().create("222222", "Toyota", "Black", "Camry", cust_001);
//
//
//
//        Booking tempBooking = new Booking.Builder()
////                .setBookingID("TEMP") // temporary, will be overwritten by factory
//                .setCleaningServices(booking2ListOfServices)
//                .setWashAttendantID("WA002")
//                .setBookingDateTime(LocalDateTime.now().plusDays(2))
//                .setVehicle(vehicle1)
//                .setTipAdd(true)
//                .setBookingCost(650)
//                .build();
//
//        Payment p1 = PaymentFactory.createPayment(tempBooking, 325, Payment.PaymentMethod.CREDIT, Payment.PaymentStatus.PENDING);
//        Payment p2 = PaymentFactory.createPayment(tempBooking, 325, Payment.PaymentMethod.DEBIT, Payment.PaymentStatus.PENDING);
//
//        List<Payment> paymentList = new ArrayList<>();
//
//        paymentList.add(p1);
//        paymentList.add(p2);
//
//        Booking booking = BookingFactory.createBooking(
//                booking2ListOfServices,
//                paymentList,
//                "WA002",
//                LocalDateTime.now().plusDays(2),
//                vehicle1,
//                true,
//                650
//        );
//
//        assertNotNull(booking);
//        assertEquals(vehicle1.getVehicleID(), booking.getVehicle().getVehicleID());
//        assertEquals(2, booking.getCleaningServices().size());
//        assertEquals(2, booking.getPayments().size());
//
//        for (int i = 0; i < booking.getPayments().size(); i++) {
//            Payment p = booking.getPayments().get(i);
//            assertEquals(booking.getBookingID(), p.getBooking().getBookingID());
//        }
//
//        System.out.println("Booking with multiple payments:\n" + booking);
//
//
//    }
//}
//
