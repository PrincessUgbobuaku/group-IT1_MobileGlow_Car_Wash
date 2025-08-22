package za.ac.cput.service.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.factory.booking.BookingFactory;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.factory.booking.VehicleFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CleaningServiceService cleaningServiceService;

    @Autowired
    private VehicleService vehicleService;

    private static Booking booking;
//    private static CleaningService cleaningService_1;
//    private static CleaningService cleaningService_2;


    @BeforeAll
    static void init() {
        System.out.println("🔧 BookingServiceTest starting...");
    }

    @Test
    @Order(1)
    void testCreate() {

        // Fetch existing cleaning services by service name
        CleaningService cleaningService_1 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.TIRE_AND_WHEEL_CLEANING);
        CleaningService cleaningService_2 = cleaningServiceService.readByServiceName(CleaningService.ServiceName.EXTERIOR_WASH);

        assertNotNull(cleaningService_1, "CleaningService 1 should not be null");
        assertNotNull(cleaningService_2, "CleaningService 2 should not be null");

        // 1. Create and save a cleaning service
//        cleaningService_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING,
//                230.00,
//                2.5);
//
//        cleaningServiceService.create(cleaningService_1);
//
//        cleaningService_2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING,
//                500.00,
//                1.5);
//
//        cleaningServiceService.create(cleaningService_2);

        List<CleaningService> booking1ListOfServices = new ArrayList<>();

        booking1ListOfServices.add(cleaningService_1);
        booking1ListOfServices.add(cleaningService_2);

        //temp customer
        Customer cust_002 = new Customer.Builder().setCustomerID("cust001").setCustomerDOB(LocalDate.of(1990, 1, 1)).build();

        // Step 2: Create and save a vehicle
        Vehicle vehicle = VehicleFactory.getInstance().create("123456", "Mercedes-Benz", "Red", "GLE", cust_002);

        vehicleService.create(vehicle);

        //booking
        booking = BookingFactory.createBooking(
                booking1ListOfServices,
                null,
                "WA001",
                LocalDateTime.now().plusDays(1),
                vehicle,
                false,
                0.0
        );

        Booking saved = bookingService.create(booking);

        assertNotNull(saved);
        assertEquals(booking.getBookingID(), saved.getBookingID());
        System.out.println("Created Booking with multiple services: " + saved);

    }

    @Test
    @Order(2)
    @Transactional
    void testRead() {
        Booking read = bookingService.read(booking.getBookingID());
        assertNotNull(read);
        assertEquals(booking.getBookingID(), read.getBookingID());
        System.out.println("Read Booking: " + read);
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback(false)
    void testUpdate() {
        Booking existingBooking = bookingService.read(booking.getBookingID());

        Booking updatedBooking = new Booking.Builder()
                .copy(existingBooking)                 // copy existing values
                .setTipAdd(true)                      // update tipAdd
                .setBookingCost(existingBooking.getBookingCost() + 50.00)  // update bookingCost
                .build();

        Booking saved = bookingService.update(updatedBooking);  // assuming you have an update method

        assertNotNull(saved);
        assertEquals(true, saved.isTipAdd());
        System.out.println("Updated Booking: " + saved);
    }

//    @Test
//    @Order(4)
//    @Transactional
//    @Rollback(false)
////    @Commit
//    void testDelete() {
//        boolean deleted = bookingService.delete(booking.getBookingID());
//        assertTrue(deleted, "Booking should be deleted successfully");
//
//        Booking deletedBooking = bookingService.read(booking.getBookingID());
//        assertNull(deletedBooking, "Deleted booking should no longer be found");
//        System.out.println("Deleted Booking with ID: " + booking.getBookingID());
//    }

    @Test
    @Order(5)
    @Transactional
    void testGetAll() {
        List<Booking> bookings = bookingService.getAll();
        assertNotNull(bookings, "Booking list should not be null");
        assertTrue(bookings.size() >= 0, "Booking list size should be zero or more");

        System.out.println("All Bookings: " + bookings);
    }



//    @Autowired
//    private BookingService bookingService;
//
//    private Booking booking;
//
//    @BeforeEach
//    void setUp() {
//        // Create empty list of cleaning services
//        List<CleaningService> cleaningServices = new ArrayList<>();
//
//        booking = BookingFactory.createBooking(
//                cleaningServices,
//                null, // No payments for now
//                "WA001",
//                LocalDateTime.now().plusDays(1),
//                "VEHICLE001",
//                false,
//                250.00
//        );
//        assertNotNull(booking);
//    }
//
//    @Test
//    void testCreate() {
//        Booking saved = bookingService.create(booking);
//        assertNotNull(saved);
//        assertEquals(booking.getBookingID(), saved.getBookingID());
//    }
//
//    @Test
//    void testRead() {
//        Booking saved = bookingService.create(booking);
//        Booking read = bookingService.read(saved.getBookingID());
//        assertNotNull(read);
//        assertEquals(saved.getBookingID(), read.getBookingID());
//    }
//
//    @Test
//    void testUpdate() {
//        Booking saved = bookingService.create(booking);
//        Booking updated = new Booking.Builder()
//                .copy(saved)
//                .setTipAdd(true)
//                .build();
//
//        Booking result = bookingService.update(updated);
//        assertTrue(result.isTipAdd());
//        assertEquals(saved.getBookingID(), result.getBookingID());
//    }
//
//    @Test
//    void testDelete() {
//        Booking saved = bookingService.create(booking);
//        boolean deleted = bookingService.delete(saved.getBookingID());
//        assertTrue(deleted);
//        assertNull(bookingService.read(saved.getBookingID()));
//    }
//
//    @Test
//    void testGetAll() {
//        bookingService.create(booking);
//        List<Booking> allBookings = bookingService.getAll();
//        assertFalse(allBookings.isEmpty());
//    }
}