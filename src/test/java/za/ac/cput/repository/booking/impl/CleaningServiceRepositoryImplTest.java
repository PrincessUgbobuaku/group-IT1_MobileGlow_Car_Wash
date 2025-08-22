//package za.ac.cput.repository.booking.impl;
//
//import org.junit.jupiter.api.*;
//import za.ac.cput.domain.booking.CleaningService;
//import za.ac.cput.factory.booking.CleaningServiceFactory;
//import za.ac.cput.repository.booking.ICleaningServiceRepository;
//import za.ac.cput.repository.booking.impl.CleaningServiceRepositoryImpl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class CleaningServiceRepositoryImplTest {
//
//    private static ICleaningServiceRepository repository = CleaningServiceRepositoryImpl.getRepository();
//
//    private static CleaningService cleaningService_1;
//    private static CleaningService cleaningService_2;
//
//    @BeforeAll
//    static void setup() {
//        cleaningService_1 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.INTERIOR_CLEANING, 350.00, 1.5);
//
//        cleaningService_2 = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.WAXING_AND_POLISHING, 400.00, 2.0);
//    }
//
//    @Test
//    @Order(1)
//    void create() {
//        CleaningService created1 = repository.create(cleaningService_1);
//        assertNotNull(created1);
//        System.out.println("Created CleaningService 1: " + created1);
//
//        CleaningService created2 = repository.create(cleaningService_2);
//        assertNotNull(created2);
//        System.out.println("Created CleaningService 2: " + created2);
//    }
//
//    @Test
//    @Order(2)
//    void read() {
//        CleaningService read1 = repository.read(cleaningService_1.getCleaningServiceID());
//        assertNotNull(read1);
//        System.out.println("Read CleaningService 1: " + read1);
//
//        CleaningService read2 = repository.read(cleaningService_2.getCleaningServiceID());
//        assertNotNull(read2);
//        System.out.println("Read CleaningService 2: " + read2);
//    }
//
//    @Test
//    @Order(3)
//    void update() {
//        CleaningService updatedService1 = new CleaningService.Builder().copy(cleaningService_1)
//                .setPriceOfService(375.00)
//                .build();
//
//        CleaningService updated1 = repository.update(updatedService1);
//        assertNotNull(updated1);
//        assertEquals(375.00, updated1.getPriceOfService());
//        System.out.println("Updated CleaningService 1: " + updated1);
//
//        CleaningService updatedService2 = new CleaningService.Builder().copy(cleaningService_2)
//                .setPriceOfService(425.00)
//                .build();
//
//        CleaningService updated2 = repository.update(updatedService2);
//        assertNotNull(updated2);
//        assertEquals(425.00, updated2.getPriceOfService());
//        System.out.println("Updated CleaningService 2: " + updated2);
//    }
//
//    @Test
//    @Order(4)
//    void delete() {
//        boolean success1 = repository.delete(cleaningService_1.getCleaningServiceID());
//        assertTrue(success1);
//        System.out.println("Deleted CleaningService 1 ID: " + cleaningService_1.getCleaningServiceID());
//
//    }
//
//    @Test
//    @Order(5)
//    void getAll() {
//        System.out.println("All Cleaning Services: " + repository.getAll());
//    }
//}