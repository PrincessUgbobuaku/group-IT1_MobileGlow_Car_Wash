package za.ac.cput.factory.user.employee;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.user.employee.Manager;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerFactoryTest {

    @Test
    public void testCreateManager() {
        Manager manager = ManagerFactory.createManager(new Date());
        System.out.println("Manager created: " + manager);

        assertNotNull(manager);
        assertNotNull(manager.getManagerID());
        assertNotNull(manager.getHireDate());


    }
}
