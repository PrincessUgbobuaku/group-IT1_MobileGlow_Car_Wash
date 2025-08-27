//package za.ac.cput.service.booking;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import za.ac.cput.domain.booking.Booking;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.domain.booking.Vehicle;
//import za.ac.cput.factory.booking.BookingFactory;
//import za.ac.cput.repository.booking.IBookingRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class BookingService implements IBookingService{
//
//    private final IBookingRepository bookingRepository;
//    private final CleaningServiceService cleaningServiceService;
//    private final VehicleService vehicleService;
//
//    @Autowired
//    public BookingService(IBookingRepository bookingRepository,
//                          CleaningServiceService cleaningServiceService,
//                          VehicleService vehicleService) { // 👈 added
//        this.bookingRepository = bookingRepository;
//        this.cleaningServiceService = cleaningServiceService;
//        this.vehicleService = vehicleService; // 👈 assigned
//    }
//
//    public Booking create(Booking booking) {
//        List<CleaningService> validatedServices = new ArrayList<>();
//
//        //checking to see if requested service exists in cleaningservice db
//        for (CleaningService service : booking.getCleaningServices()) {
//            // Read the real service from DB using ID
//            CleaningService validatedService = cleaningServiceService.read(service.getCleaningServiceID());
//
//            if (validatedService == null) {
//                throw new IllegalArgumentException("Invalid cleaning service ID: " + service.getCleaningServiceID());
//            }
//
//            validatedServices.add(validatedService);
//        }
//
//        // ✅ Validate Vehicle
//        String vehicleID = booking.getVehicle().getVehicleID();
//        Vehicle validatedVehicle = vehicleService.read(vehicleID)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID: " + vehicleID));
//
//        // Calculate total cost
//        double totalCost = validatedServices.stream()
//                .mapToDouble(CleaningService::getPriceOfService)
//                .sum();
//
//        // Use factory to create booking (payments null, booking cost calculated)
//        Booking created = BookingFactory.createBooking(
//                validatedServices,
//                null,  // payments can be null initially
//                booking.getWashAttendantID(),
//                booking.getBookingDateTime(),
//                validatedVehicle,
//                booking.isTipAdd(),
//                totalCost
//        );
//
//        if (created == null) {
//            throw new IllegalArgumentException("Invalid Booking data");
//        }
//
//        return bookingRepository.save(created);
//    }
//
//    @Override
//    public Booking read(String id) {
//        return bookingRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public Booking update(Booking booking) {
//        return bookingRepository.save(booking);
//    }
//
//    @Override
//    public boolean delete(String id) {
//        if (bookingRepository.existsById(id)) {
//            bookingRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<Booking> getAll() {
//        return bookingRepository.findAll();
//    }
//
//
//}
