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
    void testBuildVehicleSuccess() {
        Vehicle vehicle = VehicleFactory.build(
                "VEH001",
                "XYZ123",
                "Toyota",
                "Red",
                "Corolla",
                "CUST001"
        );

        assertNotNull(vehicle);
        assertEquals("VEH001", vehicle.getVehicleID());
        assertEquals("Toyota", vehicle.getCarMake());
    }

    @Test
    void testBuildVehicleWithEmptyIDShouldFail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                VehicleFactory.build(
                        "", "XYZ123", "Toyota", "Red", "Corolla", "CUST001")
        );
        assertTrue(exception.getMessage().contains("Vehicle fields must not be null or empty"));
    }
}

