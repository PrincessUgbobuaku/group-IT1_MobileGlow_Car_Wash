//package za.ac.cput.repository.booking.impl;
//
//import org.junit.jupiter.api.*;
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.factory.booking.CleaningServiceFactory;
//import za.ac.cput.repository.booking.IBookingRepository;
//import za.ac.cput.repository.booking.impl.BookingRepositoryImpl;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class BookingRepositoryImplTest {
//
//    private static IBookingRepository repository = BookingRepositoryImpl.getRepository();
//
//    private static CleaningService cs1;
//    private static CleaningService cs2;
//    private static List<CleaningService> services;
//    private static Booking booking;
//
//    @BeforeAll
//    static void setUp() {
//        cs1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 230.00, 2.5);
//        cs2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 500.00, 1.0);
//
//        services = new ArrayList<>();
//        services.add(cs1);
//        services.add(cs2);
//
//        booking = BookingFactory.createBooking(
//                services, "attendant_001", LocalDateTime.of(2025, 10, 30, 12, 0),
//                "vehicle001", true, 730.00);
//    }
//
//    @Test
//    @Order(1)
//    void create() {
//        Booking created = repository.create(booking);
//        assertNotNull(created);
//        System.out.println("Created Booking: " + created);
//    }
//
//    @Test
//    @Order(2)
//    void read() {
//        Booking read = repository.read(booking.getBookingID());
//        assertNotNull(read);
//        System.out.println("Read Booking: " + read);
//    }
//
//    @Test
//    @Order(3)
//    void update() {
//        Booking updatedBooking = new Booking.Builder()
//                .copy(booking)
//                .setTipAdd(false)
//                .setBookingCost(700.00)
//                .build();
//
//        Booking updated = repository.update(updatedBooking);
//        assertNotNull(updated);
//        assertFalse(updated.isTipAdd());
//        assertEquals(700.00, updated.getBookingCost());
//        System.out.println("Updated Booking: " + updated);
//    }
//
//    @Test
//    @Order(4)
//    void delete() {
//        boolean success = repository.delete(booking.getBookingID());
//        assertTrue(success);
//        System.out.println("Deleted Booking ID: " + booking.getBookingID());
//    }
//
//    @Test
//    @Order(5)
//    void getAll() {
//        System.out.println("All Bookings: " + repository.getAll());
//    }
//}