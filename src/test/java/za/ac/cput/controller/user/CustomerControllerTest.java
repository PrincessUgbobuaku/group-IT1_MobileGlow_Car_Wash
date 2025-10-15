// // Thaakirah Watson, 230037550
// package za.ac.cput.controller.user;

// import jakarta.transaction.Transactional;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.junit.jupiter.api.MethodOrderer;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.annotation.DirtiesContext;
// import za.ac.cput.domain.user.Customer;
// import za.ac.cput.domain.user.User;
// import za.ac.cput.service.user.CustomerService;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// @Transactional
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// public class CustomerControllerTest {

//     @Autowired
//     private CustomerController controller;

//     @Autowired
//     private CustomerService customerService;

//     private Customer customer1;
//     private Customer customer2;

//     // Generate unique names to avoid conflicts
//     private String uniqueName1;
//     private String uniqueName2;
//     private String uniqueSurname1;
//     private String uniqueSurname2;

//     @BeforeEach
//     void setUp() {
//         // Generate unique names for each test run
//         String uuid1 = UUID.randomUUID().toString().substring(0, 6);
//         String uuid2 = UUID.randomUUID().toString().substring(0, 6);

//         uniqueName1 = "John" + uuid1;
//         uniqueSurname1 = "Doe" + uuid1;
//         uniqueName2 = "Jane" + uuid2;
//         uniqueSurname2 = "Smith" + uuid2;

//         // Create customers without hardcoded IDs (let database assign them)
//         customer1 = new Customer.Builder()
//                 .setUserName(uniqueName1)
//                 .setUserSurname(uniqueSurname1)
//                 .setIsActive(true)
//                 .setRoleDescription(User.RoleDescription.CLIENT)
//                 .setCustomerDOB(LocalDate.of(1990, 1, 1))
//                 .build();

//         customer2 = new Customer.Builder()
//                 .setUserName(uniqueName2)
//                 .setUserSurname(uniqueSurname2)
//                 .setIsActive(false)
//                 .setRoleDescription(User.RoleDescription.CLIENT)
//                 .setCustomerDOB(LocalDate.of(1992, 5, 10))
//                 .build();

//         // Persist them using controller and update references
//         ResponseEntity<Customer> response1 = controller.create(customer1);
//         ResponseEntity<Customer> response2 = controller.create(customer2);

//         // Update references with the persisted entities (they now have IDs)
//         customer1 = response1.getBody();
//         customer2 = response2.getBody();

//         System.out.println("Setup complete - Customer1 ID: " + customer1.getUserId() + ", Name: " + uniqueName1);
//         System.out.println("Setup complete - Customer2 ID: " + customer2.getUserId() + ", Name: " + uniqueName2);
//     }

//     @Test
//     @Order(1)
//     void create() {
//         String newUuid = UUID.randomUUID().toString().substring(0, 6);
//         Customer newCustomer = new Customer.Builder()
//                 .setUserName("Alice" + newUuid)
//                 .setUserSurname("Johnson" + newUuid)
//                 .setIsActive(true)
//                 .setRoleDescription(User.RoleDescription.CLIENT)
//                 .setCustomerDOB(LocalDate.of(1985, 12, 12))
//                 .build();

//         ResponseEntity<Customer> response = controller.create(newCustomer);
//         System.out.println("CREATE Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode()); // Assuming your controller returns 200
//         assertNotNull(response.getBody());
//         assertNotNull(response.getBody().getUserId());
//         assertEquals("Alice" + newUuid, response.getBody().getUserName());
//         assertEquals("Johnson" + newUuid, response.getBody().getUserSurname());
//     }

//     @Test
//     @Order(2)
//     void read() {
//         ResponseEntity<Customer> response = controller.read(customer1.getUserId());
//         System.out.println("READ Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals(customer1.getUserName(), response.getBody().getUserName());
//         assertEquals(customer1.getUserId(), response.getBody().getUserId());
//     }

//     @Test
//     @Order(3)
//     void findBySurname() {
//         ResponseEntity<List<Customer>> response = controller.findBySurname(uniqueSurname1);
//         System.out.println("FIND BY SURNAME Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertFalse(response.getBody().isEmpty());
//         assertTrue(response.getBody().stream()
//                 .anyMatch(c -> c.getUserSurname().equals(uniqueSurname1)));
//     }

//     @Test
//     @Order(4)
//     void update() {
//         String updatedSurname = "Updated" + uniqueSurname1;
//         Customer updatedCustomer = new Customer.Builder()
//                 .copy(customer1)
//                 .setUserSurname(updatedSurname)
//                 .build();

//         ResponseEntity<Customer> response = controller.update(customer1.getUserId(), updatedCustomer);
//         System.out.println("UPDATE Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertEquals(updatedSurname, response.getBody().getUserSurname());
//         assertEquals(customer1.getUserId(), response.getBody().getUserId());

//         // Update reference for other tests
//         customer1 = response.getBody();
//         uniqueSurname1 = updatedSurname;
//     }

//     @Test
//     @Order(5)
//     void findActiveCustomers() {
//         ResponseEntity<List<Customer>> response = controller.findActiveCustomers();
//         System.out.println("FIND ACTIVE CUSTOMERS Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertFalse(response.getBody().isEmpty());

//         // Verify all returned customers are active
//         assertTrue(response.getBody().stream().allMatch(Customer::getIsActive),
//                 "All returned customers should be active");

//         // Should find at least our active customer1
//         assertTrue(response.getBody().stream()
//                         .anyMatch(c -> c.getUserId().equals(customer1.getUserId())),
//                 "Should find our active customer");
//     }

//     @Test
//     @Order(6)
//     void findAll() {
//         ResponseEntity<List<Customer>> response = controller.findAll();
//         System.out.println("FIND ALL Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertTrue(response.getBody().size() >= 2, "Should find at least 2 customers");

//         // Verify our customers are in the list
//         List<Long> customerIds = response.getBody().stream()
//                 .map(Customer::getUserId)
//                 .toList();
//         assertTrue(customerIds.contains(customer1.getUserId()));
//         assertTrue(customerIds.contains(customer2.getUserId()));
//     }

//     @Test
//     @Order(7)
//     void delete() {
//         Long customerIdToDelete = customer2.getUserId();
//         ResponseEntity<Void> response = controller.delete(customerIdToDelete);
//         System.out.println("DELETE Result: " + response.getStatusCode());

//         assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

//         // Verify deletion by trying to read the deleted customer
//         ResponseEntity<Customer> readResponse = controller.read(customerIdToDelete);
//         assertEquals(HttpStatus.NOT_FOUND, readResponse.getStatusCode());
//     }

//     @Test
//     @Order(8)
//     void findBySurname_NotFound() {
//         String nonExistentSurname = "NonExistent" + UUID.randomUUID().toString().substring(0, 6);
//         ResponseEntity<List<Customer>> response = controller.findBySurname(nonExistentSurname);
//         System.out.println("FIND BY SURNAME (Non-existent) Result: " + response.getBody());

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotNull(response.getBody());
//         assertTrue(response.getBody().isEmpty(), "Should return empty list for non-existent surname");
//     }

//     @Test
//     @Order(9)
//     void read_NotFound() {
//         ResponseEntity<Customer> response = controller.read(99999L);
//         System.out.println("READ (Non-existent ID) Result: " + response.getStatusCode());

//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//     }
// }