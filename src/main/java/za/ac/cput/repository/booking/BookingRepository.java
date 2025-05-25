package za.ac.cput.repository.booking;

import za.ac.cput.repository.booking.impl.IBookingRepository;

public class BookingRepository /*implements IBookingRepository*/ {

    /*
    *  private static BookingRepository repository = null;
    private Map<String, Booking> bookingDB;

    private BookingRepository() {
        this.bookingDB = new HashMap<>();
    }

    public static BookingRepository getRepository() {
        if (repository == null) {
            repository = new BookingRepository();
        }
        return repository;
    }

    @Override
    public Booking create(Booking booking) {
        bookingDB.put(booking.getBookingID(), booking);
        return booking;
    }

    @Override
    public Booking read(String bookingID) {
        return bookingDB.get(bookingID);
    }

    @Override
    public Booking update(Booking booking) {
        if (bookingDB.containsKey(booking.getBookingID())) {
            bookingDB.put(booking.getBookingID(), booking);
            return booking;
        }
        return null;
    }

    @Override
    public boolean delete(String bookingID) {
        return bookingDB.remove(bookingID) != null;
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookingDB.values());
    }
    *
    * */


}
