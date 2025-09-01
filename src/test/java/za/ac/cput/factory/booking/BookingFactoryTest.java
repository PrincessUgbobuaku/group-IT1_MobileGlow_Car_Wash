package za.ac.cput.factory.booking;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.payment.PaymentFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFactoryTest {

    @Test
    public void testCreateBookingWithoutPayments() {
        // Creating cleaning services
        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
                "WAXING_AND_POLISHING",
                300,
                1.5,
                "Exterior Care"
        );

        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
                "CERAMIC_COATING",
                700,
                2.0,
                "Paint Protection"
        );

        List<CleaningService> services = List.of(cs_1, cs_2);

        // Dummy customer
        Customer cust_001 = new Customer.Builder()
                .setUserName("Brian")
                .setUserSurname("Kabongo")
                .setCustomerDOB(LocalDate.of(1990, 1, 1)).build();


        Vehicle vehicle = VehicleFactory.createVehicle(
                "ABC123",
                "Toyota",
                "Red",
                "Corolla",
                cust_001
        );


        // Dummy wash attendant
        WashAttendant washAttendant = new WashAttendant.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .build();

        Booking booking = BookingFactory.createBooking(
                services,
                null,
                washAttendant,
                LocalDateTime.of(2025, 10, 30, 12, 0),
                vehicle,
                true,
                500
        );

        assertNotNull(booking);
        assertNotNull(booking.getWashAttendant());
        assertEquals("John", booking.getWashAttendant().getUserName());
        assertEquals("Doe", booking.getWashAttendant().getUserSurname());

        assertNotNull(booking.getVehicle());
        assertEquals(2, booking.getCleaningServices().size());
        assertTrue(booking.getPayments().isEmpty());

        System.out.println("Booking without payments:\n" + booking);
    }

    @Test
    public void testCreateBookingWithMultiplePayments() {
        // Creating cleaning services
        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
                "WAXING_AND_POLISHING", 230.00, 2.5, "Detailing");

        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
                "INTERIOR_CLEANING", 500.00, 1, "Interior");

        List<CleaningService> services = List.of(cs_1, cs_2);

        // Dummy customer
        Customer customer = new Customer.Builder()
                .setUserName("Linda")
                .setUserSurname("Brown")
                .setCustomerDOB(LocalDate.of(1992, 5, 15))
                .build();

        // Dummy vehicle
        Vehicle vehicle = VehicleFactory.createVehicle(
                "XYZ789", "Mazda", "Blue", "3", customer
        );

        // Dummy wash attendant
        WashAttendant washAttendant = new WashAttendant.Builder()
                .setUserName("Alice")
                .setUserSurname("Smith")
                .build();

        // Temporary booking for payment linking
        Booking tempBooking = new Booking.Builder()
                .setCleaningServices(services)
                .setWashAttendant(washAttendant)
                .setBookingDateTime(LocalDateTime.now().plusDays(2))
                .setVehicle(vehicle)
                .setTipAdd(true)
                .setBookingCost(650)
                .build();

        // Payments linked to temporary booking
        Payment p1 = PaymentFactory.createPayment(tempBooking, 325, Payment.PaymentMethod.CREDIT, Payment.PaymentStatus.PENDING);
        Payment p2 = PaymentFactory.createPayment(tempBooking, 325, Payment.PaymentMethod.DEBIT, Payment.PaymentStatus.PENDING);

        List<Payment> payments = List.of(p1, p2);

        // Final booking with payments
        Booking booking = BookingFactory.createBooking(
                services,
                payments,
                washAttendant,
                LocalDateTime.now().plusDays(2),
                vehicle,
                true,
                650
        );

        assertNotNull(booking);
        assertNotNull(booking.getVehicle());
        assertEquals("Mazda", booking.getVehicle().getCarMake());
        assertEquals("Blue", booking.getVehicle().getCarColour());
        assertEquals(2, booking.getCleaningServices().size());
        assertEquals(2, booking.getPayments().size());

        for (Payment p : booking.getPayments()) {
            assertEquals(booking.getBookingID(), p.getBooking().getBookingID());
        }

        System.out.println("Booking with multiple payments:\n" + booking);
    }
}