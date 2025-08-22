/*
Student name: Thaakirah Watson
Student number: 230037550
Description: Repository implementation class for Vehicle
 */

package za.ac.cput.repository.booking.impl;

import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.repository.booking.VehicleRepository;

import java.util.HashSet;
import java.util.Set;

public class IVehicleRepository implements VehicleRepository {
    private static IVehicleRepository repository = null;
    private Set<Vehicle> vehicles = new HashSet<>();

    private IVehicleRepository() {}

    public static IVehicleRepository getRepository() {
        if (repository == null) {
            repository = new IVehicleRepository();
        }
        return repository;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        vehicles.add(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle read(String id) {
        return vehicles.stream()
                .filter(v -> v.getVehicleID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        Vehicle old = read(vehicle.getVehicleID());
        if (old != null) {
            vehicles.remove(old);
            vehicles.add(vehicle);
            return vehicle;
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        Vehicle vehicle = read(id);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            return true;
        }
        return false;
    }

    @Override
    public Set<Vehicle> getAll() {
        return vehicles;
    }
}