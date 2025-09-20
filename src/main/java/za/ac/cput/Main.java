package za.ac.cput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.employee.WashAttendant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {

/*UNCOMMENT TO SEE HOW PROGRAM RUNS*/
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}