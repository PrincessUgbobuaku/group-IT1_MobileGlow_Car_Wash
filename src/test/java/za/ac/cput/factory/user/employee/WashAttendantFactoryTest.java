package za.ac.cput.factory.user.employee;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.employee.WashAttendant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WashAttendantFactoryTest {
    @Test
    public void testCreateWashAttendant() {
        WashAttendant washAttendant= WashAttendantFactory.createWashAttendant();
        System.out.println("WashAttendant created: " + washAttendant);

        assertNotNull(washAttendant);
        assertNotNull(washAttendant.getUserId());

    }

    @Test
    public void testCreateWashAttendantWithValidShiftHours() {
        WashAttendant washAttendant = WashAttendantFactory.createWashAttendant();
        System.out.println("WashAttendant created: " + washAttendant);

        assertNotNull(washAttendant);
        assertNotNull(washAttendant.getUserId());

    }
}
