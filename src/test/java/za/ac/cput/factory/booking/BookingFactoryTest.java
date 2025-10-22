package za.ac.cput.factory.booking;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Card;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.payment.CardFactory;
import za.ac.cput.factory.payment.PaymentFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingFactoryTest {

        @Test
        public void testCreateBookingWithoutPayments() {
                // --- Cleaning Services ---
                CleaningService cs1 = CleaningServiceFactory.createCleaningService(
                        "WAXING_AND_POLISHING", 300, 1.5, "Exterior Care");

                CleaningService cs2 = CleaningServiceFactory.createCleaningService(
                        "CERAMIC_COATING", 700, 2.0, "Paint Protection");

                List<CleaningService> services = List.of(cs1, cs2);

                // --- Dummy Customer ---
                Customer customer = new Customer.Builder()
                        .setUserId(1L) // ✅ Added for completeness
                        .setUserName("Brian")
                        .setUserSurname("Kabongo")
                        .setCustomerDOB(LocalDate.of(1990, 1, 1))
                        .build();

                // --- Vehicle ---
                Vehicle vehicle = VehicleFactory.createVehicle(
                        "ABC123", "Toyota", "Red", "Corolla", customer);

                // --- Wash Attendant ---
                WashAttendant washAttendant = new WashAttendant.Builder()
                        .setUserName("John")
                        .setUserSurname("Doe")
                        .build();

                // --- Create Booking ---
                Booking booking = BookingFactory.createBooking(
                        services, vehicle, washAttendant,
                        LocalDateTime.of(2025, 10, 30, 12, 0),
                        true, 500);

                // --- Assertions ---
                assertNotNull(booking);
                assertEquals("John", booking.getWashAttendant().getUserName());
                assertEquals("Doe", booking.getWashAttendant().getUserSurname());
                assertEquals(2, booking.getCleaningServices().size());
                assertEquals(500, booking.getBookingCost());
                assertTrue(booking.isTipAdd());
                assertFalse(booking.isCancelled(), "New bookings should not be cancelled by default");

                System.out.println("\n✅ Booking without payments:\n" + booking);
        }

        @Test
        public void testCreateBookingWithMultiplePayments() {
                // --- Cleaning Services ---
                CleaningService cs1 = CleaningServiceFactory.createCleaningService(
                        "INTERIOR_CLEANING", 500.00, 1.0, "Interior");

                CleaningService cs2 = CleaningServiceFactory.createCleaningService(
                        "ENGINE_DETAILING", 350.00, 2.0, "Engine Bay");

                List<CleaningService> services = List.of(cs1, cs2);

                // --- Dummy Customer (Linked to Card + Vehicle + Payment) ---
                Customer customer = new Customer.Builder()
                        .setUserId(2L) // ✅ Required for PaymentFactory validation
                        .setUserName("Linda")
                        .setUserSurname("Brown")
                        .setCustomerDOB(LocalDate.of(1992, 5, 15))
                        .build();

                // --- Vehicle ---
                Vehicle vehicle = VehicleFactory.createVehicle(
                        "XYZ789", "Mazda", "Blue", "3", customer);

                // --- Wash Attendant ---
                WashAttendant washAttendant = new WashAttendant.Builder()
                        .setUserName("Alice")
                        .setUserSurname("Smith")
                        .build();

                // --- Dummy Card (Linked to Customer) ---
                Card dummyCard = CardFactory.createCard(
                        "4111111111111111",
                        "Linda Brown",
                        "123",
                        YearMonth.of(2027, 12),
                        customer // ✅ Customer link here
                );

                assertNotNull(dummyCard);
                assertNotNull(dummyCard.getCustomer());
                assertEquals(2L, dummyCard.getCustomer().getUserId());

                // --- Temporary Booking for Payment Linking ---
                Booking tempBooking = new Booking.Builder()
                        .setCleaningServices(services)
                        .setWashAttendant(washAttendant)
                        .setBookingDateTime(LocalDateTime.now().plusDays(1))
                        .setVehicle(vehicle)
                        .setTipAdd(true)
                        .setBookingCost(850)
                        .setCancelled(false)
                        .build();

                // --- Payments Linked to Customer ---
                Payment p1 = PaymentFactory.createPayment(
                        tempBooking,
                        425,
                        Payment.PaymentMethod.CARD,
                        Payment.PaymentStatus.PENDING,
                        dummyCard
                );

                Payment p2 = PaymentFactory.createPayment(
                        tempBooking,
                        425,
                        Payment.PaymentMethod.CASH,
                        Payment.PaymentStatus.PENDING
                );

                List<Payment> payments = List.of(p1, p2);

                // --- Final Booking Creation ---
                Booking booking = BookingFactory.createBooking(
                        services,
                        vehicle,
                        washAttendant,
                        LocalDateTime.now().plusDays(1),
                        true,
                        850
                );

                // --- Assertions ---
                assertNotNull(booking);
                assertEquals("Mazda", booking.getVehicle().getCarMake());
                assertEquals("Blue", booking.getVehicle().getCarColour());
                assertEquals(2, booking.getCleaningServices().size());
                assertTrue(booking.isTipAdd());
                assertFalse(booking.isCancelled());
                assertNotNull(dummyCard);

                System.out.println("\n✅ Booking with multiple payments:\n" + booking);
                System.out.println("💳 Linked payments:");
                payments.forEach(System.out::println);
        }

        @Test
        public void testCancelledBooking() {
                // --- Cleaning Service ---
                CleaningService service = CleaningServiceFactory.createCleaningService(
                        "BASIC_WASH", 100.00, 1.0, "Basic Cleaning");

                // --- Dummy Customer ---
                Customer customer = new Customer.Builder()
                        .setUserId(3L)
                        .setUserName("Sarah")
                        .setUserSurname("Lee")
                        .setCustomerDOB(LocalDate.of(1995, 8, 20))
                        .build();

                // --- Vehicle ---
                Vehicle vehicle = VehicleFactory.createVehicle(
                        "AAA111", "Honda", "Silver", "Civic", customer);

                // --- Wash Attendant ---
                WashAttendant washAttendant = new WashAttendant.Builder()
                        .setUserName("Tom")
                        .setUserSurname("Green")
                        .build();

                // --- Cancelled Booking ---
                Booking booking = new Booking.Builder()
                        .setCleaningServices(List.of(service))
                        .setVehicle(vehicle)
                        .setWashAttendant(washAttendant)
                        .setBookingDateTime(LocalDateTime.now())
                        .setTipAdd(false)
                        .setBookingCost(100)
                        .setCancelled(true)
                        .build();

                // --- Assertions ---
                assertNotNull(booking);
                assertTrue(booking.isCancelled(), "Booking should be marked as cancelled");

                System.out.println("\n🚫 Cancelled booking:\n" + booking);
        }
}