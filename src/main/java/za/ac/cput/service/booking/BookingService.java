package za.ac.cput.service.booking;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.factory.booking.BookingFactory;
import za.ac.cput.repository.booking.IBookingRepository;
import za.ac.cput.service.user.employee.WashAttendantService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    private final IBookingRepository bookingRepository;
    private final CleaningServiceService cleaningServiceService;
    private final VehicleService vehicleService;

    @Autowired
    public BookingService(IBookingRepository bookingRepository,
                          CleaningServiceService cleaningServiceService,
                          VehicleService vehicleService) {
        this.bookingRepository = bookingRepository;
        this.cleaningServiceService = cleaningServiceService;
        this.vehicleService = vehicleService;
    }

    @Override
    public Booking create(Booking booking) {

        System.out.println("Received cleaning services: " + booking.getCleaningServices());
        for (CleaningService cs : booking.getCleaningServices()) {
            System.out.println("CleaningService ID: " + cs.getCleaningServiceId());
        }

        // ✅ Rule 1: At least one cleaning service
        if (booking.getCleaningServices() == null || booking.getCleaningServices().isEmpty()) {
            throw new IllegalArgumentException("At least one cleaning service must be selected.");
        }

        List<CleaningService> validatedServices = new ArrayList<>();

        // ✅ Validate cleaning services from DB
        for (CleaningService service : booking.getCleaningServices()) {
            CleaningService validatedService = cleaningServiceService.read(service.getCleaningServiceId());
            if (validatedService == null) {
                throw new IllegalArgumentException("Invalid cleaning service ID: " + service.getCleaningServiceId());
            }
            validatedServices.add(validatedService);
        }

        // ✅ Rule 2: Booking must have a vehicle
        if (booking.getVehicle() == null || booking.getVehicle().getVehicleID() == null) {
            throw new IllegalArgumentException("Booking must include a vehicle.");
        }

        Vehicle validatedVehicle = vehicleService.read(booking.getVehicle().getVehicleID());
        if (validatedVehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + booking.getVehicle().getVehicleID());
        }

        // ✅ Rule 3: Booking must have a wash attendant
        if (booking.getWashAttendant() == null || booking.getWashAttendant().getUserId() == null) {
            throw new IllegalArgumentException("Booking must have an assigned wash attendant.");
        }

        // ✅ Rule 4: Booking date must be in the future
        if (booking.getBookingDateTime() == null
                || booking.getBookingDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Booking date must be in the future.");
        }

        // ✅ Rule 5a: Check wash attendant conflict FIRST
        if (bookingRepository.existsByWashAttendantAndBookingDateTimeAndCancelledFalse(
                booking.getWashAttendant(), booking.getBookingDateTime())) {
            throw new IllegalArgumentException("This wash attendant is already booked at the selected time.");
        }

        // ✅ Rule 5b: Then check vehicle conflict
        if (bookingRepository.existsByVehicleAndBookingDateTimeAndCancelledFalse(
                validatedVehicle, booking.getBookingDateTime())) {
            throw new IllegalArgumentException("This vehicle already has an active booking at the selected time.");
        }

        // ✅ Rule 6: Calculate total cost
        double totalCost = validatedServices.stream()
                .mapToDouble(CleaningService::getPriceOfService)
                .sum();

        // ✅ Create booking and ensure it's not cancelled by default
        Booking created = new Booking.Builder()
                .copy(BookingFactory.createBooking(
                        validatedServices,
                        validatedVehicle,
                        booking.getWashAttendant(),
                        booking.getBookingDateTime(),
                        booking.isTipAdd(),
                        totalCost
                ))
                .setCancelled(false)
                .build();

        return bookingRepository.save(created);
    }

    @Override
    public Booking read(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking update(Booking booking) {
        List<Booking> existingBookings = bookingRepository.findByBookingId(booking.getBookingId());

        if (existingBookings.isEmpty()) {
            throw new RuntimeException("Booking not found with id " + booking.getBookingId());
        }

        Booking existingBooking = existingBookings.get(0);

        // ✅ Copy and update including cancellation status
        Booking updatedBooking = new Booking.Builder()
                .copy(existingBooking)
                .setBookingDateTime(booking.getBookingDateTime())
                .setCleaningServices(booking.getCleaningServices())
                .setVehicle(booking.getVehicle())
                .setWashAttendant(booking.getWashAttendant())
                .setTipAdd(booking.isTipAdd())
                .setBookingCost(booking.getBookingCost())
                .setCancelled(booking.isCancelled())
                .build();

        return bookingRepository.save(updatedBooking);
    }

    @Override
    public boolean delete(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public boolean hasBookingConflict(Long vehicleId, java.time.LocalDateTime bookingDateTime) {
        Vehicle vehicle = vehicleService.read(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleId);
        }
        // ✅ Only check for non-cancelled bookings
        return bookingRepository.existsByVehicleAndBookingDateTimeAndCancelledFalse(vehicle, bookingDateTime);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null when cancelling a booking.");
        }

        Booking booking = read(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found with id: " + bookingId);
        }

        if (booking.isCancelled()) {
            throw new IllegalStateException("Booking is already cancelled.");
        }

        Booking cancelledBooking = new Booking.Builder()
                .copy(booking)
                .setCancelled(true)
                .build();

        return bookingRepository.save(cancelledBooking);
    }
}