package za.ac.cput.service.booking;

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
            VehicleService vehicleService) { // 👈 added
        this.bookingRepository = bookingRepository;
        this.cleaningServiceService = cleaningServiceService;
        this.vehicleService = vehicleService; // 👈 assigned
    }

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

        // checking to see if requested service exists in cleaningservice db
        for (CleaningService service : booking.getCleaningServices()) {
            // Read the real service from DB using ID
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

        // ✅ Validate Vehicle
        long vehicleID = booking.getVehicle().getVehicleID();
        Vehicle validatedVehicle = vehicleService.read(vehicleID);

        if (validatedVehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle ID: " + vehicleID);
        }

        // ✅ Rule 3: Booking must have a wash attendant
        if (booking.getWashAttendant() == null || booking.getWashAttendant().getUserId() == null) {
            throw new IllegalArgumentException("Booking must have an assigned wash attendant.");
        }

        // ✅ Rule 6: Prevent duplicate bookings for the same vehicle
        if (bookingRepository.existsByVehicleAndBookingDateTime(validatedVehicle, booking.getBookingDateTime())) {
            throw new IllegalArgumentException("This vehicle already has a booking at the selected time.");
        }

        // ✅ Rule 6: Prevent wash attendant from being double-booked
        if (bookingRepository.existsByWashAttendantAndBookingDateTime(booking.getWashAttendant(),
                booking.getBookingDateTime())) {
            throw new IllegalArgumentException("This wash attendant is already booked at the selected time.");
        }

        // ✅ Rule 4: Booking date must be in the future
        if (booking.getBookingDateTime() == null
                || booking.getBookingDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Booking date must be in the future.");
        }

        // ✅ Rule 5: Calculate total cost from services
        double totalCost = validatedServices.stream()
                .mapToDouble(CleaningService::getPriceOfService)
                .sum();

        // Use factory to create booking (payments null, booking cost calculated)
        Booking created = BookingFactory.createBooking(
                validatedServices,
                validatedVehicle,
                booking.getWashAttendant(),
                booking.getBookingDateTime(),
                booking.isTipAdd(),
                totalCost);

        if (created == null) {
            throw new IllegalArgumentException("Invalid Booking data");
        }

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

        // Use builder to copy and update fields
        Booking updatedBooking = new Booking.Builder()
                .copy(existingBooking)
                .setBookingDateTime(booking.getBookingDateTime())
                .setCleaningServices(booking.getCleaningServices())
                .setVehicle(booking.getVehicle())
                .setWashAttendant(booking.getWashAttendant())
                .setTipAdd(booking.isTipAdd())
                .setBookingCost(booking.getBookingCost())
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
        return bookingRepository.existsByVehicleAndBookingDateTime(vehicle, bookingDateTime);
    }

}
