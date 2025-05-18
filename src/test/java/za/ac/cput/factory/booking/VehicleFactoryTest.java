/*
 Student name : Thaakirah Watson
 Student number: 230037550
 Description   : Test class for VehicleFactory
*/

package za.ac.cput.factory.booking;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.Vehicle;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleFactoryTest {

    @Test
    void testBuild1VehicleSuccess() {
        Vehicle vehicle = VehicleFactory.build1(
                "XYZ123",
                "Toyota",
                "Red",
                "Corolla",
                "CUST001"
        );

        assertNotNull(vehicle);
        assertNotNull(vehicle.getVehicleID());
        assertFalse(vehicle.getVehicleID().isBlank());
        assertEquals("Toyota", vehicle.getCarMake());
        assertEquals("CUST001", vehicle.getCustomerID());
    }

    @Test
    void testBuildVehicleWithMissingFields() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.build1("", "Toyota", "Red", "Corolla", "CUST001")
        );
        assertTrue(exception.getMessage().contains("Vehicle fields must not be null or empty"));
    }
}
