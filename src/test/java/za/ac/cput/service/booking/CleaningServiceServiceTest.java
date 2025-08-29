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
                2.0,
                "Interior Care"  // <-- Add category here
        );

        CleaningService saved = cleaningServiceService.create(cleaningService);
        assertNotNull(saved);
        assertEquals(cleaningService.getCleaningServiceID(), saved.getCleaningServiceID());
        assertEquals("Interior Care", saved.getCategory());  // Verify category saved correctly
        System.out.println("✅ Created Cleaning Service: " + saved);
    }

    @Test
    @Order(2)
    void testCreateDuplicateThrowsException() {
        // Attempt to create a duplicate record with the same serviceName
        CleaningService duplicate = CleaningServiceFactory.createCleaningService(
                cleaningService.getServiceName(),
                400.00,
                2.5,
                "Interior Care"  // Must include category here too
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cleaningServiceService.create(duplicate);
        });

        assertEquals("CleaningServiceService: Service already exists", exception.getMessage());
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

    // Uncomment if you want to test deletion
    // @Test
    // @Order(5)
    // @Rollback(false)
    // void testDelete() {
    //     boolean deleted = cleaningServiceService.delete(cleaningService.getCleaningServiceID());
    //     assertTrue(deleted);
    //     System.out.println("🗑️ Deleted Cleaning Service with ID: " + cleaningService.getCleaningServiceID());
    // }

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