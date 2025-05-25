/*
    ManagerRepositoryImplTest
Author: Abulele Voki(230778941)
Date: 11 May 2025
*/

package za.ac.cput.repository.user.employee;

import org.junit.*;
import za.ac.cput.domain.user.employee.Manager;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ManagerRepositoryTest {
//
//    private static ManagerRepository repository;
//    private static Manager manager;
//
//    @BeforeClass
//    public static void setUp() {
//        repository = ManagerRepository.getRepository();
//        manager = new Manager.Builder()
//                .setManagerID("M001")
//                .setHireDate(new Date())
//                .build();
//    }
//
//    @Test
//    public void testCreate() {
//        Manager created = repository.create(manager);
//        assertNotNull(created);
//        assertEquals("M001", created.getManagerID());
//    }
//
//    @Test
//    public void testRead() {
//        Manager read = repository.read("M001");
//        assertNotNull(read);
//    }
//
//    @Test
//    public void testUpdate() {
//        Date newDate = new Date();
//        Manager updated = new Manager.Builder()
//                .setManagerID("M001")
//                .setHireDate(newDate)
//                .build();
//        Manager result = repository.update(updated);
//        assertNotNull(result);
//        assertEquals(newDate, result.getHireDate());
//    }
//
//    @Test
//    public void testDelete() {
//        boolean success = repository.delete("M001");
//        assertTrue(success);
//    }
//
//    @Test
//    public void testGetAll() {
//        List<Manager> list = repository.getAll();
//        assertNotNull(list);
//    }
//}
