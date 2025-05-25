package za.ac.cput.repository.booking.impl;

import za.ac.cput.domain.booking.Booking;

import java.util.List;

public interface IBookingRepository {

    List<Booking> getAll();
}
