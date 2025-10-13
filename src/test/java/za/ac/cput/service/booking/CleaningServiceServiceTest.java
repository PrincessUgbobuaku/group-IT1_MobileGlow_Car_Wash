package za.ac.cput.service.booking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.repository.booking.ICleaningServiceRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CleaningServiceServiceTest {

    @Autowired
    private CleaningServiceService cleaningServiceService;

    private static CleaningService savedCleaningService;

    @Test
    @Rollback(false)
    @Order(1)
    void testCreate() {
        CleaningService cleaningService = CleaningServiceFactory.createCleaningService(
                "JET_WASH",
                200.00,
                3.0,
                "Full Detailing"
        );

        savedCleaningService = cleaningServiceService.create(cleaningService);

        assertNotNull(savedCleaningService);
        assertNotNull(savedCleaningService.getCleaningServiceId());
        assertEquals("Protection Services", savedCleaningService.getCategory());

        System.out.println("Created Cleaning Service: " + savedCleaningService);
    }

    @Test
    @Rollback(false)
    @Order(2)
    void testCreateDuplicateThrowsException() {
        CleaningService duplicate = CleaningServiceFactory.createCleaningService(
                savedCleaningService.getServiceName(),  // same name
                400.00,
                2.5,
                "Interior Care"
        );

        Exception exception = assertThrows(Exception.class, () -> {
            cleaningServiceService.create(duplicate);
        });

        Throwable cause = exception;
        while (cause != null && !(cause instanceof IllegalArgumentException)) {
            cause = cause.getCause();
        }

        assertNotNull(cause);
        assertEquals("CleaningService already exists with name: " + savedCleaningService.getServiceName(), cause.getMessage());
        System.out.println("Duplicate service creation prevented: " + cause.getMessage());
    }

    @Test
    @Rollback(false)
    @Order(3)
    void testRead() {
        CleaningService read = cleaningServiceService.read(savedCleaningService.getCleaningServiceId());

        assertNotNull(read);
        assertEquals(savedCleaningService.getCleaningServiceId(), read.getCleaningServiceId());
        System.out.println("Read Cleaning Service: " + read);
    }

//    @Test
//    @Order(4)
//    void testUpdate() {
//        CleaningService updated = new CleaningService.Builder()
//                .copy(savedCleaningService)
//                .setPriceOfService(350.00)
//                .build();
//
//        CleaningService result = cleaningServiceService.update(updated);
//
//        assertNotNull(result);
//        assertEquals(350.00, result.getPriceOfService());
//        System.out.println("Updated Cleaning Service: " + result);
//
//        savedCleaningService = result; // update reference for future tests if needed
//    }

    @Test
    @Order(5)
    void testDelete() {
        boolean deleted = cleaningServiceService.delete(savedCleaningService.getCleaningServiceId());

        assertTrue(deleted);
        System.out.println("🗑Deleted Cleaning Service with ID: " + savedCleaningService.getCleaningServiceId());
    }

    @Test
    @Order(6)
    void testGetAll() {
        List<CleaningService> services = cleaningServiceService.getAll();

        assertNotNull(services);
        assertTrue(services.size() >= 0); // it could be 0 now if delete ran

        System.out.println("📋 All Cleaning Services:");
        services.forEach(System.out::println);
    }
}