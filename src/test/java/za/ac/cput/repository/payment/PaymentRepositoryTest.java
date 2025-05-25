package za.ac.cput.repository.payment;

public class PaymentRepositoryTest {

    /*
    * private PaymentRepository repository;
    private Payment payment;
    private Booking booking;

    @BeforeEach
    void setUp() {
        repository = PaymentRepository.getRepository();
        booking = BookingFactory.createBookingFactory1(
                List.of(), "12345", LocalDateTime.now().plusDays(1), "V001", false, 350.0);
        payment = PaymentFactory.createPaymentFactory1(
                booking, 350.0, Payment.PaymentMethod.CARD, Payment.PaymentStatus.PAID);
        repository.create(payment);
    }

    @Test
    void testCreate() {
        assertNotNull(payment);
        assertEquals(payment, repository.read(payment.getPaymentID()));
    }

    @Test
    void testRead() {
        Payment readPayment = repository.read(payment.getPaymentID());
        assertNotNull(readPayment);
    }

    @Test
    void testUpdate() {
        Payment updated = new Payment.Builder()
                .copy(payment)
                .setPaymentAmount(400.0)
                .build();
        repository.update(updated);
        assertEquals(400.0, repository.read(updated.getPaymentID()).getPaymentAmount());
    }

    @Test
    void testDelete() {
        assertTrue(repository.delete(payment.getPaymentID()));
    }

    @Test
    void testGetAll() {
        assertFalse(repository.getAll().isEmpty());
    }
    *
    * */
}
