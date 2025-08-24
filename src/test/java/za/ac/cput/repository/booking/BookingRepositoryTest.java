package za.ac.cput.repository.booking;

public class BookingRepositoryTest {

    /*
    * private BookingRepository repository;
    private Booking booking;

    @BeforeEach
    void setUp() {
        repository = BookingRepository.getRepository();
        booking = BookingFactory.createBookingFactory1(
                List.of(), "12345", LocalDateTime.now().plusDays(1), "V001", false, 350.0);
        repository.create(booking);
    }

    @Test
    void testCreate() {
        assertNotNull(booking);
        assertEquals(booking, repository.read(booking.getBookingID()));
    }

    @Test
    void testRead() {
        Booking readBooking = repository.read(booking.getBookingID());
        assertNotNull(readBooking);
    }

    @Test
    void testUpdate() {
        Booking updated = new Booking.Builder().copy(booking).setTipAdd(true).build();
        repository.update(updated);
        assertTrue(repository.read(updated.getBookingID()).isTipAdd());
    }

    @Test
    void testDelete() {
        assertTrue(repository.delete(booking.getBookingID()));
    }

    @Test
    void testGetAll() {
        assertFalse(repository.getAll().isEmpty());
    }
    * */
}
