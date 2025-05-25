/*
 Student name : Thaakirah Watson
 Student number: 230037550
 Description   : Test class for VehicleFactory
*/

package za.ac.cput.factory.booking;

import org.junit.Test;
import za.ac.cput.domain.booking.Vehicle;

import static org.junit.Assert.*;

public class VehicleFactoryTest {

    @Test
    public void testBuild1VehicleSuccess() {
        Vehicle vehicle = VehicleFactory.build1(
                "XYZ123",
                "Toyota",
                "Red",
                "Corolla",
                "CUST001"
        );

        assertNotNull(vehicle);
        assertNotNull(vehicle.getVehicleID());
        assertFalse(vehicle.getVehicleID().isEmpty());
        assertEquals("Toyota", vehicle.getCarMake());
        assertEquals("CUST001", vehicle.getCustomerID());
    }

    @Test
    public void testBuildVehicleWithMissingFields() {
        try {
            VehicleFactory.build1("", "Toyota", "Red", "Corolla", "CUST001");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Vehicle fields must not be null or empty"));
        }
    }
}
