//package za.ac.cput.controller.user.employee;
//
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import za.ac.cput.domain.generic.Address;
//import za.ac.cput.domain.generic.Contact;
//import za.ac.cput.domain.user.Login;
//import za.ac.cput.domain.user.User;
//import za.ac.cput.domain.user.employee.Accountant;
//import za.ac.cput.factory.generic.AddressFactory;
//import za.ac.cput.factory.generic.ContactFactory;
//import za.ac.cput.factory.user.LoginFactory;
//import za.ac.cput.factory.user.employee.AccountantFactory;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.MethodName.class)
//class AccountantControllerTest {
//
//    private static Contact contact = ContactFactory.createContact("0725637252");
//    private static Address address = AddressFactory.createAddressFactory1(
//            "101", "Main Street", "Cape Town", "8000");
//    private static Login login = LoginFactory.createLogin("accountant@gmail.com", "password123");
//
//    private static Accountant accountant = AccountantFactory.createAccountant(
//            "John",
//            "Doe",
//            true,                          // isActive
//            User.RoleDescription.EMPLOYEE, // roleDescription
//            "Full-Time",                   // employeeType
//            true,                          // hasTaxFillingAuthority
//            contact,
//            address,
//            login
//    );
//
//
//    private static Accountant accountantWithId;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private static final String BASE_URL = "http://localhost:8080/MobileCarWash/Accountant";
//
//    @Test
//    void a_create() {
//        String url = BASE_URL + "/create";
//        ResponseEntity<Accountant> response = restTemplate.postForEntity(url, accountant, Accountant.class);
//        assertNotNull(response.getBody());
//        System.out.println("response: " + response.getBody());
//        accountantWithId = response.getBody();
//        assertNotNull(accountantWithId);
//        System.out.println("accountantWithId: " + accountantWithId);
//    }
//
//    @Test
//    void b_read() {
//        assertNotNull(accountantWithId, "accountantWithId is null");
//        String url = BASE_URL + "/read/" + accountantWithId.getUserId();
//        ResponseEntity<Accountant> response = restTemplate.getForEntity(url, Accountant.class);
//        assertNotNull(response.getBody());
//        System.out.println("response: " + response.getBody());
//    }
//
//    @Test
//    void c_update() {
//        assertNotNull(accountantWithId, "accountantWithId is null");
//        String url = BASE_URL + "/update";
//
//        Accountant updatedAccountant = new Accountant.Builder()
//                .copy(accountantWithId)
//                .setUserName("Jonathan")
//                .setHasTaxFillingAuthority(false)
//                .build();
//
//        restTemplate.put(url, updatedAccountant);
//
//        // Verify if update worked
//        ResponseEntity<Accountant> readResponse =
//                restTemplate.getForEntity(BASE_URL + "/read/" + updatedAccountant.getUserId(), Accountant.class);
//        assertEquals(HttpStatus.OK, readResponse.getStatusCode());
//
//        Accountant readAccountant = readResponse.getBody();
//        assertNotNull(readAccountant);
//        assertEquals("Jonathan", readAccountant.getUserName());
//        assertFalse(readAccountant.getHasTaxFillingAuthority());
//        System.out.println("Updated Accountant: " + readAccountant);
//    }
//
//    @Test
//    void d_getAllAccountants() {
//        String url = BASE_URL + "/getAllAccountants";
//        ResponseEntity<Accountant[]> response = restTemplate.getForEntity(url, Accountant[].class);
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().length > 0);
//        System.out.println("All Accountants:");
//        for (Accountant acct : response.getBody()) {
//            System.out.println(acct);
//        }
//    }
//
//    @Test
//    void e_delete() {
//        String url = BASE_URL + "/delete/" + accountantWithId.getUserId();
//        System.out.println("Deleting accountant: " + accountantWithId);
//        restTemplate.delete(url);
//
//        // Verify deletion
//        ResponseEntity<Accountant> readResponse =
//                restTemplate.getForEntity(BASE_URL + "/read/" + accountantWithId.getUserId(), Accountant.class);
//
//        // Expect 404 after deletion
//        assertEquals(HttpStatus.NOT_FOUND, readResponse.getStatusCode());
//        System.out.println("After deletion, status code: " + readResponse.getStatusCode());
//    }
//}