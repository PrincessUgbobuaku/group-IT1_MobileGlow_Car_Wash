package za.ac.cput.repository.booking.impl;

import za.ac.cput.domain.booking.CleaningService;

import java.util.List;

public interface ICleaningServiceRepository {

    List<CleaningService> getAll();
}
