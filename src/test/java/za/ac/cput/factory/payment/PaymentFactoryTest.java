package za.ac.cput.factory.payment;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.booking.Booking;
import za.ac.cput.domain.booking.CleaningService;
import za.ac.cput.domain.booking.Vehicle;
import za.ac.cput.domain.payment.Payment;
import za.ac.cput.domain.user.Customer;
import za.ac.cput.domain.user.employee.WashAttendant;
import za.ac.cput.factory.booking.BookingFactory;
import za.ac.cput.factory.booking.CleaningServiceFactory;
import za.ac.cput.factory.booking.VehicleFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentFactoryTest {

    @Test
    public void testCreatePaymentForExistingBooking() {
        // Creating cleaning services
        CleaningService cs_1 = CleaningServiceFactory.createCleaningService(
                "WAXING_AND_POLISHING",
                300,
                1.5,
                "Exterior Care"
        );

        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
                "CERAMIC_COATING",
                700,
                2.0,
                "Paint Protection"
        );

        List<CleaningService> services = List.of(cs_1, cs_2);

        // Dummy customer
        Customer cust_001 = new Customer.Builder()
                .setUserName("Brian")
                .setUserSurname("Kabongo")
                .setCustomerDOB(LocalDate.of(1990, 1, 1)).build();


        Vehicle vehicle = VehicleFactory.createVehicle(
                "ABC123",
                "Toyota",
                "Red",
                "Corolla",
                cust_001
        );


        // Dummy wash attendant
        WashAttendant washAttendant = new WashAttendant.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .build();

        Booking booking = BookingFactory.createBooking(
                services,
                null,
                washAttendant,
                LocalDateTime.of(2025, 10, 30, 12, 0),
                vehicle,
                true,
                500
        );

        assertNotNull(booking);

        Payment payment = PaymentFactory.createPayment(
                booking,
                500,
                Payment.PaymentMethod.DEBIT,
                Payment.PaymentStatus.PAID
        );

        assertNotNull(payment);
        // Instead of checking bookingID (which is null), check booking reference equality
        assertSame(booking, payment.getBooking());
        assertEquals(500, payment.getPaymentAmount());

        System.out.println("Payment linked to booking:\n" + payment);
    }

    @Test
    public void testCreateMultiplePaymentsForOneBooking() {
        // Creating cleaning service
        CleaningService cs_2 = CleaningServiceFactory.createCleaningService(
                "CERAMIC_COATING",
                700,
                2.0,
                "Paint Protection"
        );

        List<CleaningService> services = List.of(cs_2);

        // Dummy customer
        Customer cust_001 = new Customer.Builder()
                .setUserName("Brian")
                .setUserSurname("Kabongo")
                .setCustomerDOB(LocalDate.of(1990, 1, 1)).build();


        Vehicle vehicle = VehicleFactory.createVehicle(
                "ABC123",
                "Toyota",
                "Red",
                "Corolla",
                cust_001
        );


        // Dummy wash attendant
        WashAttendant washAttendant = new WashAttendant.Builder()
                .setUserName("John")
                .setUserSurname("Doe")
                .build();

        Booking booking = BookingFactory.createBooking(
                services,
                null,
                washAttendant,
                LocalDateTime.of(2025, 10, 30, 12, 0),
                vehicle,
                true,
                500
        );

        assertNotNull(booking);

        Payment payment1 = PaymentFactory.createPayment(
                booking,
                250,
                Payment.PaymentMethod.DEBIT,
                Payment.PaymentStatus.PENDING
        );

        Payment payment2 = PaymentFactory.createPayment(
                booking,
                250,
                Payment.PaymentMethod.PAYPAL,
                Payment.PaymentStatus.PENDING
        );

        List<Payment> payments = new ArrayList<>();
        payments.add(payment1);
        payments.add(payment2);

        assertNotNull(payment1);
        assertNotNull(payment2);

        // Assert that both payments reference the same booking instance
        assertSame(booking, payment1.getBooking());
        assertSame(booking, payment2.getBooking());

        System.out.println("Multiple payments for a single booking:");
        for (Payment payment : payments) {
            System.out.println(payment);
        }    }

}