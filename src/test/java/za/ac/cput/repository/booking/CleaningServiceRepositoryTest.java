package za.ac.cput.repository.booking;

public class CleaningServiceRepositoryTest {

    /*
    * private CleaningServiceRepository repository;
    private CleaningService cleaningService;

    @BeforeEach
    void setUp() {
        repository = CleaningServiceRepository.getRepository();
        cleaningService = CleaningServiceFactory.createCleaningServiceFactory1(
                CleaningService.ServiceName.ENGINE_CLEANING, 500.0, 2.5);
        repository.create(cleaningService);
    }

    @Test
    void testCreate() {
        assertNotNull(cleaningService);
        assertEquals(cleaningService, repository.read(cleaningService.getCleaningServiceID()));
    }

    @Test
    void testRead() {
        CleaningService readService = repository.read(cleaningService.getCleaningServiceID());
        assertNotNull(readService);
    }

    @Test
    void testUpdate() {
        CleaningService updated = new CleaningService.Builder()
                .copy(cleaningService)
                .setPriceOfService(600.0)
                .build();
        repository.update(updated);
        assertEquals(600.0, repository.read(updated.getCleaningServiceID()).getPriceOfService());
    }

    @Test
    void testDelete() {
        assertTrue(repository.delete(cleaningService.getCleaningServiceID()));
    }

    @Test
    void testGetAll() {
        assertFalse(repository.getAll().isEmpty());
    }
    *
    * */
}
