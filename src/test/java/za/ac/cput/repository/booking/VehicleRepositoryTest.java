/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository Test class for Vehicle
 */

package za.ac.cput.repository.booking;

import org.junit.Before;
import org.junit.Test;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.repository.booking.impl.IVehicleRepository;

import static org.junit.Assert.*;

public class VehicleRepositoryTest {

    private IVehicleRepository repository;
    private Vehicle vehicle;

    @Before
    public void setUp() {
        repository = IVehicleRepository.getRepository();
        vehicle = new Vehicle.Builder()
                .setVehicleID("V001")
                .setCarPlateNumber("CA123456")
                .setCarMake("Toyota")
                .setCarColour("White")
                .setCarModel("Yaris")
                .setCustomerID("C001")
                .build();
        repository.create(vehicle);
    }

    @Test
    public void create() {
        assertNotNull(repository.read(vehicle.getVehicleID()));
    }

    @Test
    public void read() {
        Vehicle read = repository.read("V001");
        assertEquals(vehicle.getVehicleID(), read.getVehicleID());
    }

    @Test
    public void update() {
        Vehicle updated = new Vehicle.Builder()
                .setVehicleID("V001")
                .setCarPlateNumber("CA654321")
                .setCarMake("Toyota")
                .setCarColour("Black")
                .setCarModel("Corolla")
                .setCustomerID("C001")
                .build();
        repository.update(updated);
        assertEquals("Black", repository.read("V001").getCarColour());
    }

    @Test
    public void delete() {
        repository.delete("V001");
        assertNull(repository.read("V001"));
    }

    @Test
    public void getAll() {
        assertFalse(repository.getAll().isEmpty());
    }
}
