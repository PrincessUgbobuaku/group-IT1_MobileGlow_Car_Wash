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
        vehicle = vehicleService.read(1L);
        assertNotNull(vehicle, "Vehicle should exist in the database");
        assertNotNull(vehicle.getCustomer(), "Vehicle should have a customer");

        washAttendant = washAttendantService.read(1L);
        assertNotNull(washAttendant, "WashAttendant should exist in the database");
    }

    @Test
    @Rollback(false)
    @Order(1)
    void testCreate() {
        CleaningService cleaningService_1 = cleaningServiceService.readByServiceName("INTERIOR_STEAM");
        CleaningService cleaningService_2 = cleaningServiceService.readByServiceName("JET_WASH");

        List<CleaningService> services = new ArrayList<>();
        services.add(cleaningService_1);
        services.add(cleaningService_2);

        booking = BookingFactory.createBooking(
                services, vehicle, washAttendant, LocalDateTime.now().plusDays(1),
                true, 0.0
        );

        booking = bookingService.create(booking);
        assertNotNull(booking.getBookingId());
        assertFalse(booking.isCancelled(), "New booking should not be cancelled by default");
        System.out.println("✅ Created Booking: " + booking);
    }

    @Test
    @Rollback(false)
    @Transactional
    @Order(2)
    void testRead() {
        assertNotNull(booking, "Booking should be initialized");
        Booking read = bookingService.read(booking.getBookingId());
        assertNotNull(read);
        assertEquals(booking.getBookingId(), read.getBookingId());
        System.out.println("📖 Read Booking: " + read);
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
        System.out.println("🛠️ Updated Booking: " + saved);
    }

    @Test
    @Rollback(true)
    @Order(4)
    void testCreateFailsWithNoCleaningServices() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            BookingFactory.createBooking(
                    new ArrayList<>(),
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
        LocalDateTime bookingTime = LocalDateTime.now().plusDays(2);

        CleaningService cleaningService = cleaningServiceService.readByServiceName("OIL_COATING");

        Booking firstBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        bookingService.create(firstBooking);

        Booking duplicateBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendantService.read(13L),
                bookingTime,
                false,
                0.0
        );

        Exception thrown = assertThrows(Exception.class, () -> {
            bookingService.create(duplicateBooking);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("This vehicle already has an active booking at the selected time.", cause.getMessage());
    }

    @Test
    @Rollback(true)
    @Order(7)
    void testCreateFailsWithWashAttendantDoubleBooking() {
        LocalDateTime bookingTime = LocalDateTime.now().plusDays(3);

        CleaningService cleaningService = cleaningServiceService.readByServiceName("OIL_COATING");

        Booking firstBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicleService.read(13L),
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        bookingService.create(firstBooking);

        Booking duplicateBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicleService.read(13L),
                washAttendant,
                bookingTime,
                false,
                0.0
        );

        Exception thrown = assertThrows(Exception.class, () -> {
            bookingService.create(duplicateBooking);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("This wash attendant is already booked at the selected time.", cause.getMessage());
    }

    @Test
    @Rollback(true)
    @Order(8)
    void testCreateFailsWithPastBookingDate() {
        CleaningService cleaningService = cleaningServiceService.readByServiceName("WATERLESS_WASH");

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
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("Booking date must be in the future.", cause.getMessage());
    }

    @Test
    @Order(9)
    @Transactional
    void testGetAll() {
        List<Booking> bookings = bookingService.getAll();
        assertNotNull(bookings);
        assertTrue(bookings.size() >= 0);
        System.out.println("📋 All Bookings: " + bookings);
    }

    // 🆕 NEW TESTS BELOW
    // ----------------------------------------------------------------------

    @Test
    @Order(10)
    @Rollback(false)
    @Transactional
    void testCancelBooking() {
        // Create a new active booking
        CleaningService cleaningService = cleaningServiceService.readByServiceName("WATERLESS_WASH");
        Booking newBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendant,
                LocalDateTime.now().plusDays(4),
                false,
                100.0
        );

        Booking savedBooking = bookingService.create(newBooking);

        // Cancel it
        Booking cancelledBooking = bookingService.cancelBooking(savedBooking.getBookingId());

        assertNotNull(cancelledBooking);
        assertTrue(cancelledBooking.isCancelled(), "Booking should be marked as cancelled");
        System.out.println("❌ Cancelled Booking: " + cancelledBooking);
    }

    @Test
    @Order(11)
    @Rollback(true)
    void testHasBookingConflict() {
        LocalDateTime bookingTime = LocalDateTime.now().plusDays(5);
        CleaningService cleaningService = cleaningServiceService.readByServiceName("WATERLESS_WASH");

        Booking conflictBooking = BookingFactory.createBooking(
                List.of(cleaningService),
                vehicle,
                washAttendant,
                bookingTime,
                false,
                120.0
        );

        // ✅ Save and capture the returned booking with generated ID
        Booking savedBooking = bookingService.create(conflictBooking);

        // ✅ Verify conflict exists (vehicle/time combination)
        boolean conflictExists = bookingService.hasBookingConflict(vehicle.getVehicleID(), bookingTime);
        assertTrue(conflictExists, "Conflict should exist for same vehicle/time");

        // ✅ Cancel using saved booking ID (not the unsaved one)
        bookingService.cancelBooking(savedBooking.getBookingId());

        // ✅ Check conflict again — should now be false
        boolean conflictAfterCancel = bookingService.hasBookingConflict(vehicle.getVehicleID(), bookingTime);
        assertFalse(conflictAfterCancel, "Conflict should no longer exist after cancellation");
    }
}