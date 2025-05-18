package za.ac.cput.factory.booking;

import org.junit.Test;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BookingFactoryTest {

    @Test
    public void testCreateBookingFactory1() {
        CleaningService cs_1 = new CleaningService.Builder() //cleaning service 1 of booking 1
                .setCleaningServiceID("cs_1")
                .setServiceName(CleaningService.ServiceName.WAXING_AND_POLISHING)
                .setPriceOfService(230.00)
                .setDuration(2.5)
                .build();

        CleaningService cs_2 = new CleaningService.Builder() //cleaning service 2 of booking 1
                .setCleaningServiceID("cs_2")
                .setServiceName(CleaningService.ServiceName.INTERIOR_CLEANING)
                .setPriceOfService(500.00)
                .setDuration(1)
                .build();

        List<CleaningService> booking1ListOfServices = new ArrayList<>();

        booking1ListOfServices.add(cs_1);
        booking1ListOfServices.add(cs_2);

        Booking booking1 = BookingFactory.createBookingFactory1(booking1ListOfServices, "wa_1", (LocalDateTime.of(2025, 5, 30, 12, 0)), "vehicle1", true, 500);

        assertNotNull(booking1);
        assertEquals("wa_1", booking1.getWashAttendantID());
        assertEquals("vehicle1", booking1.getVehicleID());
        assertEquals(2, booking1.getCleaningServices().size());
        System.out.println("Booking was successful: " + booking1.toString());
    }




    }

