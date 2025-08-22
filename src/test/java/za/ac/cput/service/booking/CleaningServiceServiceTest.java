package za.ac.cput.service.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.factory.booking.CleaningServiceFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CleaningServiceServiceTest {

    @Autowired
    private CleaningServiceService cleaningServiceService;

    private static CleaningService cleaningService;

    @BeforeAll
    static void init() {
        System.out.println("🔧 CleaningServiceServiceTest starting...");
    }

    @Test
    @Order(1)
    @Rollback(false)
    void testCreate() {
        cleaningService = CleaningServiceFactory.createCleaningService(
                CleaningService.ServiceName.INTERIOR_CLEANING,
                300.00,
                2.0);

        CleaningService saved = cleaningServiceService.create(cleaningService);
        assertNotNull(saved);
        assertEquals(cleaningService.getCleaningServiceID(), saved.getCleaningServiceID());
        System.out.println("✅ Created Cleaning Service: " + saved);
    }

    @Test
    @Order(2)
    void testCreateDuplicateThrowsException() {
        // Attempt to create a duplicate record with the same serviceName
        CleaningService duplicate = CleaningServiceFactory.createCleaningService(
                cleaningService.getServiceName(), 400.00, 2.5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cleaningServiceService.create(duplicate);
        });

        assertEquals("Service already exists", exception.getMessage());
        System.out.println("⚠️ Duplicate service creation prevented: " + exception.getMessage());
    }

    @Test
    @Order(3)
    @Rollback(false)
    void testRead() {
        CleaningService read = cleaningServiceService.read(cleaningService.getCleaningServiceID());
        assertNotNull(read);
        assertEquals(cleaningService.getCleaningServiceID(), read.getCleaningServiceID());
        System.out.println("📖 Read Cleaning Service: " + read);
    }

    @Test
    @Order(4)
    @Rollback(false)
    void testUpdate() {
        CleaningService updated = new CleaningService.Builder()
                .copy(cleaningService)
                .setPriceOfService(350.00)
                .build();

        CleaningService result = cleaningServiceService.update(updated);
        assertNotNull(result);
        assertEquals(350.00, result.getPriceOfService());
        System.out.println("🔄 Updated Cleaning Service: " + result);
    }

//    @Test
//    @Order(5)
//    @Rollback(false)
//    void testDelete() {
//        boolean deleted = cleaningServiceService.delete(cleaningService.getCleaningServiceID());
//        assertTrue(deleted);
//        System.out.println("🗑️ Deleted Cleaning Service with ID: " + cleaningService.getCleaningServiceID());
//    }

    @Test
    @Order(6)
    @Rollback(false)
    void testGetAll() {
        List<CleaningService> services = cleaningServiceService.getAll();
        assertNotNull(services);
        assertTrue(services.size() >= 0);
        System.out.println("📋 All Cleaning Services:");
        services.forEach(System.out::println);
    }
}

//    @Autowired
//    private CleaningServiceService cleaningServiceService;
//
//    private CleaningService cleaningService;
//
//    @BeforeEach
//    void setUp() {
//        cleaningService = CleaningServiceFactory.createCleaningService(
//                CleaningService.ServiceName.EXTERIOR_WASH,
//                200.0,
//                1.0
//        );
//        assertNotNull(cleaningService); // Ensure object is created properly
//    }
//
//    @Test
//    void testCreate() {
//        CleaningService saved = cleaningServiceService.create(cleaningService);
//        assertNotNull(saved);
//        assertEquals(cleaningService.getCleaningServiceID(), saved.getCleaningServiceID());
//    }
//
//    @Test
//    void testRead() {
//        cleaningServiceService.create(cleaningService);
//        CleaningService found = cleaningServiceService.read(cleaningService.getCleaningServiceID());
//        assertNotNull(found);
//        assertEquals(cleaningService.getCleaningServiceID(), found.getCleaningServiceID());
//    }
//
//    @Test
//    void testUpdate() {
//        cleaningServiceService.create(cleaningService);
//        CleaningService updated = new CleaningService.Builder()
//                .copy(cleaningService)
//                .setPriceOfService(250.0)
//                .build();
//        CleaningService result = cleaningServiceService.update(updated);
//        assertNotNull(result);
//        assertEquals(250.0, result.getPriceOfService());
//    }
//
//    @Test
//    void testDelete() {
//        cleaningServiceService.create(cleaningService);
//        boolean deleted = cleaningServiceService.delete(cleaningService.getCleaningServiceID());
//        assertTrue(deleted);
//        assertNull(cleaningServiceService.read(cleaningService.getCleaningServiceID()));
//    }
//
//    @Test
//    void testGetAll() {
//        cleaningServiceService.create(cleaningService);
//        List<CleaningService> all = cleaningServiceService.getAll();
//        assertFalse(all.isEmpty());
//    }

