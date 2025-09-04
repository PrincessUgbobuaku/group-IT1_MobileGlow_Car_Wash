package za.ac.cput.service.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.booking.BookingFactory;
import za.ac.cput.service.booking.VehicleService;
import za.ac.cput.service.user.employee.WashAttendantService;

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

    @Autowired
    private WashAttendantService washAttendantService;

    private static Booking booking;
    private static Vehicle vehicle;
    private static WashAttendant washAttendant;

    @BeforeAll
    static void init() {
        System.out.println("🔧 BookingServiceTest starting...");
    }

    @Test
    @Rollback(value = false)
    @BeforeEach
    @Order(0)
    void setupEntities() {
        // Fetch an existing vehicle (ensure this ID exists in DB)
        vehicle = vehicleService.read(1L);
        assertNotNull(vehicle, "Vehicle should exist in the database");
        assertNotNull(vehicle.getCustomer(), "Vehicle should have a customer");

        // Fetch an existing wash attendant (ensure this ID exists in DB)
        washAttendant = washAttendantService.read(1L);
        assertNotNull(washAttendant, "WashAttendant should exist in the database");
    }

    @Test
    @Rollback(false)
    @Order(1)
    void testCreate() {
        CleaningService cleaningService_1 = cleaningServiceService.readByServiceName("INTERIOR_STEAM");
        CleaningService cleaningService_2 = cleaningServiceService.readByServiceName("DEEP_FULL_WASH");

        List<CleaningService> services = new ArrayList<>();
        services.add(cleaningService_1);
        services.add(cleaningService_2);

        // Optional: loop if needed
        for (CleaningService cs : services) {
            System.out.println("Loaded cleaning service: " + cs.getServiceName());
        }

        booking = BookingFactory.createBooking(
                services, vehicle, washAttendant, LocalDateTime.now().plusDays(1),
                 true, 0.0
        );

        booking = bookingService.create(booking); // persist with ID
        assertNotNull(booking.getBookingId());
        System.out.println("Created Booking: " + booking);
    }

    @Test
    @Rollback(value = false)
    @Transactional
    @Order(2)
    void testRead() {
        assertNotNull(booking, "Booking should be initialized");
        assertNotNull(booking.getBookingId(), "Booking ID should not be null");

        Booking read = bookingService.read(booking.getBookingId());
        assertNotNull(read);
        assertEquals(booking.getBookingId(), read.getBookingId());
        System.out.println(" Read Booking: " + read);
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback(false)
    void testUpdate() {
        Booking existingBooking = bookingService.read(booking.getBookingId());

        Booking updatedBooking = new Booking.Builder()
                .copy(existingBooking)
                .setTipAdd(true)
                .setBookingCost(existingBooking.getBookingCost())
                .build();

        Booking saved = bookingService.update(updatedBooking);

        assertNotNull(saved);
        assertTrue(saved.isTipAdd());
        System.out.println(" Updated Booking: " + saved);
    }

    @Test
    @Rollback(true)
    @Order(4)
    void testCreateFailsWithNoCleaningServices() {

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            BookingFactory.createBooking(
                    new ArrayList<>(), // empty services list
                    vehicle,
                    washAttendant,
                    LocalDateTime.now().plusDays(1),
                    false,
                    0.0
            );
        });

        assertEquals("At least one cleaning service is required", thrown.getMessage());
    }

    @Test
    @Rollback(true)
    @Order(6)
    void testCreateFailsWithDuplicateVehicleBooking() {
        // Create a booking at a specific datetime
        LocalDateTime bookingTime = LocalDateTime.now().plusDays(2);

        CleaningService cleaningService = cleaningServiceService.readByServiceName("SURFACE_WIPE");

        Booking firstBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        bookingService.create(firstBooking);

        // Attempt to create second booking for same vehicle and time - should fail
        Booking duplicateBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendantService.read(59L), // different wash attendant if needed
                bookingTime,
                false,
                0.0
        );

        Exception thrown = assertThrows(Exception.class, () -> {
            bookingService.create(duplicateBooking);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause, "Exception cause should not be null");
        assertTrue(cause instanceof IllegalArgumentException, "Cause should be IllegalArgumentException");
        assertEquals("This vehicle already has a booking at the selected time.", cause.getMessage());
    }

    @Test
    @Rollback(true)
    @Order(7)
    void testCreateFailsWithWashAttendantDoubleBooking() {
        LocalDateTime bookingTime = LocalDateTime.now().plusDays(3);

        CleaningService cleaningService = cleaningServiceService.readByServiceName("SURFACE_WIPE");

        Booking firstBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicleService.read(62L), // different vehicle if needed
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        bookingService.create(firstBooking);

        Booking duplicateBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicleService.read(63L), // different vehicle to isolate wash attendant double-booking
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        Exception thrown = assertThrows(Exception.class, () -> {
            bookingService.create(duplicateBooking);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause, "Exception cause should not be null");
        assertTrue(cause instanceof IllegalArgumentException, "Cause should be IllegalArgumentException");
        assertEquals("This wash attendant is already booked at the selected time.", cause.getMessage());
    }

    @Test
    @Rollback(true)
    @Order(8)
    void testCreateFailsWithPastBookingDate() {
        CleaningService cleaningService = cleaningServiceService.readByServiceName("SURFACE_WIPE");

        Booking pastBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendant,
                LocalDateTime.now().minusDays(1),
                false,
                0.0
        );

        Exception thrown = assertThrows(Exception.class, () -> {
            bookingService.create(pastBooking);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause, "Exception cause should not be null");
        assertTrue(cause instanceof IllegalArgumentException, "Cause should be IllegalArgumentException");
        assertEquals("Booking date must be in the future.", cause.getMessage());
    }

    @Test
    @Order(9)
    @Transactional
    void testGetAll() {
        List<Booking> bookings = bookingService.getAll();
        assertNotNull(bookings, "Booking list should not be null");
        assertTrue(bookings.size() >= 0, "Booking list size should be zero or more");

        System.out.println("✅ All Bookings: " + bookings);
    }


}