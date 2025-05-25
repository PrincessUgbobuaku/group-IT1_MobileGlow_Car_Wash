package za.ac.cput.factory.booking;

import org.junit.Test;
import za.ac.cput.domain.booking.CleaningService;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import za.ac.cput.domain.booking.CleaningService;


public class CleaningServiceFactoryTest {

    @Test
    public void testCreateCleaningServiceFactory1() {
        CleaningService cs_1 = CleaningServiceFactory.createCleaningServiceFactory1(CleaningService.ServiceName.ENGINE_CLEANING, 500, 2.5);

        assertNotNull(cs_1); // Ensure object is created
//        assertNotNull(cs_1.getCleaningServiceID()); // Ensure ID is generated
//        assertEquals(CleaningService.ServiceName.ENGINE_CLEANING, cs_1.getServiceName()); // Check correct enum
//        assertEquals(500.00, cs_1.getPriceOfService(), 0.01); // Check price
//        assertEquals(2.5, cs_1.getDuration(), 0.01); // Check duration
//        System.out.println(cs_1.toString());

    }
}
