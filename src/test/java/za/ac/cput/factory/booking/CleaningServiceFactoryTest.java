package za.ac.cput.factory.booking;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.CleaningService;

import static org.junit.jupiter.api.Assertions.*;

public class CleaningServiceFactoryTest {

    @Test
    public void testCreateSingleCleaningService() {
        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
                CleaningService.ServiceName.ENGINE_CLEANING,
                500,
                2.5,
                "Exterior Wash"  // ✅ new category
        );

        assertNotNull(cs_1); // Ensure object is created
        assertEquals(CleaningService.ServiceName.ENGINE_CLEANING, cs_1.getServiceName());
        assertEquals("Exterior Wash", cs_1.getCategory()); // ✅ verify category
        assertTrue(cs_1.getPriceOfService() > 0);

        System.out.println("Single cleaning service created:\n" + cs_1);
    }

    @Test
    public void testCreateMultipleCleaningServices() {
        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
                CleaningService.ServiceName.WAXING_AND_POLISHING,
                300,
                1.5,
                "Exterior Care"
        );

        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
                CleaningService.ServiceName.CERAMIC_COATING,
                700,
                2.0,
                "Paint Protection"
        );

        assertNotNull(cs_1);
        assertNotNull(cs_2);
        assertNotEquals(cs_1.getCleaningServiceID(), cs_2.getCleaningServiceID());
        assertEquals("Exterior Care", cs_1.getCategory());
        assertEquals("Paint Protection", cs_2.getCategory());

        System.out.println("Multiple cleaning services created:\n" + cs_1 + "\n" + cs_2);
    }
}